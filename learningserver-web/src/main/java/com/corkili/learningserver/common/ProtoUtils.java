package com.corkili.learningserver.common;

import com.corkili.learningserver.bo.Course;
import com.corkili.learningserver.bo.CourseCatalog;
import com.corkili.learningserver.bo.CourseCatalog.CourseCatalogItem;
import com.corkili.learningserver.bo.CourseComment;
import com.corkili.learningserver.bo.CourseSubscription;
import com.corkili.learningserver.bo.CourseWork;
import com.corkili.learningserver.bo.Exam;
import com.corkili.learningserver.bo.ExamQuestion;
import com.corkili.learningserver.bo.ForumTopic;
import com.corkili.learningserver.bo.Message;
import com.corkili.learningserver.bo.Question;
import com.corkili.learningserver.bo.Scorm;
import com.corkili.learningserver.bo.SubmittedCourseWork;
import com.corkili.learningserver.bo.SubmittedExam;
import com.corkili.learningserver.bo.TopicComment;
import com.corkili.learningserver.bo.TopicReply;
import com.corkili.learningserver.bo.User;
import com.corkili.learningserver.bo.WorkQuestion;
import com.corkili.learningserver.generate.protobuf.Info.Answer;
import com.corkili.learningserver.generate.protobuf.Info.CourseCatalogInfo;
import com.corkili.learningserver.generate.protobuf.Info.CourseCatalogItemInfo;
import com.corkili.learningserver.generate.protobuf.Info.CourseCatalogItemInfoList;
import com.corkili.learningserver.generate.protobuf.Info.CourseCommentInfo;
import com.corkili.learningserver.generate.protobuf.Info.CourseCommentType;
import com.corkili.learningserver.generate.protobuf.Info.CourseInfo;
import com.corkili.learningserver.generate.protobuf.Info.CourseSubscriptionInfo;
import com.corkili.learningserver.generate.protobuf.Info.CourseWorkInfo;
import com.corkili.learningserver.generate.protobuf.Info.CourseWorkQuestionInfo;
import com.corkili.learningserver.generate.protobuf.Info.CourseWorkSimpleInfo;
import com.corkili.learningserver.generate.protobuf.Info.CourseWorkSubmittedAnswer;
import com.corkili.learningserver.generate.protobuf.Info.DeliveryContentInfo;
import com.corkili.learningserver.generate.protobuf.Info.EssayAnswer;
import com.corkili.learningserver.generate.protobuf.Info.EssaySubmittedAnswer;
import com.corkili.learningserver.generate.protobuf.Info.ExamInfo;
import com.corkili.learningserver.generate.protobuf.Info.ExamQuestionInfo;
import com.corkili.learningserver.generate.protobuf.Info.ExamSimpleInfo;
import com.corkili.learningserver.generate.protobuf.Info.ExamSubmittedAnswer;
import com.corkili.learningserver.generate.protobuf.Info.ForumTopicInfo;
import com.corkili.learningserver.generate.protobuf.Info.Image;
import com.corkili.learningserver.generate.protobuf.Info.MessageInfo;
import com.corkili.learningserver.generate.protobuf.Info.MultipleChoiceAnswer;
import com.corkili.learningserver.generate.protobuf.Info.MultipleChoiceSubmittedAnswer;
import com.corkili.learningserver.generate.protobuf.Info.MultipleFillingAnswer;
import com.corkili.learningserver.generate.protobuf.Info.MultipleFillingSubmittedAnswer;
import com.corkili.learningserver.generate.protobuf.Info.MultipleFillingSubmittedAnswer.Pair;
import com.corkili.learningserver.generate.protobuf.Info.QuestionInfo;
import com.corkili.learningserver.generate.protobuf.Info.QuestionSimpleInfo;
import com.corkili.learningserver.generate.protobuf.Info.QuestionType;
import com.corkili.learningserver.generate.protobuf.Info.Score;
import com.corkili.learningserver.generate.protobuf.Info.Score.MultipleScore;
import com.corkili.learningserver.generate.protobuf.Info.ScormInfo;
import com.corkili.learningserver.generate.protobuf.Info.SingleChoiceAnswer;
import com.corkili.learningserver.generate.protobuf.Info.SingleChoiceSubmittedAnswer;
import com.corkili.learningserver.generate.protobuf.Info.SingleFillingAnswer;
import com.corkili.learningserver.generate.protobuf.Info.SingleFillingSubmittedAnswer;
import com.corkili.learningserver.generate.protobuf.Info.SubmittedAnswer;
import com.corkili.learningserver.generate.protobuf.Info.SubmittedCourseWorkInfo;
import com.corkili.learningserver.generate.protobuf.Info.SubmittedCourseWorkSimpleInfo;
import com.corkili.learningserver.generate.protobuf.Info.SubmittedExamInfo;
import com.corkili.learningserver.generate.protobuf.Info.SubmittedExamSimpleInfo;
import com.corkili.learningserver.generate.protobuf.Info.TopicCommentInfo;
import com.corkili.learningserver.generate.protobuf.Info.TopicReplyInfo;
import com.corkili.learningserver.generate.protobuf.Info.UserInfo;
import com.corkili.learningserver.generate.protobuf.Info.UserType;
import com.corkili.learningserver.scorm.cam.model.DeliveryContent;
import com.google.protobuf.ByteString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@Slf4j
public class ProtoUtils {

