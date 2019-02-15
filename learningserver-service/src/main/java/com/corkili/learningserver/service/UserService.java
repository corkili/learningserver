package com.corkili.learningserver.service;

import java.util.Optional;

import com.corkili.learningserver.bo.User;
import com.corkili.learningserver.common.ServiceResult;

public interface UserService extends Service<User, com.corkili.learningserver.po.User> {

    Optional<User> retrieveUserByPhoneAndUserType(String phone, User.Type userType);

    ServiceResult register(String phone, String password, String username, User.Type userType);

    ServiceResult login(String phone, String password, User.Type userType);

}
