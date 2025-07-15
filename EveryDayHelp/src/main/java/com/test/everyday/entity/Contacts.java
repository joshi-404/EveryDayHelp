package com.test.everyday.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "contacts")
public class Contacts 
{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer contact_id;
	@Column
	private String name;
	@Column
	private String email;
	@Column
	private String phone;
	@Column
	private String question;
	
	
	public Contacts(String name, String email, String phone, String question) {
		super();
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.question = question;
	}
	
	
	public Contacts() {
		
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
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public Integer getContact_id() {
		return contact_id;
	}

}