    public static DeliveryContentInfo generateDeliveryContentInfo(DeliveryContent deliveryContent) {
        if (deliveryContent == null) {
            return DeliveryContentInfo.getDefaultInstance();
        }
        return DeliveryContentInfo.newBuilder()
                .setItemId(deliveryContent.getItemId())
                .setBasePath(deliveryContent.getBasePath())
                .setEntry(deliveryContent.getEntry())
                .setHasParameters(StringUtils.isNotBlank(deliveryContent.getParameters()))
                .setParameters(deliveryContent.getParameters() == null ? "" : deliveryContent.getParameters())
                .addAllContent(deliveryContent.getContent().getPhysicalFilePathList())
                .build();
    }

    public static CourseCatalogInfo generateCourseCatalogInfo(CourseCatalog courseCatalog) {
        if (courseCatalog == null) {
            return CourseCatalogInfo.getDefaultInstance();
        }

        CourseCatalogInfo.Builder courseCatalogInfo = CourseCatalogInfo.newBuilder();
        courseCatalogInfo.setDefaultItemId(courseCatalog.getDefaultItemId());
        courseCatalogInfo.setMaxLevel(courseCatalog.getMaxLevel());
        courseCatalogInfo.setItemNumber(courseCatalog.getItemNumber());

        if (courseCatalog.getMaxLevel() > 0) {
            Map<Integer, List<CourseCatalogItemInfo.Builder>> itemMap = new HashMap<>();
            for (Entry<Integer, List<CourseCatalogItem>> entry : courseCatalog.getItems().entrySet()) {
                List<CourseCatalogItemInfo.Builder> itemList = new ArrayList<>();
                for (CourseCatalogItem courseCatalogItem : entry.getValue()) {
                    itemList.add(generateCourseCatalogItemInfo(courseCatalogItem));
                }
                itemMap.put(entry.getKey(), itemList);
            }
            for (Entry<Integer, List<CourseCatalogItem>> entry : courseCatalog.getItems().entrySet()) {
                List<CourseCatalogItem> items = entry.getValue();
                List<CourseCatalogItemInfo.Builder> infos = itemMap.getOrDefault(entry.getKey(), new ArrayList<>());
                List<CourseCatalogItemInfo.Builder> lastLevelList = itemMap.getOrDefault(entry.getKey() - 1, new ArrayList<>());
                List<CourseCatalogItemInfo.Builder> nextLevelList = itemMap.getOrDefault(entry.getKey() + 1, new ArrayList<>());
                for (CourseCatalogItem item : items) {
                    CourseCatalogItemInfo.Builder info = null;
                    for (CourseCatalogItemInfo.Builder builder : infos) {
                        if (builder.getLevel() == item.getLevel() && builder.getIndex() == item.getIndex()) {
                            info = builder;
                            break;
                        }
                    }
                    if (info == null) {
                        log.warn("generate course catalog info error-1");
                        continue;
                    }
                    // parent
                    info.setHasParent(item.getParentItem() != null);
                    if (item.getParentItem() != null) {
                        CourseCatalogItemInfo.Builder parent = null;
                        for (CourseCatalogItemInfo.Builder builder : lastLevelList) {
                            if (builder.getLevel() == item.getParentItem().getLevel()
                                    && builder.getIndex() == item.getParentItem().getIndex()) {
                                parent = builder;
                                break;
                            }
                        }
                        if (parent == null) {
                            log.warn("generate course catalog info error-2");
                        } else {
                            info.setParentItem(parent);
                        }
                    }
                    // child
                    CourseCatalogItemInfoList.Builder nextLevelItems = CourseCatalogItemInfoList.newBuilder();
                    for (CourseCatalogItem nextLevelItem : item.getNextLevelItems()) {
                        CourseCatalogItemInfo.Builder child = null;
                        for (CourseCatalogItemInfo.Builder builder : nextLevelList) {
                            if (builder.getLevel() == nextLevelItem.getLevel()
                                    && builder.getIndex() == nextLevelItem.getIndex()) {
                                child = builder;
                                break;
                            }
                        }
                        if (child == null) {
                            log.warn("generate course catalog info error-3");
                        } else {
                            nextLevelItems.addCourseCatalogItemInfo(child);
                        }
                    }
                    info.setNextLevelItems(nextLevelItems);
                }
            }
            for (Entry<Integer, List<CourseCatalogItemInfo.Builder>> entry : itemMap.entrySet()) {
                CourseCatalogItemInfoList.Builder itemList = CourseCatalogItemInfoList.newBuilder();
                for (CourseCatalogItemInfo.Builder builder : entry.getValue()) {
                    itemList.addCourseCatalogItemInfo(builder);
                }
                courseCatalogInfo.putItems(entry.getKey(), itemList.build());
            }
        }

        return courseCatalogInfo.build();
    }

