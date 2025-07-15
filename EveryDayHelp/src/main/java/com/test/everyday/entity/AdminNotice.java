package com.test.everyday.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class AdminNotice 
{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int notice_id;
	
	@Column
	private String title;
	
	@Column
	private String Description;

//	public AdminNotice(String title, String description) {
//		super();
//		this.title = title;
//		Description = description;
//	}

}
