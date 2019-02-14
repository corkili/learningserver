package com.corkili.learningserver.bo;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
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
public class SubmittedExam implements BusinessObject {

    private Long id;

    private Date createTime;

    private Date updateTime;

    /**
     * Format:
     *   use {^^^} divide each question's submitted answer
     *
     *   use {###} divide question index, submitted answer, score(negative if not checked)
     *
     *   use {***} divide each filling answer in submitted answer,
     *   use {%%%} divide answerIndex and answerContent in each filling answer, if questionType is MultipleFilling
     *
     *   use {&&&} divide choice in submitted answer, if questionType is MultipleChoice
     *
     *   when type is essay, a string represent the correct answer. if contains image(s),
     *   use {##image##} divide text and imagePaths, then use {!!!} divide each image path in imagePaths
     */
    private Map<Integer, InnerSubmittedAnswer> submittedAnswers;

    private boolean alreadyCheckAllAnswer;

    private double totalScore;

    private Long belongExamId;

    private Long submitterId;

    public void setSubmittedAnswers(String submittedAnswersStr, Map<Integer, QuestionType> questionTypeMap) {
        List<String> submittedAnswerList = ServiceUtils.string2List(submittedAnswersStr, Pattern.compile("\\{\\^\\^\\^}"));
        for (String answer : submittedAnswerList) {
            InnerSubmittedAnswer innerSubmittedAnswer = new InnerSubmittedAnswer(answer, questionTypeMap, belongExamId);
            submittedAnswers.put(innerSubmittedAnswer.getQuestionIndex(), innerSubmittedAnswer);
        }
    }

    public String getSubmittedAnswersStr() {
        List<String> submittedAnswerList = new LinkedList<>();
        for (InnerSubmittedAnswer innerSubmittedAnswer : submittedAnswers.values()) {
            submittedAnswerList.add(innerSubmittedAnswer.getAnswer());
        }
        return ServiceUtils.list2String(submittedAnswerList, "{^^^}");
    }

    @Getter
    @Setter
    public static class InnerSubmittedAnswer {
        private int questionIndex;
        private SubmittedAnswer submittedAnswer;
        private double score;

        private InnerSubmittedAnswer(String answer, Map<Integer, QuestionType> questionTypeMap, Long belongExamId) {
            setAnswer(answer, questionTypeMap, belongExamId);
        }

        public String getAnswer() {
            return questionIndex + "{###}" + submittedAnswer.getAnswer() + "{###}" + score;
        }

        public void setAnswer(String answer, Map<Integer, QuestionType> questionTypeMap, Long belongExamId) {
            if (StringUtils.isBlank(answer)) {
                throw new IllegalArgumentException("answer is null or blank!");
            }
            String[] tmp = answer.split("\\{###}");
            if (tmp.length != 3) {
                throw new IllegalArgumentException("answer's format invalid!");
            }
            this.questionIndex = Integer.parseInt(tmp[0]);
            this.score = -1;
            try {
                QuestionType questionType = questionTypeMap.get(questionIndex);
                if (questionType == null) {
                    throw new IllegalArgumentException(ServiceUtils.format(
                            "questionType of question [{}] associated with exam [{}] not exist!",
                            questionIndex, belongExamId));
                }
                this.submittedAnswer = questionType.getSubmittedAnswerType()
                        .getConstructor(String.class).newInstance(tmp[1]);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

}
