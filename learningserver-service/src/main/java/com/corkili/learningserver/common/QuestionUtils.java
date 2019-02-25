package com.corkili.learningserver.common;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import lombok.extern.slf4j.Slf4j;

import com.corkili.learningserver.bo.ExamQuestion;
import com.corkili.learningserver.bo.Question;
import com.corkili.learningserver.bo.Question.MultipleChoiceAnswer;
import com.corkili.learningserver.bo.Question.MultipleFillingAnswer;
import com.corkili.learningserver.bo.Question.SingleChoiceAnswer;
import com.corkili.learningserver.bo.Question.SingleFillingAnswer;
import com.corkili.learningserver.bo.QuestionType;
import com.corkili.learningserver.bo.SubmittedAnswer;
import com.corkili.learningserver.bo.SubmittedAnswer.MultipleChoiceSubmittedAnswer;
import com.corkili.learningserver.bo.SubmittedAnswer.MultipleFillingSubmittedAnswer;
import com.corkili.learningserver.bo.SubmittedAnswer.MultipleFillingSubmittedAnswer.Pair;
import com.corkili.learningserver.bo.SubmittedAnswer.SingleChoiceSubmittedAnswer;
import com.corkili.learningserver.bo.SubmittedAnswer.SingleFillingSubmittedAnswer;
import com.corkili.learningserver.bo.SubmittedCourseWork;
import com.corkili.learningserver.bo.SubmittedCourseWork.InnerSubmittedAnswer;
import com.corkili.learningserver.bo.SubmittedExam;
import com.corkili.learningserver.bo.WorkQuestion;

@Slf4j
public class QuestionUtils {

    public static void checkCourseWork(Map<Long, Question> questionMap, Map<Integer, WorkQuestion> workQuestionMap,
                                       SubmittedCourseWork submittedCourseWork) {
        if (questionMap == null) {
            log.error("questionMap is null");
            return;
        }
        if (workQuestionMap == null) {
            log.error("workQuestionMap is null");
            return;
        }
        if (submittedCourseWork == null) {
            log.error("submittedCourseWork is null");
            return;
        }
        for (SubmittedCourseWork.InnerSubmittedAnswer innerSubmittedAnswer : submittedCourseWork.getSubmittedAnswers().values()) {
            WorkQuestion workQuestion = workQuestionMap.get(innerSubmittedAnswer.getQuestionIndex());
            if (workQuestion == null) {
                log.warn("workQuestion of index [{}] not exists", innerSubmittedAnswer.getQuestionIndex());
                continue;
            }
            Question question = questionMap.get(workQuestion.getQuestionId());
            if (question == null) {
                log.warn("question [{}] of workQuestion [{}] not exists",
                        workQuestion.getQuestionId(), innerSubmittedAnswer.getQuestionIndex());
                continue;
            }
            double result = checkQuestionAnswer(question, innerSubmittedAnswer.getSubmittedAnswer(), null);
            innerSubmittedAnswer.setCheckStatus((int) result);
        }
        rollupAnswerCheckResult(submittedCourseWork);
    }

    public static void checkExam(Map<Long, Question> questionMap, Map<Integer, ExamQuestion> examQuestionMap,
                                 SubmittedExam submittedExam) {
        if (questionMap == null) {
            log.error("questionMap is null");
            return;
        }
        if (examQuestionMap == null) {
            log.error("examQuestionMap is null");
            return;
        }
        if (submittedExam == null) {
            log.error("submittedExam is null");
            return;
        }
        for (SubmittedExam.InnerSubmittedAnswer innerSubmittedAnswer : submittedExam.getSubmittedAnswers().values()) {
            ExamQuestion examQuestion = examQuestionMap.get(innerSubmittedAnswer.getQuestionIndex());
            if (examQuestion == null) {
                log.warn("examQuestion of index [{}] not exists", innerSubmittedAnswer.getQuestionIndex());
                continue;
            }
            Question question = questionMap.get(examQuestion.getQuestionId());
            if (question == null) {
                log.warn("question [{}] of workQuestion [{}] not exists",
                        examQuestion.getQuestionId(), innerSubmittedAnswer.getQuestionIndex());
                continue;
            }
            double result = checkQuestionAnswer(question, innerSubmittedAnswer.getSubmittedAnswer(), examQuestion.getScoreMap());
            innerSubmittedAnswer.setScore(result);
        }
        rollupAnswerCheckResult(submittedExam);
    }

