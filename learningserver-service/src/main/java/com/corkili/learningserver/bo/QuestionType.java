package com.corkili.learningserver.bo;

import lombok.Getter;

import com.corkili.learningserver.bo.Question.AbstractAnswer;
import com.corkili.learningserver.bo.Question.EssayAnswer;
import com.corkili.learningserver.bo.Question.MultipleChoiceAnswer;
import com.corkili.learningserver.bo.Question.MultipleFillingAnswer;
import com.corkili.learningserver.bo.Question.SingleChoiceAnswer;
import com.corkili.learningserver.bo.Question.SingleFillingAnswer;
import com.corkili.learningserver.bo.SubmittedAnswer.AbstractSubmittedAnswer;
import com.corkili.learningserver.bo.SubmittedAnswer.EssaySubmittedAnswer;
import com.corkili.learningserver.bo.SubmittedAnswer.MultipleChoiceSubmittedAnswer;
import com.corkili.learningserver.bo.SubmittedAnswer.MultipleFillingSubmittedAnswer;
import com.corkili.learningserver.bo.SubmittedAnswer.SingleChoiceSubmittedAnswer;
import com.corkili.learningserver.bo.SubmittedAnswer.SingleFillingSubmittedAnswer;

@Getter
public enum  QuestionType {
    SingleFilling(SingleFillingAnswer.class, SingleFillingSubmittedAnswer.class), // auto check is true or false
    MultipleFilling(MultipleFillingAnswer.class, MultipleFillingSubmittedAnswer.class), // auto check is true or false
    SingleChoice(SingleChoiceAnswer.class, SingleChoiceSubmittedAnswer.class), // auto check must be true
    MultipleChoice(MultipleChoiceAnswer.class, MultipleChoiceSubmittedAnswer.class), // auto check must be true
    Essay(EssayAnswer.class, EssaySubmittedAnswer.class); // auto check must be false

    private Class<? extends AbstractAnswer> answerType;
    private Class<? extends AbstractSubmittedAnswer> submittedAnswerType;

    QuestionType(Class<? extends AbstractAnswer> answerType, Class<? extends AbstractSubmittedAnswer> submittedAnswerType) {
        this.answerType = answerType;
        this.submittedAnswerType = submittedAnswerType;
    }
}