    private static CourseCatalogItemInfo.Builder generateCourseCatalogItemInfo(CourseCatalogItem courseCatalogItem) {
        if (courseCatalogItem == null) {
            return null;
        }
        return CourseCatalogItemInfo.newBuilder()
                .setLevel(courseCatalogItem.getLevel())
                .setIndex(courseCatalogItem.getIndex())
                .setItemId(courseCatalogItem.getItemId())
                .setItemTitle(courseCatalogItem.getItemTitle())
                .setSelectable(courseCatalogItem.isSelectable())
                .setVisible(courseCatalogItem.isVisible())
                .addAllHideLmsUI(courseCatalogItem.getHideLmsUI())
                .setHasCompletionThreshold(courseCatalogItem.getCompletionThreshold() != null)
                .setCompletionThreshold(courseCatalogItem.getCompletionThreshold() != null ? courseCatalogItem.getCompletionThreshold() : 0.0)
                .setProgressMeasure(courseCatalogItem.getProgressMeasure())
                .setCompletionStatus(courseCatalogItem.getCompletionStatus())
                .setLeaf(courseCatalogItem.isLeaf());
    }

    public static ScormInfo generateScormInfo(Scorm scorm, boolean loadZipData) {
        if (scorm == null) {
            return ScormInfo.getDefaultInstance();
        }
        byte[] data = new byte[0];
        if (loadZipData) {
            data = ScormZipUtils.readScormZip(scorm.getPath());
        }
        return ScormInfo.newBuilder()
                .setScormId(scorm.getId())
                .setCreateTime(getTime(scorm.getCreateTime()))
                .setUpdateTime(getTime(scorm.getUpdateTime()))
                .setData(ByteString.copyFrom(data))
                .build();
    }

    public static CourseSubscriptionInfo generateCourseSubscriptionInfo(CourseSubscription courseSubscription,
                                                                        User subscriber, Course course, User courseTeacher,
                                                                        boolean loadImageData) {
        if (courseSubscription == null) {
            return CourseSubscriptionInfo.getDefaultInstance();
        }
        return CourseSubscriptionInfo.newBuilder()
                .setCourseSubscriptionId(courseSubscription.getId())
                .setCreateTime(getTime(courseSubscription.getCreateTime()))
                .setUpdateTime(getTime(courseSubscription.getUpdateTime()))
                .setSubscriberId(courseSubscription.getId())
                .setSubscriberInfo(generateUserInfo(subscriber))
                .setSubscribedCourseId(courseSubscription.getSubscribedCourseId())
                .setSubscribedCourseInfo(generateCourseInfo(course, courseTeacher, loadImageData))
                .build();
    }

    public static MessageInfo generateMessageInfo(Message message, User receiver, User sender, boolean loadImageData) {
        if (message == null) {
            return MessageInfo.getDefaultInstance();
        }
        MessageInfo.Builder builder = MessageInfo.newBuilder()
                .setMessageId(message.getId())
                .setCreateTime(getTime(message.getCreateTime()))
                .setUpdateTime(getTime(message.getUpdateTime()))
                .setReceiverId(receiver.getId())
                .setReceiverInfo(generateUserInfo(receiver))
                .setSenderId(sender.getId())
                .setSenderInfo(generateUserInfo(sender));
        if (message.isImage()) {
            builder.setImage(generateImageList(Collections.singletonList(message.getContent()), loadImageData).get(0));
        } else {
            builder.setText(message.getContent());
        }
        return builder.build();
    }

