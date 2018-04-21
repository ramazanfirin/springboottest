package com.mastertech.springboottest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mastertech.springboottest.domain.UserRecord;  

public interface UserRepository extends JpaRepository <UserRecord, Long> {

}
