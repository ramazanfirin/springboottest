package com.mastertech.springboottest.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity  
public class UserRecord {

	@Id  
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotNull
    private String name;
	
	private String email;

	
	
	
	
	public UserRecord() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

	public UserRecord name(String name) {
        this.name = name;
        return this;
    }
	
	public UserRecord email(String email) {
        this.email = email;
        return this;
    }



	public UserRecord(Long id, String name, String email) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
