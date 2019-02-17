package com.corkili.learningserver;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.corkili.learningserver.po.Course;
import com.corkili.learningserver.po.Message;
import com.corkili.learningserver.po.User;
import com.corkili.learningserver.po.User.Type;
import com.corkili.learningserver.repo.CourseCommentRepository;
import com.corkili.learningserver.repo.CourseRepository;
import com.corkili.learningserver.repo.MessageRepository;
import com.corkili.learningserver.repo.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LearningServerApplication.class)
public class POTests {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseCommentRepository courseCommentRepository;

    @Test
    public void testCreateUser() {
        User sender = new User();
        sender.setPhone("sender-phone-1");
        sender.setUsername("sender-username-1");
        sender.setPassword("sender-password-1");
        sender.setUserType(Type.Teacher);
        User receiver = new User();
        receiver.setPhone("receiver-phone-1");
        receiver.setUsername("receiver-username-1");
        receiver.setPassword("receiver-password-1");
        receiver.setUserType(Type.Student);
        System.out.println("before save:");
        System.out.println("receiver" + receiver);
        System.out.println("sender" + sender);
        receiver = userRepository.saveAndFlush(receiver);
        sender = userRepository.saveAndFlush(sender);
        System.out.println("after save:");
        System.out.println("receiver" + userRepository.findById(receiver.getId()).get());
        System.out.println("sender" + userRepository.findById(sender.getId()).get());
    }

    @Test
    public void testCreateMessage() {
        Message message = new Message();
        User receiver = userRepository.findById(1L).get();
        User sender = userRepository.findById(2L).get();
        message.setContent("message-1");
        message.setReceiver(receiver);
        message.setSender(sender);
        System.out.println("before save:");
        System.out.println("message" + message);
        message = messageRepository.saveAndFlush(message);
        System.out.println("after save:");
        System.out.println("message" + messageRepository.findById(message.getId()).get());
    }

    @Test
    public void testCreateMessage2() {
        Message message = new Message();
        message.setContent("message-10");
        message.setReceiver(new User());
        message.setSender(new User());
        System.out.println("before save:");
        System.out.println("message" + message);
        message = messageRepository.saveAndFlush(message);
        System.out.println("after save:");
        System.out.println("message" + messageRepository.findById(message.getId()).get());
    }

    @Test
    public void testCreateMessage3() {
        Message message = new Message();
        message.setContent("message-30");
        message.setReceiver(null);
        message.setSender(null);
        System.out.println("before save:");
        System.out.println("message" + message);
        message = messageRepository.saveAndFlush(message);
        System.out.println("after save:");
        System.out.println("message" + messageRepository.findById(message.getId()).get());
    }

    @Test
    public void testUpdateMessage() {
        Message message = messageRepository.findById(1L).get();
        System.out.println("before update:");
        System.out.println("message" + message);
        message.getReceiver().setUsername("receiver-username-2");
        message.getSender().setUsername("sender-username-2");
        messageRepository.saveAndFlush(message);
        System.out.println("after update:");
        System.out.println("message" + messageRepository.findById(message.getId()).get());
    }

    @Test
    public void testUpdateMessage2() {
        Message message = messageRepository.findById(1L).get();
        System.out.println("before update:");
        System.out.println("message" + message);
        message.setUpdateTime(new Date(System.currentTimeMillis()));
        message.setContent("message-2");
        messageRepository.saveAndFlush(message);
        System.out.println("after update:");
        message = messageRepository.findById(message.getId()).get();
        System.out.println("message" + message);
    }

    @Test
    public void testUpdateMessage3() {
        Message message = messageRepository.findById(1L).get();
        System.out.println("before update:");
        System.out.println("message" + message);
        message.setUpdateTime(new Date(System.currentTimeMillis()));
        message.setContent("message-3");
        User receiver = new User();
        User sender = new User();
        receiver.setId(1L);
        sender.setId(2L);
        message.setReceiver(receiver);
        message.setSender(sender);
        messageRepository.saveAndFlush(message);
        System.out.println("after update:");
        System.out.println("message" + messageRepository.findById(message.getId()).get());
    }

    @Test
    public void testUpdateMessage4() {
        Message message = messageRepository.findById(1L).get();
        System.out.println("before update:");
        System.out.println("message" + message);
        message.setUpdateTime(new Date(System.currentTimeMillis()));
        message.setContent("message-4");
        message.setReceiver(null);
        message.setSender(null);
        messageRepository.saveAndFlush(message);
        System.out.println("after update:");
        System.out.println("message" + messageRepository.findById(message.getId()).get());
    }

    @Test
    public void testUpdateMessage5() {
        Message message = messageRepository.findById(1L).get();
        System.out.println("before update:");
        System.out.println("message" + message);
        message.setUpdateTime(new Date(System.currentTimeMillis()));
        message.setContent("message-5");
        message.setReceiver(new User());
        message.setSender(new User());
        messageRepository.saveAndFlush(message);
        System.out.println("after update:");
        System.out.println("message" + messageRepository.findById(message.getId()).get());
    }

    @Test
    public void testQueryCourseCommentId() {
        List<Long> idList = courseCommentRepository.findAllCourseCommentIdByCommentedCourseId(1L);
        System.out.println(idList);
    }

    @Test
    public void testQueryCourse() {
        List<Course> courseList = courseRepository.findAllByTeacherUsernameContaining("sender");
        System.out.println(courseList);
    }

}
