package com.test.everyday.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.test.everyday.entity.Contacts;
import com.test.everyday.service.PublicService;

@Controller
public class PublicController 
{
	
	@Value("${app.phone}")
	String phone;
	
	private final PublicService publicService;
	
	public PublicController(PublicService publicService) {
		super();
		this.publicService = publicService;
	}
	
//	@ModelAttribute
//	public void setTitle(Model model)
//	{
//		model.addAttribute("title","EveryDayHelp");
//	}
	
	@GetMapping("/")
	public String home(Model model)
	{
		//controller->View->Model
		//View->Controller->ModelAttribute
		//To create Global data access method
		
		model.addAttribute("msg","Easy Day Help Portal");
		model.addAttribute("phone",phone);
		return "public/home";//it is  view name means .html page name 
	}
	
	@GetMapping("/aboutUs")
	public String aboutUs()
	{
		return "public/aboutUs"; 
	}

	@GetMapping("/contactUs")
	public String contactUs()
	{
		return "public/contactUs"; 
	}
	
	@PostMapping("/contactUs")
	public String addContact(RedirectAttributes rda,Contacts contacts)
	{
		publicService.addContact(contacts);
		rda.addFlashAttribute("msg","Thank You for contacting us");
		//model.addAttribute("msg","Thank You");
		//return "public/contactUs";//forward always carry old request
		return "redirect:/contactUs";//in redirect browser always generates new request
	}
	
}
