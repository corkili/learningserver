package com.corkili.learningserver.bo;

import com.corkili.learningserver.common.ServiceUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

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
     *   use {%%%} divide answerIndex, answerContent and score(negative if not checked) in each filling answer, if questionType is MultipleFilling
     *
     *   use {&&&} divide choice in submitted answer, if questionType is MultipleChoice
     *
     *   when type is essay, a string represent the correct answer. if contains image(s),
     *   use {##image##} divide text and imagePaths, then use {!!!} divide each image path in imagePaths
     */
    private Map<Integer, InnerSubmittedAnswer> submittedAnswers;

    private boolean alreadyCheckAllAnswer;

    private double totalScore;

    private boolean finished;

    private Long belongExamId;

    private Long submitterId;
    
    public static SubmittedExam copyFrom(SubmittedExam submittedExam) {
        SubmittedExam copySubmittedExam = new SubmittedExam();
        copySubmittedExam.id = submittedExam.id;
        copySubmittedExam.createTime = submittedExam.createTime;
        copySubmittedExam.updateTime = submittedExam.updateTime;
        Map<Integer, QuestionType> questionTypeMap = new HashMap<>();
        submittedExam.submittedAnswers.forEach((k, v) -> questionTypeMap.put(k, v.getQuestionType()));
        copySubmittedExam.setSubmittedAnswers(submittedExam.getSubmittedAnswersStr(), questionTypeMap);
        copySubmittedExam.alreadyCheckAllAnswer = submittedExam.alreadyCheckAllAnswer;
        copySubmittedExam.totalScore = submittedExam.totalScore;
        copySubmittedExam.finished = submittedExam.finished;
        copySubmittedExam.belongExamId = submittedExam.belongExamId;
        copySubmittedExam.submitterId = submittedExam.submitterId;
        return copySubmittedExam;
    }

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

    public void putNewSubmittedAnswer(int questionIndex, SubmittedAnswer submittedAnswer) {
        submittedAnswers.put(questionIndex, new InnerSubmittedAnswer(questionIndex, submittedAnswer));
    }

    @Getter
    @Setter
    public static class InnerSubmittedAnswer {
        private int questionIndex;
        private SubmittedAnswer submittedAnswer;
        private double score;
        private QuestionType questionType;

        private InnerSubmittedAnswer(int questionIndex, SubmittedAnswer submittedAnswer) {
            this.questionIndex = questionIndex;
            this.submittedAnswer = submittedAnswer;
            this.score = -1;
            for (QuestionType type : QuestionType.values()) {
                if (type.getSubmittedAnswerType().isInstance(submittedAnswer)) {
                    this.questionType = type;
                    break;
                }
            }
        }

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
            this.score = Double.parseDouble(tmp[2]);
            try {
                QuestionType questionType = questionTypeMap.get(questionIndex);
                if (questionType == null) {
                    throw new IllegalArgumentException(ServiceUtils.format(
                            "questionType of question [{}] associated with exam [{}] not exist!",
                            questionIndex, belongExamId));
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