    public static CourseCommentInfo generateCourseCommentInfo(CourseComment courseComment, User author, boolean loadImageData) {
        if (courseComment == null) {
            return CourseCommentInfo.getDefaultInstance();
        }
        return CourseCommentInfo.newBuilder()
                .setCourseCommentId(courseComment.getId())
                .setCreateTime(getTime(courseComment.getCreateTime()))
                .setUpdateTime(getTime(courseComment.getUpdateTime()))
                .setCommentType(CourseCommentType.valueOf(courseComment.getCommentType().name()))
                .setContent(courseComment.getContent())
                .addAllImage(generateImageList(courseComment.getImagePaths(), loadImageData))
                .setCommentAuthorId(courseComment.getCommentAuthorId())
                .setCommentAuthorInfo(generateUserInfo(author))
                .setCommentedCourseId(courseComment.getCommentedCourseId())
                .build();
    }
    
    public static TopicReplyInfo generateTopicReplyInfo(TopicReply topicReply, User author, boolean loadImageData) {
        if (topicReply == null) {
            return TopicReplyInfo.getDefaultInstance();
        }
        return TopicReplyInfo.newBuilder()
                .setTopicReplyId(topicReply.getId())
                .setCreateTime(getTime(topicReply.getCreateTime()))
                .setUpdateTime(getTime(topicReply.getUpdateTime()))
                .setContent(topicReply.getContent())
                .addAllImage(generateImageList(topicReply.getImagePaths(), loadImageData))
                .setAuthorId(topicReply.getAuthorId())
                .setAuthorInfo(generateUserInfo(author))
                .setBelongCommentId(topicReply.getBelongCommentId())
                .build();
    }
    
    public static TopicCommentInfo generateTopicCommentInfo(TopicComment topicComment, User author, boolean loadImageData) {
        if (topicComment == null) {
            return TopicCommentInfo.getDefaultInstance();
        }
        return TopicCommentInfo.newBuilder()
                .setTopicCommentId(topicComment.getId())
                .setCreateTime(getTime(topicComment.getCreateTime()))
                .setUpdateTime(getTime(topicComment.getUpdateTime()))
                .setContent(topicComment.getContent())
                .addAllImage(generateImageList(topicComment.getImagePaths(), loadImageData))
                .setAuthorId(topicComment.getAuthorId())
                .setAuthorInfo(generateUserInfo(author))
                .setBelongTopicId(topicComment.getBelongTopicId())
                .build();
    }
    
    public static ForumTopicInfo generateForumTopicInfo(ForumTopic forumTopic, User author, boolean loadImageData) {
        if (forumTopic == null) {
            return ForumTopicInfo.getDefaultInstance();
        }
        return ForumTopicInfo.newBuilder()
                .setForumTopicId(forumTopic.getId())
                .setCreateTime(getTime(forumTopic.getCreateTime()))
                .setUpdateTime(getTime(forumTopic.getUpdateTime()))
                .setTitle(forumTopic.getTitle())
                .setDescription(forumTopic.getDescription())
                .addAllImage(generateImageList(forumTopic.getImagePaths(), loadImageData))
                .setAuthorId(forumTopic.getAuthorId())
                .setAuthorInfo(generateUserInfo(author))
                .setBelongCourseId(forumTopic.getBelongCourseId())
                .build();
    }

    public static SubmittedExamSimpleInfo generateSubmittedExamSimpleInfo(
            SubmittedExam submittedExam, User submitter) {
        if (submittedExam == null) {
            return SubmittedExamSimpleInfo.getDefaultInstance();
        }
        return SubmittedExamSimpleInfo.newBuilder()
                .setSubmittedExamId(submittedExam.getId())
                .setCreateTime(getTime(submittedExam.getCreateTime()))
                .setUpdateTime(getTime(submittedExam.getUpdateTime()))
                .setAlreadyCheckAllAnswer(submittedExam.isAlreadyCheckAllAnswer())
                .setTotalScore(submittedExam.getTotalScore())
                .setFinished(submittedExam.isFinished())
                .setBelongExamId(submittedExam.getBelongExamId())
                .setSubmitterId(submittedExam.getSubmitterId())
                .setSubmitterInfo(generateUserInfo(submitter))
                .build();
    }

