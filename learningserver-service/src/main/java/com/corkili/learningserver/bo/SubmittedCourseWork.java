package com.corkili.learningserver.bo;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubmittedCourseWork {

    private Long id;

    private Date createTime;

    private Date updateTime;

    /**
     * Format:
     *   use {^^^} divide each question's submitted answer
     *
     *   use {###} divide question index, submitted answer, check status(0 or 1)
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

    private CourseWork belongCourseWork;

    private User submitter;

    @Getter
    @Setter
    public static class InnerSubmittedAnswer {
        private int questionIndex;
        private SubmittedAnswer submittedAnswer;
        private boolean checked;

        public String getAnswer() {
            return questionIndex + "{###}" + submittedAnswer.getAnswer() + "{###}" + (checked ? 1 : 0);
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
            this.checked = false;
            try {
                this.submittedAnswer = questionType.getSubmittedAnswerType()
                        .getConstructor(String.class).newInstance(tmp[1]);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

}