    public static void rollupAnswerCheckResult(SubmittedCourseWork submittedCourseWork) {
        boolean alreadyCheckAllAnswer = true;
        for (InnerSubmittedAnswer innerSubmittedAnswer : submittedCourseWork.getSubmittedAnswers().values()) {
            if (innerSubmittedAnswer.getCheckStatus() < 0) {
                alreadyCheckAllAnswer = false;
                break;
            }
        }
        submittedCourseWork.setAlreadyCheckAllAnswer(alreadyCheckAllAnswer);
    }

    public static void rollupAnswerCheckResult(SubmittedExam submittedExam) {
        boolean alreadyCheckAllAnswer = true;
        double totalScore = 0.0;
        for (SubmittedExam.InnerSubmittedAnswer innerSubmittedAnswer : submittedExam.getSubmittedAnswers().values()) {
            if (innerSubmittedAnswer.getScore() < 0) {
                alreadyCheckAllAnswer = false;
            } else {
                totalScore += innerSubmittedAnswer.getScore();
            }
        }
        submittedExam.setAlreadyCheckAllAnswer(alreadyCheckAllAnswer);
        submittedExam.setTotalScore(totalScore);
    }

    private static double checkQuestionAnswer(Question question, SubmittedAnswer submittedAnswer, Map<Integer, Double> scoreMap) {
        if (question == null || submittedAnswer == null) {
            log.error("question or submittedAnswer is null");
            return -1;
        }
        QuestionType questionType = question.getQuestionType();
        double result = -1;
        switch (questionType) {
            case SingleFilling:
                result = checkQuestionAnswer(question, (SingleFillingSubmittedAnswer) submittedAnswer, scoreMap);
                break;
            case MultipleFilling:
                result = checkQuestionAnswer(question, (MultipleFillingSubmittedAnswer) submittedAnswer, scoreMap);
                break;
            case SingleChoice:
                result = checkQuestionAnswer(question, (SingleChoiceSubmittedAnswer) submittedAnswer, scoreMap);
                break;
            case MultipleChoice:
                result = checkQuestionAnswer(question, (MultipleChoiceSubmittedAnswer) submittedAnswer, scoreMap);
                break;
        }
        return result;
    }

    /**
     * -1: not check
     *
     * scoreMap == null:
     *  0: false
     *  1: true
     *
     * scoreMap != null:
     *  score
     */
    private static double checkQuestionAnswer(Question question, SingleFillingSubmittedAnswer submittedAnswer, Map<Integer, Double> scoreMap) {
        if (!question.isAutoCheck()) {
            return -1;
        }
        if (question.getAnswer() == null) {
            log.error("answer of question [{}] is null", question.getId());
            return -1;
        }
        if (question.getAnswer() instanceof SingleFillingAnswer) {
            SingleFillingAnswer answer = (SingleFillingAnswer) question.getAnswer();
            boolean checkResult = false;
            for (String ans : answer.getAnswerList()) {
                if (StringUtils.equals(ans, submittedAnswer.getAnswer())) {
                    checkResult = true;
                    break;
                }
            }
            if (scoreMap == null) {
                return checkResult ? 1 : 0;
            } else {
                return checkResult ? scoreMap.getOrDefault(-1, 0d) : 0;
            }
        } else {
            return -1;
        }
    }