    public static SubmittedExamInfo generateSubmittedExamInfo(SubmittedExam submittedExam, User submitter) {
        if (submittedExam == null) {
            return SubmittedExamInfo.getDefaultInstance();
        }
        Map<Integer, ExamSubmittedAnswer> submittedAnswerMap = new HashMap<>();
        submittedExam.getSubmittedAnswers().forEach((index, ans) -> {
            submittedAnswerMap.put(index, ExamSubmittedAnswer.newBuilder()
                    .setQuestionIndex(ans.getQuestionIndex())
                    .setSubmittedAnswer(generateSubmittedAnswer(ans.getSubmittedAnswer()))
                    .setScore(ans.getScore())
                    .build());
        });
        return SubmittedExamInfo.newBuilder()
                .setSubmittedExamId(submittedExam.getId())
                .setCreateTime(getTime(submittedExam.getCreateTime()))
                .setUpdateTime(getTime(submittedExam.getUpdateTime()))
                .putAllSubmittedAnswer(submittedAnswerMap)
                .setAlreadyCheckAllAnswer(submittedExam.isAlreadyCheckAllAnswer())
                .setTotalScore(submittedExam.getTotalScore())
                .setFinished(submittedExam.isFinished())
                .setBelongExamId(submittedExam.getBelongExamId())
                .setSubmitterId(submittedExam.getSubmitterId())
                .setSubmitterInfo(generateUserInfo(submitter))
                .build();
    }
    
    public static SubmittedCourseWorkSimpleInfo generateSubmittedCourseWorkSimpleInfo(
            SubmittedCourseWork submittedCourseWork, User submitter) {
        if (submittedCourseWork == null) {
            return SubmittedCourseWorkSimpleInfo.getDefaultInstance();
        }
        return SubmittedCourseWorkSimpleInfo.newBuilder()
                .setSubmittedCourseWorkId(submittedCourseWork.getId())
                .setCreateTime(getTime(submittedCourseWork.getCreateTime()))
                .setUpdateTime(getTime(submittedCourseWork.getUpdateTime()))
                .setAlreadyCheckAllAnswer(submittedCourseWork.isAlreadyCheckAllAnswer())
                .setFinished(submittedCourseWork.isFinished())
                .setBelongCourseWorkId(submittedCourseWork.getBelongCourseWorkId())
                .setSubmitterId(submittedCourseWork.getSubmitterId())
                .setSubmitterInfo(generateUserInfo(submitter))
                .build();
    }

    public static SubmittedCourseWorkInfo generateSubmittedCourseWorkInfo(SubmittedCourseWork submittedCourseWork, User submitter) {
        if (submittedCourseWork == null) {
            return SubmittedCourseWorkInfo.getDefaultInstance();
        }
        Map<Integer, CourseWorkSubmittedAnswer> submittedAnswerMap = new HashMap<>();
        submittedCourseWork.getSubmittedAnswers().forEach((index, ans) -> {
            submittedAnswerMap.put(index, CourseWorkSubmittedAnswer.newBuilder()
                    .setQuestionIndex(ans.getQuestionIndex())
                    .setSubmittedAnswer(generateSubmittedAnswer(ans.getSubmittedAnswer()))
                    .setCheckStatus(ans.getCheckStatus())
                    .build());
        });
        return SubmittedCourseWorkInfo.newBuilder()
                .setSubmittedCourseWorkId(submittedCourseWork.getId())
                .setCreateTime(getTime(submittedCourseWork.getCreateTime()))
                .setUpdateTime(getTime(submittedCourseWork.getUpdateTime()))
                .putAllSubmittedAnswer(submittedAnswerMap)
                .setAlreadyCheckAllAnswer(submittedCourseWork.isAlreadyCheckAllAnswer())
                .setFinished(submittedCourseWork.isFinished())
                .setBelongCourseWorkId(submittedCourseWork.getBelongCourseWorkId())
                .setSubmitterId(submittedCourseWork.getSubmitterId())
                .setSubmitterInfo(generateUserInfo(submitter))
                .build();
    }

