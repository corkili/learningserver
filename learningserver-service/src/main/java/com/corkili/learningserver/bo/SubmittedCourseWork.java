package com.corkili.learningserver.bo;

import java.lang.reflect.InvocationTargetException;
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
public class SubmittedCourseWork implements BusinessObject {

    private Long id;

    private Date createTime;

    private Date updateTime;

    /**
     * Format:
     *   use {^^^} divide each question's submitted answer
     *
     *   use {###} divide question index, submitted answer, check status(-1 not checked, 0 false, 1 true/half_true, 2 true)
     *
     *   use {***} divide each filling answer in submitted answer,
     *   use {%%%} divide answerIndex, answerContent and checkStatus (-1 not checked, 0 false, 1 true) in each filling answer, if questionType is MultipleFilling
     *
     *   use {&&&} divide choice in submitted answer, if questionType is MultipleChoice
     *
     *   when type is essay, a string represent the correct answer. if contains image(s),
     *   use {##image##} divide text and imagePaths, then use {!!!} divide each image path in imagePaths
     */
    private Map<Integer, InnerSubmittedAnswer> submittedAnswers;

    private boolean alreadyCheckAllAnswer;

    private boolean finished;

    private Long belongCourseWorkId;

    private Long submitterId;

    public SubmittedCourseWork() {
        this.submittedAnswers = new HashMap<>();
    }

    public static SubmittedCourseWork copyFrom(SubmittedCourseWork submittedCourseWork) {
        SubmittedCourseWork copySubmittedCourseWork = new SubmittedCourseWork();
        copySubmittedCourseWork.id = submittedCourseWork.id;
        copySubmittedCourseWork.createTime = submittedCourseWork.createTime;
        copySubmittedCourseWork.updateTime = submittedCourseWork.updateTime;
        Map<Integer, QuestionType> questionTypeMap = new HashMap<>();
        submittedCourseWork.submittedAnswers.forEach((k, v) -> questionTypeMap.put(k, v.getQuestionType()));
        copySubmittedCourseWork.setSubmittedAnswers(submittedCourseWork.getSubmittedAnswersStr(), questionTypeMap);
        copySubmittedCourseWork.alreadyCheckAllAnswer = submittedCourseWork.alreadyCheckAllAnswer;
        copySubmittedCourseWork.finished = submittedCourseWork.finished;
        copySubmittedCourseWork.belongCourseWorkId = submittedCourseWork.belongCourseWorkId;
        copySubmittedCourseWork.submitterId = submittedCourseWork.submitterId;
        return copySubmittedCourseWork;
    }

    public void setSubmittedAnswers(String submittedAnswersStr, Map<Integer, QuestionType> questionTypeMap) {
        List<String> submittedAnswerList = ServiceUtils.string2List(submittedAnswersStr, Pattern.compile("\\{\\^\\^\\^}"));
        for (String answer : submittedAnswerList) {
            InnerSubmittedAnswer innerSubmittedAnswer = new InnerSubmittedAnswer(answer, questionTypeMap, belongCourseWorkId);
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

    public void putNewSubmittedAnswer(int questionIndex, SubmittedAnswer submittedAnswer) {
        submittedAnswers.put(questionIndex, new InnerSubmittedAnswer(questionIndex, submittedAnswer));
    }

    @Getter
    @Setter
    public static class InnerSubmittedAnswer {
        private int questionIndex;
        private SubmittedAnswer submittedAnswer;
        private int checkStatus;
        private QuestionType questionType;

        private InnerSubmittedAnswer(int questionIndex, SubmittedAnswer submittedAnswer) {
            this.questionIndex = questionIndex;
            this.submittedAnswer = submittedAnswer;
            this.checkStatus = -1;
            for (QuestionType type : QuestionType.values()) {
                if (type.getSubmittedAnswerType().isInstance(submittedAnswer)) {
                    this.questionType = type;
                    break;
                }
            }
        }

        private InnerSubmittedAnswer(String answer, Map<Integer, QuestionType> questionTypeMap, Long belongCourseWorkId) {
            setAnswer(answer, questionTypeMap, belongCourseWorkId);
        }

        private String getAnswer() {
            return questionIndex + "{###}" + submittedAnswer.getAnswer() + "{###}" + checkStatus;
        }

        private void setAnswer(String answer, Map<Integer, QuestionType> questionTypeMap, Long belongCourseWorkId) {
            if (StringUtils.isBlank(answer)) {
                throw new IllegalArgumentException("answer is null or blank!");
            }
            String[] tmp = answer.split("\\{###}");
            if (tmp.length != 3) {
                throw new IllegalArgumentException("answer's format invalid!");
            }
            this.questionIndex = Integer.parseInt(tmp[0]);
            this.checkStatus = Integer.parseInt(tmp[2]);
            try {
                QuestionType questionType = questionTypeMap.get(questionIndex);
                if (questionType == null) {
                    throw new IllegalArgumentException(ServiceUtils.format(
                            "questionType of question [{}] associated with courseWork [{}] not exist!",
                            questionIndex, belongCourseWorkId));
                }
                this.submittedAnswer = questionType.getSubmittedAnswerType()
                        .getConstructor(String.class).newInstance(tmp[1]);
                this.questionType = questionType;
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

}
