package com.corkili.learningserver.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corkili.learningserver.po.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