    public static com.corkili.learningserver.bo.SubmittedAnswer generateSubmittedAnswer(SubmittedAnswer submittedAnswer) {
        com.corkili.learningserver.bo.SubmittedAnswer result = null;
        if (submittedAnswer.hasSingleFillingSubmittedAnswer()) {
            SingleFillingSubmittedAnswer raw = submittedAnswer.getSingleFillingSubmittedAnswer();
            result = new com.corkili.learningserver.bo.SubmittedAnswer.SingleFillingSubmittedAnswer(raw.getAnswer());
        } else if (submittedAnswer.hasMultipleFillingSubmittedAnswer()) {
            MultipleFillingSubmittedAnswer raw = submittedAnswer.getMultipleFillingSubmittedAnswer();
            com.corkili.learningserver.bo.SubmittedAnswer.MultipleFillingSubmittedAnswer res =
                    new com.corkili.learningserver.bo.SubmittedAnswer.MultipleFillingSubmittedAnswer();
            for (Entry<Integer, Pair> entry : raw.getAnswerMap().entrySet()) {
                Pair pair = entry.getValue();
                res.getAnswerMap().put(entry.getKey(),
                        new com.corkili.learningserver.bo.SubmittedAnswer.MultipleFillingSubmittedAnswer.Pair(
                                pair.getIndex(), pair.getAnswer(), pair.getScoreOrCheckStatus()));
            }
            result = res;
        } else if (submittedAnswer.hasSingleChoiceSubmittedAnswer()) {
            SingleChoiceSubmittedAnswer raw = submittedAnswer.getSingleChoiceSubmittedAnswer();
            result = new com.corkili.learningserver.bo.SubmittedAnswer.SingleChoiceSubmittedAnswer(
                    String.valueOf(raw.getChoice()));
        } else if (submittedAnswer.hasMultipleChoiceSubmittedAnswer()) {
            MultipleChoiceSubmittedAnswer raw = submittedAnswer.getMultipleChoiceSubmittedAnswer();
            com.corkili.learningserver.bo.SubmittedAnswer.MultipleChoiceSubmittedAnswer res =
                    new com.corkili.learningserver.bo.SubmittedAnswer.MultipleChoiceSubmittedAnswer();
            res.getChoices().addAll(raw.getChoiceList());
            result = res;
        } else if (submittedAnswer.hasEssaySubmittedAnswer()) {
            EssaySubmittedAnswer raw = submittedAnswer.getEssaySubmittedAnswer();
            com.corkili.learningserver.bo.SubmittedAnswer.EssaySubmittedAnswer res
                    = new com.corkili.learningserver.bo.SubmittedAnswer.EssaySubmittedAnswer();
            res.setText(raw.getText());
            result = res;
        }
        return result;
    }

    public static SubmittedAnswer generateSubmittedAnswer(com.corkili.learningserver.bo.SubmittedAnswer submittedAnswer) {
        SubmittedAnswer.Builder builder = SubmittedAnswer.newBuilder();
        if (submittedAnswer instanceof com.corkili.learningserver.bo.SubmittedAnswer.SingleFillingSubmittedAnswer) {
            com.corkili.learningserver.bo.SubmittedAnswer.SingleFillingSubmittedAnswer raw =
                    (com.corkili.learningserver.bo.SubmittedAnswer.SingleFillingSubmittedAnswer) submittedAnswer;
            builder.setSingleFillingSubmittedAnswer(SingleFillingSubmittedAnswer.newBuilder()
                    .setAnswer(raw.getAnswer())
                    .build());
        } else if (submittedAnswer instanceof com.corkili.learningserver.bo.SubmittedAnswer.MultipleFillingSubmittedAnswer) {
            com.corkili.learningserver.bo.SubmittedAnswer.MultipleFillingSubmittedAnswer raw =
                    (com.corkili.learningserver.bo.SubmittedAnswer.MultipleFillingSubmittedAnswer) submittedAnswer;
            MultipleFillingSubmittedAnswer.Builder rb = MultipleFillingSubmittedAnswer.newBuilder();
            for (Entry<Integer, com.corkili.learningserver.bo.SubmittedAnswer.MultipleFillingSubmittedAnswer.Pair> entry
                    : raw.getAnswerMap().entrySet()) {
                Pair.Builder pb = Pair.newBuilder();
                pb.setIndex(entry.getValue().getIndex());
                pb.setAnswer(entry.getValue().getAnswer());
                pb.setScoreOrCheckStatus(entry.getValue().getScoreOrCheckStatus());
                rb.putAnswer(entry.getKey(), pb.build());
            }
            builder.setMultipleFillingSubmittedAnswer(rb.build());
        } else if (submittedAnswer instanceof com.corkili.learningserver.bo.SubmittedAnswer.SingleChoiceSubmittedAnswer) {
            com.corkili.learningserver.bo.SubmittedAnswer.SingleChoiceSubmittedAnswer raw =
                    (com.corkili.learningserver.bo.SubmittedAnswer.SingleChoiceSubmittedAnswer) submittedAnswer;
            builder.setSingleChoiceSubmittedAnswer(SingleChoiceSubmittedAnswer.newBuilder()
                    .setChoice(raw.getChoice())
                    .build());
        } else if (submittedAnswer instanceof com.corkili.learningserver.bo.SubmittedAnswer.MultipleChoiceSubmittedAnswer) {
            com.corkili.learningserver.bo.SubmittedAnswer.MultipleChoiceSubmittedAnswer raw =
                    (com.corkili.learningserver.bo.SubmittedAnswer.MultipleChoiceSubmittedAnswer) submittedAnswer;
            builder.setMultipleChoiceSubmittedAnswer(MultipleChoiceSubmittedAnswer.newBuilder()
                    .addAllChoice(raw.getChoices())
                    .build());
        } else if (submittedAnswer instanceof com.corkili.learningserver.bo.SubmittedAnswer.EssaySubmittedAnswer) {
            com.corkili.learningserver.bo.SubmittedAnswer.EssaySubmittedAnswer raw =
                    (com.corkili.learningserver.bo.SubmittedAnswer.EssaySubmittedAnswer) submittedAnswer;
            builder.setEssaySubmittedAnswer(EssaySubmittedAnswer.newBuilder()
                    .setText(raw.getText())
                    .build());
        }
        return builder.build();
    }

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

