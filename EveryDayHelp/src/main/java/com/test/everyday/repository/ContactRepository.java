package com.test.everyday.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.test.everyday.entity.Contacts;

public interface ContactRepository extends JpaRepository<Contacts, Integer>{

	void deleteByEmail(String email);
}
