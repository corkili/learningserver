package com.corkili.learningserver.service.impl;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import com.corkili.learningserver.bo.User;
import com.corkili.learningserver.bo.User.Type;
import com.corkili.learningserver.common.SecurityUtils;
import com.corkili.learningserver.repo.UserRepository;
import com.corkili.learningserver.service.UserService;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<User, com.corkili.learningserver.po.User> implements UserService {

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
                user.setPassword(SecurityUtils.generateStrongPasswordHash(user.getPassword()));
            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                log.error("exception occurs when transfer user bo [{}] to user po", user.getId() == null ? "" : userPO.getId());
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
}