    /**
     * -1: not check
     *
     * scoreMap == null:
     *   0: false
     *   1: true
     * scoreMap != null:
     *   return totalScore(>= 0)
     */
    private static double checkQuestionAnswer(Question question, MultipleFillingSubmittedAnswer submittedAnswer, Map<Integer, Double> scoreMap) {
        if (!question.isAutoCheck()) {
            return -1;
        }
        if (question.getAnswer() == null) {
            log.error("answer of question [{}] is null", question.getId());
            return -1;
        }
        if (question.getAnswer() instanceof MultipleFillingAnswer) {
            MultipleFillingAnswer answer = (MultipleFillingAnswer) question.getAnswer();
            double totalScore = 0;
            boolean checkStatus = true;
            for (Entry<Integer, SingleFillingAnswer> entry : answer.getAnswerMap().entrySet()) {
                boolean checkResult = false;
                Pair pair = submittedAnswer.getAnswerMap().get(entry.getKey());
                for (String ans : entry.getValue().getAnswerList()) {
                    if (StringUtils.equals(ans, pair.getAnswer())) {
                        checkResult = true;
                        break;
                    }
                }
                double scoreOrCheckStatus;
                if (scoreMap == null) {
                    scoreOrCheckStatus = checkResult ? 1 : 0;
                    checkStatus &= checkResult;
                } else {
                    scoreOrCheckStatus = checkResult ? scoreMap.getOrDefault(pair.getIndex(), 0d) : 0;
                    totalScore += scoreOrCheckStatus;
                }
                pair.setScoreOrCheckStatus(scoreOrCheckStatus);
            }
            if (scoreMap == null) {
                return checkStatus ? 1 : 0;
            } else {
                return totalScore;
            }
        }
        return -1;
    }

    /**
     * -1: not check
     *
     * scoreMap == null:
     *  0: false
     *  1: true
     *
     * scoreMap != null:
     *  score
     */
    private static double checkQuestionAnswer(Question question, SingleChoiceSubmittedAnswer submittedAnswer, Map<Integer, Double> scoreMap) {
        if (!question.isAutoCheck()) {
            return -1;
        }
        if (question.getAnswer() == null) {
            log.error("answer of question [{}] is null", question.getId());
            return -1;
        }
        if (question.getAnswer() instanceof SingleChoiceAnswer) {
            SingleChoiceAnswer answer = (SingleChoiceAnswer) question.getAnswer();
            if (scoreMap == null) {
                return answer.getChoice() == submittedAnswer.getChoice() ? 1 : 0;
            } else {
                return answer.getChoice() == submittedAnswer.getChoice() ? scoreMap.getOrDefault(-1, 0d) : 0;
            }
        } else {
            return -1;
        }
    }

    /**
     * -1: not check
     *
     * scoreMap == null:
     *  0: false
     *  1: half_true
     *  2: true
     *
     * scoreMap != null:
     *  score
     */
    private static double checkQuestionAnswer(Question question, MultipleChoiceSubmittedAnswer submittedAnswer, Map<Integer, Double> scoreMap) {
        if (!question.isAutoCheck()) {
            return -1;
        }
        if (question.getAnswer() == null) {
            log.error("answer of question [{}] is null", question.getId());
            return -1;
        }
        if (question.getAnswer() instanceof MultipleChoiceAnswer) {
            MultipleChoiceAnswer answer = (MultipleChoiceAnswer) question.getAnswer();
            int count = 0;
            for (Integer choice : submittedAnswer.getChoices()) {
                if (answer.getChoices().contains(choice)) {
                    count++;
                }
            }
            if (answer.isSelectAllIsCorrect()) {
                if (scoreMap == null) {
                    return count == answer.getChoices().size() ? 2 : 0;
                } else {
                    return count == answer.getChoices().size() ? scoreMap.getOrDefault(-1, 0d) : 0;
                }
            } else {
                if (scoreMap == null) {
                    return count == 0 ? 0 : (count == answer.getChoices().size() ? 2 : 1);
                } else {
                    double score = scoreMap.getOrDefault(-1, 0d);
                    return count == 0 ? 0 : (count == answer.getChoices().size() ? score : score / 2);
                }
            }
        } else {
            return -1;
        }
    }

}
