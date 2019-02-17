package com.corkili.learningserver.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corkili.learningserver.po.User;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsUserByPhoneAndUserType(String phone, User.Type userType);

    Optional<User> findUserByPhoneAndUserType(String phone, User.Type userType);

}
