package com.mastertech.springboottest.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mastertech.springboottest.domain.UserRecord;
import com.mastertech.springboottest.repository.UserRepository;

@Service
public class UserService {

	@Autowired  
    private UserRepository userRepository;
	
	 public List<UserRecord> getAllUsers(){  
	        List<UserRecord>userRecords = new ArrayList<>();  
	        userRepository.findAll().forEach(userRecords::add);  
	        return userRecords;  
	    }  
	    public UserRecord getUser(Long id){  
	        //return userRepository.findOne(id); 
	        return null;
	    }  
	    public void addUser(UserRecord userRecord){  
	        userRepository.save(userRecord);  
	    }  
	    public void delete(Long id){  
	        userRepository.deleteById(id);  
	    }  
}
