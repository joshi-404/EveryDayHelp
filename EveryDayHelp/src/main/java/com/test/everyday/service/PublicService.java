package com.test.everyday.service;

import org.springframework.stereotype.Service;

import com.test.everyday.entity.Contacts;
import com.test.everyday.repository.ContactRepository;

@Service
public class PublicService 
{
	
	private final ContactRepository contactRepository;

	public PublicService(ContactRepository contactRepository) {
		this.contactRepository = contactRepository;
	}

	public void addContact(Contacts contacts) {
		contactRepository.save(contacts);
		
	}


	
	

}
