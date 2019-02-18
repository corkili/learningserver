package com.corkili.learningserver.bo;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.corkili.learningserver.common.ServiceUtils;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Question implements BusinessObject {

    public Question() {
        this.imagePaths = new ArrayList<>();
        this.choices = new HashMap<>();
    }

    private Long id;

    private Date createTime;

    private Date updateTime;

    private String question;

    /**
     * Format:
     *   use {!!!} divide each image path.
     */
    private List<String> imagePaths;

    private QuestionType questionType;

    private boolean autoCheck;

    /**
     * When questionType is SingleChoice or MultipleChoice, use the attribute store choices.
     *
     * String Format, use {###} divide each choice, use {@@@} divide choiceIndex and content in each choice:
     *
     *   choiceIndex{@@@}content{###}choiceIndex{@@@}content ...
     *
     * Example:
     *
     *   1{@@@}choice A{###}2{@@@}choice B{###}3{@@@}choice C
     *
     */
    private Map<Integer, String> choices;

    /**
     * Use different format to represent different answer, depend on questionType:
     *
     * 1. SingleFilling: List<String>
     *   use {$$$} divide each correctAnswer, if the response from student is one of the correctAnswer, response is correct:
     *
     *   correctAnswer1{$$$}correctAnswer2{$$$}correctAnswer3
     *
     * 2. MultipleFilling:
     *   use {***} divide each answer, use {%%%} divide answerIndex and answerContent in each answer, 
     *   format of answerContent use the format of SingleFilling:
     *
     *   answerIndex{%%%}answerContent{***}answerIndex{%%%}answerContent
     *
     * 3. SingleChoice: int
     *   a integer represent the correct choice index described in choices attribute.
     *   
     * 4. MultipleChoice: MultipleChoiceAnswer
     *   use {|||} divide each correctChoice if get all score when select all correctChoice and get half score when
     *   select part of these correctChoice, use {&&&} divide each correctChoice if get all score only when select all correctChoice.
     *
     *   correctChoice1{|||}correctChoice2{|||}correctChoice3
     *
     *   correctChoice1{&&&}correctChoice2{&&&}correctChoice3
     *
     * 5. Essay:
     *    a string represent the correct answer. if contains image(s), use {##image##} divide text and imagePaths,
     *    then use {!!!} divide each image path in imagePaths
     *
     */
    private Answer answer;

    private Long authorId;

    public void setImagePaths(String imagePathsStr) {
        imagePaths = ServiceUtils.string2List(imagePathsStr, Pattern.compile("\\{!!!}"));
    }

    public void addImagePath(String imagePath) {
        if (StringUtils.isNotBlank(imagePath)) {
            imagePaths.add(imagePath);
        }
    }

    public List<String> getImagePaths() {
        return imagePaths;
    }

    public String getImagePathsStr() {
        return ServiceUtils.list2String(imagePaths, "{!!!}");
    }

    public void setChoices(String choicesStr) {
        List<String> choicePairs = ServiceUtils.string2List(choicesStr, Pattern.compile("\\{###}"));
        Map<Integer, String> map = new HashMap<>();
        for (String choicePair : choicePairs) {
            String[] keyValue = choicePair.split("\\{@@@}");
            if (keyValue.length != 2) {
                continue;
            }
            try {
                map.put(Integer.valueOf(keyValue[0]), keyValue[1]);
            } catch (NumberFormatException ignored) {
            }
        }
        this.choices = map;
    }

    public void putChoice(int index, String choice) {
        if (StringUtils.isNotBlank(choice)) {
            this.choices.put(index, choice);
        }
    }

    public Map<Integer, String> getChoices() {
        return choices;
    }

    public String getChoicesStr() {
        if (this.choices == null) {
            return "";
        }
        List<String> pairs = new LinkedList<>();
        this.choices.forEach((index, choice) -> pairs.add(index + "{@@@}" + choice));
        return ServiceUtils.list2String(pairs, "{###}");
    }

    public Answer getAnswer() {
        return this.answer;
    }

    public String getAnswerStr() {
        if (this.answer == null) {
            return "";
        } else {
            return this.answer.getAnswer();
        }
    }

    public void setAnswer(String answer, QuestionType questionType) {
        if (questionType == null || StringUtils.isBlank(answer)) {
            return;
        }
        this.questionType = questionType;
        try {
            this.answer = questionType.getAnswerType().getConstructor(String.class).newInstance(answer);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public boolean isSingleFillingQuestion() {
        return questionType == QuestionType.SingleFilling;
    }

    public boolean isMultipleFillingQuestion() {
        return questionType == QuestionType.MultipleFilling;
    }

    public boolean isFillingQuestion() {
        return questionType == QuestionType.SingleFilling || questionType == QuestionType.MultipleFilling;
    }

    public boolean isSingleChoiceQuestion() {
        return questionType == QuestionType.SingleChoice;
    }

    public boolean isMultipleChoiceQuestion() {
        return questionType == QuestionType.MultipleChoice;
    }

    public boolean isChoiceQuestion() {
        return questionType == QuestionType.SingleChoice || questionType == QuestionType.MultipleChoice;
    }

    public boolean isEssayQuestion() {
        return questionType == QuestionType.Essay;
    }

    public interface Answer {
        String getAnswer();

        void setAnswer(String answer);
    }

    public static abstract class AbstractAnswer implements Answer {

        public AbstractAnswer() {

        }

        public AbstractAnswer(String answer) {
            setAnswer(answer);
        }
    }

    @Setter
    @Getter
    @ToString
    @EqualsAndHashCode(callSuper = true)
    public static class SingleFillingAnswer extends AbstractAnswer {

        private List<String> answerList;

        public SingleFillingAnswer() {
            this.answerList = new LinkedList<>();
        }

        public SingleFillingAnswer(String answer) {
            super(answer);
        }

        @Override
        public String getAnswer() {
            return ServiceUtils.list2String(answerList, "{$$$}");
        }

        @Override
        public void setAnswer(String answer) {
            this.answerList = ServiceUtils.string2List(answer, Pattern.compile("\\{\\$\\$\\$}"));
        }
    }

    @Setter
    @Getter
    @ToString
    @EqualsAndHashCode(callSuper = true)
    public static class MultipleFillingAnswer extends AbstractAnswer {

        private Map<Integer, SingleFillingAnswer> answerMap;

        public MultipleFillingAnswer() {
            this.answerMap = new HashMap<>();
        }

        public MultipleFillingAnswer(String answer) {
            super(answer);
        }

        @Override
        public String getAnswer() {
            List<String> pairs = new LinkedList<>();
            this.answerMap.forEach((index, sfa) -> pairs.add(index + "{%%%}" + sfa.getAnswer()));
            return ServiceUtils.list2String(pairs, "{***}");
        }

        @Override
        public void setAnswer(String answer) {
            List<String> choicePairs = ServiceUtils.string2List(answer, Pattern.compile("\\{\\*\\*\\*}"));
            Map<Integer, SingleFillingAnswer> map = new HashMap<>();
            for (String choicePair : choicePairs) {
                String[] keyValue = choicePair.split("\\{%%%}");
                if (keyValue.length != 2) {
                    continue;
                }
                try {
                    map.put(Integer.valueOf(keyValue[0]), new SingleFillingAnswer(keyValue[1]));
                } catch (NumberFormatException ignored) {
                }
            }
            this.answerMap = map;
        }
    }

    @Setter
    @Getter
    @ToString
    @EqualsAndHashCode(callSuper = true)
    public static class SingleChoiceAnswer extends AbstractAnswer {

        private int choice;

        public SingleChoiceAnswer(int choice) {
            this.choice = choice;
        }

        public SingleChoiceAnswer(String answer) {
            super(answer);
        }

        @Override
        public String getAnswer() {
            return String.valueOf(choice);
        }

        @Override
        public void setAnswer(String answer) {
            choice = Integer.parseInt(answer);
        }
    }

    @Setter
    @Getter
    @ToString
    @EqualsAndHashCode(callSuper = true)
    public static class MultipleChoiceAnswer extends AbstractAnswer {
        private List<Integer> choices;
        private boolean selectAllIsCorrect;

        public MultipleChoiceAnswer(boolean selectAllIsCorrect) {
            choices = new LinkedList<>();
            this.selectAllIsCorrect = selectAllIsCorrect;
        }

        public MultipleChoiceAnswer(String answer) {
            super(answer);
        }

        @Override
        public String getAnswer() {
            if (selectAllIsCorrect) {
                return ServiceUtils.list2String(choices, "{&&&}");
            } else {
                return ServiceUtils.list2String(choices, "{|||}");
            }
        }

        @Override
        public void setAnswer(String answer) {
            if (answer.contains("{&&&}") && answer.contains("{|||}")) {
                return;
            }
            if (answer.contains("{|||}")) {
                selectAllIsCorrect = false;
            } else {
                selectAllIsCorrect = true;
            }
            List<String> choiceList;
            if (selectAllIsCorrect) {
                choiceList = ServiceUtils.string2List(answer, Pattern.compile("\\{&&&}"));
            } else {
                choiceList = ServiceUtils.string2List(answer, Pattern.compile("\\{\\|\\|\\|}"));
            }
            List<Integer> choices = new ArrayList<>();
            for (String choice : choiceList) {
                try {
                    choices.add(Integer.valueOf(choice));
                } catch (NumberFormatException ignored) {

                }
            }
            this.choices = choices;
        }
    }

    @Setter
    @Getter
    @ToString
    @EqualsAndHashCode(callSuper = true)
    public static class EssayAnswer extends AbstractAnswer {

        private String text;
        private List<String> imagePaths;

        public EssayAnswer() {
            imagePaths = new ArrayList<>();
        }

        public EssayAnswer(String answer) {
            super(answer);
            imagePaths = new ArrayList<>();
        }

        @Override
        public String getAnswer() {
            return text + (imagePaths.isEmpty() ? "" :
                    ("{##image##}" + ServiceUtils.list2String(imagePaths, "{!!!}")));
        }

        @Override
        public void setAnswer(String answer) {
            if (StringUtils.isBlank(answer)) {
                return;
            }
            if (answer.startsWith("{##image##}")) {
                this.text = "";
                this.imagePaths = ServiceUtils.string2List(answer, Pattern.compile("\\{!!!}"));
            } else {
                if (answer.contains("{##image##}") && !answer.endsWith("{##image##}")) {
                    String[] tmp = answer.split("\\{##image##}");
                    this.text = tmp[0];
                    this.imagePaths = ServiceUtils.string2List(tmp[1], Pattern.compile("\\{!!!}"));
                } else {
                    if (answer.endsWith("{##image##}")) {
                        this.text = answer.substring(0, answer.length() - "{##image##}".length());
                    } else {
                        this.text = answer;
                    }
                    this.imagePaths = new ArrayList<>();
                }
            }
        }
    }

}
