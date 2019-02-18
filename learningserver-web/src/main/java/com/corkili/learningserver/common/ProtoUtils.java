package com.corkili.learningserver.common;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.protobuf.ByteString;

import com.corkili.learningserver.bo.Course;
import com.corkili.learningserver.bo.Question;
import com.corkili.learningserver.bo.User;
import com.corkili.learningserver.generate.protobuf.Info.Answer;
import com.corkili.learningserver.generate.protobuf.Info.CourseInfo;
import com.corkili.learningserver.generate.protobuf.Info.EssayAnswer;
import com.corkili.learningserver.generate.protobuf.Info.Image;
import com.corkili.learningserver.generate.protobuf.Info.MultipleChoiceAnswer;
import com.corkili.learningserver.generate.protobuf.Info.MultipleFillingAnswer;
import com.corkili.learningserver.generate.protobuf.Info.QuestionInfo;
import com.corkili.learningserver.generate.protobuf.Info.QuestionSimpleInfo;
import com.corkili.learningserver.generate.protobuf.Info.QuestionType;
import com.corkili.learningserver.generate.protobuf.Info.SingleChoiceAnswer;
import com.corkili.learningserver.generate.protobuf.Info.SingleFillingAnswer;
import com.corkili.learningserver.generate.protobuf.Info.UserInfo;
import com.corkili.learningserver.generate.protobuf.Info.UserType;

public class ProtoUtils {

    public static List<Image> generateImageList(List<String> imagePaths, boolean loadImageData) {
        List<Image> imageList = new LinkedList<>();
        if (imagePaths == null) {
            return imageList;
        }
        for (String imagePath : imagePaths) {
            if (loadImageData) {
                imageList.add(Image.newBuilder()
                        .setPath(imagePath)
                        .setHasData(true)
                        .setImage(ByteString.copyFrom(ImageUtils.readImage(imagePath)))
                        .build());
            } else {
                imageList.add(Image.newBuilder()
                        .setPath(imagePath)
                        .setHasData(false)
                        .build());
            }
        }
        return imageList;
    }

    public static CourseInfo generateCourseInfo(Course course, User teacher, boolean loadImageData) {
        List<Image> imageList = generateImageList(course.getImagePaths(), loadImageData);
        return CourseInfo.newBuilder()
                .setCourseId(course.getId())
                .setCreateTime(course.getCreateTime().getTime())
                .setUpdateTime(course.getUpdateTime().getTime())
                .setOpen(course.isOpen())
                .setCourseName(course.getCourseName())
                .setDescription(course.getDescription())
                .addAllImage(imageList)
                .addAllTag(course.getTags())
                .setTeacherInfo(generateUserInfo(teacher))
                .build();
    }

    public static UserInfo generateUserInfo(User user) {
        return UserInfo.newBuilder()
                .setPhone(user.getPhone())
                .setUsername(user.getUsername())
                .setUserType(UserType.valueOf(user.getUserType().name()))
                .build();
    }

    public static QuestionInfo generateQuestionInfo(Question question, boolean loadImageData) {
        return QuestionInfo.newBuilder()
                .setQuestionId(question.getId())
                .setCreateTime(question.getCreateTime().getTime())
                .setUpdateTime(question.getUpdateTime().getTime())
                .setQuestion(question.getQuestion())
                .addAllImage(generateImageList(question.getImagePaths(), loadImageData))
                .setQuestionType(QuestionType.valueOf(question.getQuestionType().name()))
                .setAutoCheck(question.isAutoCheck())
                .putAllChoices(question.getChoices())
                .setAnswer(generateAnswer(question.getQuestionType(), question.getAnswer(), loadImageData))
                .setAuthorId(question.getAuthorId())
                .build();
    }

    public static QuestionSimpleInfo generateQuestionSimpleInfo(Question question) {
        return QuestionSimpleInfo.newBuilder()
                .setQuestionId(question.getId())
                .setQuestion(question.getQuestion())
                .setQuestionType(QuestionType.valueOf(question.getQuestionType().name()))
                .setAutoCheck(question.isAutoCheck())
                .setAuthorId(question.getAuthorId())
                .build();
    }

