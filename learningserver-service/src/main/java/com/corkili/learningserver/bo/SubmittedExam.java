package com.corkili.learningserver.bo;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
    private String submittedAnswers;

    private boolean alreadyCheckAllAnswer;

    private double totalScore;

    private Long belongExamId;

    private Long submitterId;

    @Getter
    @Setter
    public static class InnerSubmittedAnswer {
        private int questionIndex;
        private SubmittedAnswer submittedAnswer;
        private double score;

        public String getAnswer() {
            return questionIndex + "{###}" + submittedAnswer.getAnswer() + "{###}" + score;
        }

        public void setAnswer(String answer, QuestionType questionType) {
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
                this.submittedAnswer = questionType.getSubmittedAnswerType()
                        .getConstructor(String.class).newInstance(tmp[1]);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

}
