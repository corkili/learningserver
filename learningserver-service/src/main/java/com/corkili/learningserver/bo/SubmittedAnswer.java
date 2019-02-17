package com.corkili.learningserver.bo;

import java.util.ArrayList;
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

public interface SubmittedAnswer {

    String getAnswer();

    void setAnswer(String answer);

    abstract class AbstractSubmittedAnswer implements SubmittedAnswer {

        public AbstractSubmittedAnswer(String answer) {
            setAnswer(answer);
        }
    }

    @Setter
    @Getter
    @ToString
    @EqualsAndHashCode(callSuper = true)
    class SingleFillingSubmittedAnswer extends AbstractSubmittedAnswer {

        private String answer;

        public SingleFillingSubmittedAnswer(String answer) {
            super(answer);
        }

        @Override
        public String getAnswer() {
            return answer;
        }

        @Override
        public void setAnswer(String answer) {
            this.answer = answer;
        }
    }

    @Setter
    @Getter
    @ToString
    @EqualsAndHashCode(callSuper = true)
    class MultipleFillingSubmittedAnswer extends AbstractSubmittedAnswer {

        private Map<Integer, Pair> answerMap;

        public MultipleFillingSubmittedAnswer(String answer) {
            super(answer);
        }

        @Override
        public String getAnswer() {
            List<String> pairs = new LinkedList<>();
            this.answerMap.values().forEach(a -> pairs.add(a.index + "{%%%}" + a.answer + "{%%%}" + a.scoreOrCheckStatus));
            return ServiceUtils.list2String(pairs, "{***}");
        }

        @Override
        public void setAnswer(String answer) {
            List<String> choicePairs = ServiceUtils.string2List(answer, Pattern.compile("\\{\\*\\*\\*}"));
            Map<Integer, Pair> map = new HashMap<>();
            for (String choicePair : choicePairs) {
                String[] tmp = choicePair.split("\\{%%%}");
                if (tmp.length != 3) {
                    continue;
                }
                try {
                    Pair pair = new Pair(Integer.valueOf(tmp[0]), tmp[1], Double.parseDouble(tmp[2]));
                    map.put(pair.index, pair);
                } catch (NumberFormatException ignored) {
                }
            }
            this.answerMap = map;
        }

        @Setter
        @Getter
        @ToString
        @EqualsAndHashCode
        public static class Pair {
            int index;
            String answer;
            double scoreOrCheckStatus;

            private Pair(int index, String answer, double scoreOrCheckStatus) {
                this.index = index;
                this.answer = answer;
                this.scoreOrCheckStatus = scoreOrCheckStatus;
            }
        }
    }

    @Setter
    @Getter
    @ToString
    @EqualsAndHashCode(callSuper = true)
    class SingleChoiceSubmittedAnswer extends AbstractSubmittedAnswer {

        private int choice;

        public SingleChoiceSubmittedAnswer(String answer) {
            super(answer);
        }

        @Override
        public String getAnswer() {
            return String.valueOf(choice);
        }

        @Override
        public void setAnswer(String answer) {
            this.choice = Integer.parseInt(answer);
        }
    }

    @Setter
    @Getter
    @ToString
    @EqualsAndHashCode(callSuper = true)
    class MultipleChoiceSubmittedAnswer extends AbstractSubmittedAnswer {

        private List<Integer> choices;

        public MultipleChoiceSubmittedAnswer(String answer) {
            super(answer);
        }

        @Override
        public String getAnswer() {
            return ServiceUtils.list2String(choices, "{&&&}");
        }

        @Override
        public void setAnswer(String answer) {
            List<String> choiceList = ServiceUtils.string2List(answer, Pattern.compile("\\{&&&}"));
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
    class EssaySubmittedAnswer extends AbstractSubmittedAnswer {

        private String text;
        private List<String> imagePaths;

        public EssaySubmittedAnswer(String answer) {
            super(answer);
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
