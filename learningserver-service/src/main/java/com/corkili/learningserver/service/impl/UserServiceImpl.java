package com.corkili.learningserver.service.impl;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import com.corkili.learningserver.bo.User;
import com.corkili.learningserver.bo.User.Type;
import com.corkili.learningserver.common.SecurityUtils;
import com.corkili.learningserver.common.ServiceResult;
import com.corkili.learningserver.common.ServiceUtils;
import com.corkili.learningserver.repo.UserRepository;
import com.corkili.learningserver.service.UserService;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<User, com.corkili.learningserver.po.User> implements UserService {

    private static final String CACHE_NAME = "memoryCache";

    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<User> po2bo(com.corkili.learningserver.po.User userPO) {
        Optional<User> superOptional = super.po2bo(userPO);
        if (!superOptional.isPresent()) {
            return Optional.empty();
        }
        User user = superOptional.get();
        user.setRawPassword(false);
        if (userPO.getUserType() != null) {
            user.setUserType(Type.valueOf(userPO.getUserType().name()));
        }
        return Optional.of(user);
    }

    @Override
    public Optional<com.corkili.learningserver.po.User> bo2po(User user) {
        Optional<com.corkili.learningserver.po.User> superOptional = super.bo2po(user);
        if (!superOptional.isPresent()) {
            return Optional.empty();
        }
        com.corkili.learningserver.po.User userPO = superOptional.get();
        if (user.isRawPassword()) {
            try {
                userPO.setPassword(SecurityUtils.generateStrongPasswordHash(user.getPassword()));
            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                log.error("exception occurs when transfer user bo [{}] to user po, hashing password - {}",
                        user.getId() == null ? "" : userPO.getId(), ServiceUtils.stringifyError(e));
                return Optional.empty();
            }
        }
        if (user.getUserType() != null) {
            userPO.setUserType(com.corkili.learningserver.po.User.Type.valueOf(user.getUserType().name()));
        }
        return Optional.of(userPO);
    }

    @Override
    protected JpaRepository<com.corkili.learningserver.po.User, Long> repository() {
        return userRepository;
    }

    @Override
    protected String entityName() {
        return "user";
    }

    @Override
    protected User newBusinessObject() {
        return new User();
    }

    @Override
    protected com.corkili.learningserver.po.User newPersistObject() {
        return new com.corkili.learningserver.po.User();
    }

    @Override
    protected Logger logger() {
        return log;
    }

    @Override
    public Optional<User> retrieveUserByPhoneAndUserType(String phone, Type userType) {
        return Optional.ofNullable(retrieveUserByPhoneAndUserType(phone, userType, entityName()));
    }

    @Cacheable(cacheNames = CACHE_NAME, key = "#entityName + #result.id", unless = "#result == null or #result.id == null")
    public User retrieveUserByPhoneAndUserType(String phone, Type userType, String entityName) {
        if (StringUtils.isBlank(phone)) {
            logger().error("phone of {} is null", entityName);
            return null;
        }
        Optional<com.corkili.learningserver.po.User> userPOOptional = userRepository.findUserByPhoneAndUserType(
                phone, com.corkili.learningserver.po.User.Type.valueOf(userType.name()));
        if (!userPOOptional.isPresent()) {
            logger().error("{} with phone [{}] and userType [{}] not exist in db", entityName, phone, userType);
            return null;
        }
        return po2bo(userPOOptional.get()).orElse(null);
    }

    @Override
    public ServiceResult register(String phone, String password, String username, Type userType) {
        if (StringUtils.isBlank(phone)) {
            return recordErrorAndCreateFailResultWithMessage("register error: phone is empty");
        }
        if (StringUtils.isBlank(password)) {
            return recordErrorAndCreateFailResultWithMessage("register error: password is empty");
        }
        if (StringUtils.isBlank(username)) {
            return recordErrorAndCreateFailResultWithMessage("register error: username is empty");
        }
        if (userType == null) {
            return recordErrorAndCreateFailResultWithMessage("register error: userType is null");
        }
        if (phone.length() != 11) {
            return recordErrorAndCreateFailResultWithMessage("register error: length of phone is not 11");
        }
        if (userRepository.existsUserByPhoneAndUserType(phone, com.corkili.learningserver.po.User.Type.valueOf(userType.name()))) {
            return recordErrorAndCreateFailResultWithMessage(
                    "register error: phone [{}] with userType [{}] already exist", phone, userType);
        }
        User user = new User();
        user.setPhone(phone);
        user.setPassword(password);
        user.setRawPassword(true);
        user.setUsername(username);
        user.setUserType(userType);
        Optional<User> userOptional = create(user);
        if (!userOptional.isPresent()) {
            return recordErrorAndCreateFailResultWithMessage("register error: create user failed");
        }
        return ServiceResult.successResult("register success", User.class, user);
    }

    @Override
    public ServiceResult login(String phone, String password, Type userType) {
        if (StringUtils.isBlank(phone)) {
            return recordErrorAndCreateFailResultWithMessage("login error: phone is empty");
        }
        if (StringUtils.isBlank(password)) {
            return recordErrorAndCreateFailResultWithMessage("login error: password is empty");
        }
        if (userType == null) {
            return recordErrorAndCreateFailResultWithMessage("login error: userType is null");
        }
        if (phone.length() != 11) {
            return recordErrorAndCreateFailResultWithMessage("login error: length of phone is not 11");
        }
        Optional<User> userOptional = retrieveUserByPhoneAndUserType(phone, userType);
        if (!userOptional.isPresent()) {
            return recordErrorAndCreateFailResultWithMessage(
                    "login error: phone [{}] with userType [{}] already exist", phone, userType);
        }
        User user = userOptional.get();
        try {
            if (!SecurityUtils.validatePassword(password, user.getPassword())) {
                return recordErrorAndCreateFailResultWithMessage("login error: invalid password");
            }
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            return recordErrorAndCreateFailResultWithMessage("login error: invalid password");
        }
        return ServiceResult.successResult("login success", User.class, user);
    }
}