    public static ExamSimpleInfo generateExamSimpleInfo(Exam exam) {
        if (exam == null) {
            return ExamSimpleInfo.getDefaultInstance();
        }
        return ExamSimpleInfo.newBuilder()
                .setExamId(exam.getId())
                .setExamName(exam.getExamName())
                .setBelongCourseId(exam.getBelongCourseId())
                .setStartTime(getTime(exam.getStartTime()))
                .setEndTime(getTime(exam.getEndTime()))
                .build();
    }

    public static CourseWorkSimpleInfo generateCourseWorkSimpleInfo(CourseWork courseWork) {
        if (courseWork == null) {
            return CourseWorkSimpleInfo.getDefaultInstance();
        }
        return CourseWorkSimpleInfo.newBuilder()
                .setCourseWorkId(courseWork.getId())
                .setOpen(courseWork.isOpen())
                .setCourseWorkName(courseWork.getWorkName())
                .setBelongCourseId(courseWork.getBelongCourseId())
                .setHasDeadline(courseWork.getDeadline() != null)
                .setDeadline(getTime(courseWork.getDeadline()))
                .build();
    }

    public static ExamInfo generateExamInfo(Exam exam, List<ExamQuestion> examQuestionList) {
        if (exam == null) {
            return ExamInfo.getDefaultInstance();
        }
        List<ExamQuestionInfo> examQuestionInfos = new LinkedList<>();
        if (examQuestionList != null) {
            for (ExamQuestion examQuestion : examQuestionList) {
                examQuestionInfos.add(generateExamQuestionInfo(examQuestion));
            }
        }
        return ExamInfo.newBuilder()
                .setExamId(exam.getId())
                .setCreateTime(getTime(exam.getCreateTime()))
                .setUpdateTime(getTime(exam.getUpdateTime()))
                .setExamName(exam.getExamName())
                .setBelongCourseId(exam.getBelongCourseId())
                .setStartTime(getTime(exam.getStartTime()))
                .setEndTime(getTime(exam.getEndTime()))
                .setDuration(exam.getDuration())
                .addAllExamQuestionInfo(examQuestionInfos)
                .build();
    }

    public static ExamQuestionInfo generateExamQuestionInfo(ExamQuestion examQuestion) {
        if (examQuestion == null) {
            return ExamQuestionInfo.getDefaultInstance();
        }
        Score score;
        if (examQuestion.getScore() != null) {
            score = Score.newBuilder()
                    .setSingleScore(examQuestion.getScore())
                    .build();
        } else {
            score = Score.newBuilder()
                    .setMultipleScore(MultipleScore.newBuilder()
                            .putAllScore(examQuestion.getScoreMap())
                            .build())
                    .build();
        }
        return ExamQuestionInfo.newBuilder()
                .setExamQuestionId(examQuestion.getId())
                .setCreateTime(getTime(examQuestion.getCreateTime()))
                .setUpdateTime(getTime(examQuestion.getUpdateTime()))
                .setIndex(examQuestion.getIndex())
                .setBelongExamId(examQuestion.getBelongExamId())
                .setQuestionId(examQuestion.getQuestionId())
                .setScore(score)
                .build();
    }