    /**
     * if questionType is Essay, transfer answer.getEssayAnswer().getImageList() to Map
     */
    public static Question.Answer generateQuestionAnswer(QuestionType questionType, Answer answer) {
        if (questionType == null || answer == null) {
            return null;
        }
        switch (questionType) {
            case SingleFilling: {
                if (answer.hasSingleFillingAnswer()) {
                    Question.SingleFillingAnswer questionAnswer = new Question.SingleFillingAnswer();
                    for (String ans : answer.getSingleFillingAnswer().getAnswerList()) {
                        questionAnswer.getAnswerList().add(ans);
                    }
                    return questionAnswer;
                }
                break;
            }
            case MultipleFilling: {
                if (answer.hasMultipleFillingAnswer()) {
                    Question.MultipleFillingAnswer questionAnswer = new Question.MultipleFillingAnswer();
                    for (Entry<Integer, SingleFillingAnswer> entry : answer.getMultipleFillingAnswer().getAnswerMap().entrySet()) {
                        Question.SingleFillingAnswer singleFillingAnswer = new Question.SingleFillingAnswer();
                        for (String ans : entry.getValue().getAnswerList()) {
                            singleFillingAnswer.getAnswerList().add(ans);
                        }
                        questionAnswer.getAnswerMap().put(entry.getKey(), singleFillingAnswer);
                    }
                    return questionAnswer;
                }
                break;
            }
            case SingleChoice: {
                if (answer.hasSingleChoiceAnswer()) {
                    return new Question.SingleChoiceAnswer(answer.getSingleChoiceAnswer().getChoice());
                }
                break;
            }
            case MultipleChoice: {
                if (answer.hasMultipleChoiceAnswer()) {
                    Question.MultipleChoiceAnswer questionAnswer = new Question.MultipleChoiceAnswer(
                            answer.getMultipleChoiceAnswer().getSelectAllIsCorrect());
                    answer.getMultipleChoiceAnswer().getChoiceList().forEach(questionAnswer.getChoices()::add);
                }
                break;
            }
            case Essay: {
                if (answer.hasEssayAnswer()) {
                    Question.EssayAnswer questionAnswer = new Question.EssayAnswer();
                    questionAnswer.setText(answer.getEssayAnswer().getText());
                    return questionAnswer;
                }
                break;
            }
        }
        return null;
    }

    public static Answer generateAnswer(com.corkili.learningserver.bo.QuestionType questionType,
                                        Question.Answer questionAnswer, boolean loadImageData) {
        if (questionType == null || questionAnswer == null) {
            return Answer.newBuilder().build();
        }
        switch (questionType) {
            case SingleFilling: {
                if (questionAnswer instanceof Question.SingleFillingAnswer) {
                    SingleFillingAnswer singleFillingAnswer = SingleFillingAnswer.newBuilder()
                            .addAllAnswer(((Question.SingleFillingAnswer) questionAnswer).getAnswerList())
                            .build();
                    return Answer.newBuilder()
                            .setSingleFillingAnswer(singleFillingAnswer)
                            .build();
                }
                break;
            }
            case MultipleFilling: {
                if (questionAnswer instanceof Question.MultipleFillingAnswer) {
                    Map<Integer, SingleFillingAnswer> allAnswer = new HashMap<>();
                    for (Entry<Integer, Question.SingleFillingAnswer> entry : ((Question.MultipleFillingAnswer) questionAnswer).getAnswerMap().entrySet()) {
                        SingleFillingAnswer singleFillingAnswer = SingleFillingAnswer.newBuilder()
                                .addAllAnswer(entry.getValue().getAnswerList())
                                .build();
                        allAnswer.put(entry.getKey(), singleFillingAnswer);
                    }
                    MultipleFillingAnswer multipleFillingAnswer = MultipleFillingAnswer.newBuilder()
                            .putAllAnswer(allAnswer)
                            .build();
                    return Answer.newBuilder()
                            .setMultipleFillingAnswer(multipleFillingAnswer)
                            .build();
                }
                break;
            }
            case SingleChoice: {
                if (questionAnswer instanceof Question.SingleChoiceAnswer) {
                    SingleChoiceAnswer singleChoiceAnswer = SingleChoiceAnswer.newBuilder()
                            .setChoice(((Question.SingleChoiceAnswer) questionAnswer).getChoice())
                            .build();
                    return Answer.newBuilder()
                            .setSingleChoiceAnswer(singleChoiceAnswer)
                            .build();
                }
                break;
            }
            case MultipleChoice: {
                if (questionAnswer instanceof Question.MultipleChoiceAnswer) {
                    Question.MultipleChoiceAnswer ans = (Question.MultipleChoiceAnswer) questionAnswer;
                    MultipleChoiceAnswer multipleChoiceAnswer = MultipleChoiceAnswer.newBuilder()
                            .setSelectAllIsCorrect(ans.isSelectAllIsCorrect())
                            .addAllChoice(ans.getChoices())
                            .build();
                    return Answer.newBuilder()
                            .setMultipleChoiceAnswer(multipleChoiceAnswer)
                            .build();
                }
                break;
            }
            case Essay: {
                if (questionAnswer instanceof Question.EssayAnswer) {
                    Question.EssayAnswer ans = (Question.EssayAnswer) questionAnswer;
                    EssayAnswer essayAnswer = EssayAnswer.newBuilder()
                            .setText(ans.getText())
                            .addAllImage(generateImageList(ans.getImagePaths(), loadImageData))
                            .build();
                    return Answer.newBuilder()
                            .setEssayAnswer(essayAnswer)
                            .build();
                }
                break;
            }
        }
        return Answer.newBuilder().build();
    }
}
