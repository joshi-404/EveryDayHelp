package com.test.everyday.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_details")
public class UserDetails 
{
	
	@Id
	private String email;
	@Column
	private String password;
	@Column
	private String name;
	@Column
	private String phone;
	@Column
	private String address;
	@Column
	private String pic;
	
	public UserDetails() {
		
	}
	
	public UserDetails(String email, String password, String name, String phone, String address, String pic) {
		super();
		this.email = email;
		this.password = password;
		this.name = name;
		this.phone = phone;
		this.address = address;
		this.pic = pic;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

}