    public static CourseWorkInfo generateCourseWorkInfo(CourseWork courseWork, List<WorkQuestion> workQuestionList) {
        if (courseWork == null) {
            return CourseWorkInfo.getDefaultInstance();
        }
        List<CourseWorkQuestionInfo> courseWorkQuestionInfos = new LinkedList<>();
        if (workQuestionList != null) {
            for (WorkQuestion workQuestion : workQuestionList) {
                courseWorkQuestionInfos.add(generateCourseWorkQuestionInfo(workQuestion));
            }
        }
        return CourseWorkInfo.newBuilder()
                .setCourseWorkId(courseWork.getId())
                .setCreateTime(getTime(courseWork.getCreateTime()))
                .setUpdateTime(getTime(courseWork.getUpdateTime()))
                .setOpen(courseWork.isOpen())
                .setCourseWorkName(courseWork.getWorkName())
                .setBelongCourseId(courseWork.getBelongCourseId())
                .setHasDeadline(courseWork.getDeadline() != null)
                .setDeadline(getTime(courseWork.getDeadline()))
                .addAllCourseWorkQuestionInfo(courseWorkQuestionInfos)
                .build();
    }
    
    public static CourseWorkQuestionInfo generateCourseWorkQuestionInfo(WorkQuestion workQuestion) {
        if (workQuestion == null) {
            return CourseWorkQuestionInfo.getDefaultInstance();
        }
        return CourseWorkQuestionInfo.newBuilder()
                .setCourseWorkQuestionId(workQuestion.getId())
                .setCreateTime(getTime(workQuestion.getCreateTime()))
                .setUpdateTime(getTime(workQuestion.getUpdateTime()))
                .setIndex(workQuestion.getIndex())
                .setBelongCourseWorkId(workQuestion.getBelongCourseWorkId())
                .setQuestionId(workQuestion.getQuestionId())
                .build();
    }

    public static CourseInfo generateCourseInfo(Course course, User teacher, boolean loadImageData) {
        if (course == null) {
            return CourseInfo.getDefaultInstance();
        }
        List<Image> imageList = generateImageList(course.getImagePaths(), loadImageData);
        return CourseInfo.newBuilder()
                .setCourseId(course.getId())
                .setCreateTime(getTime(course.getCreateTime()))
                .setUpdateTime(getTime(course.getUpdateTime()))
                .setOpen(course.isOpen())
                .setCourseName(course.getCourseName())
                .setDescription(course.getDescription())
                .addAllImage(imageList)
                .addAllTag(course.getTags())
                .setTeacherInfo(generateUserInfo(teacher))
                .setHasCourseware(course.getCoursewareId() != null)
                .setCoursewareId(course.getCoursewareId() != null ? course.getCoursewareId() : 0)
                .build();
    }

    public static UserInfo generateUserInfo(User user) {
        if (user == null) {
            return UserInfo.getDefaultInstance();
        }
        return UserInfo.newBuilder()
                .setUserId(user.getId())
                .setPhone(user.getPhone())
                .setUsername(user.getUsername())
                .setUserType(UserType.valueOf(user.getUserType().name()))
                .build();
    }

    public static QuestionInfo generateQuestionInfo(Question question, boolean loadImageData) {
        if (question == null) {
            return QuestionInfo.getDefaultInstance();
        }
        return QuestionInfo.newBuilder()
                .setQuestionId(question.getId())
                .setCreateTime(getTime(question.getCreateTime()))
                .setUpdateTime(getTime(question.getUpdateTime()))
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
        if (question == null) {
            return QuestionSimpleInfo.getDefaultInstance();
        }
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
                    return questionAnswer;
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
            return Answer.getDefaultInstance();
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
//                            .addAllImage(generateImageList(ans.getImagePaths(), loadImageData))
                            .build();
                    return Answer.newBuilder()
                            .setEssayAnswer(essayAnswer)
                            .build();
                }
                break;
            }
        }
        return Answer.getDefaultInstance();
    }
    
    private static long getTime(Date time) {
        if (time != null) {
            return time.getTime();
        } else {
            return 0;
        }
    }

}
