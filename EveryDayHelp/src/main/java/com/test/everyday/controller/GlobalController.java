package com.test.everyday.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalController 
{
	@Value("${app.demo}")
	String webSiteTitle;
	
	@ModelAttribute
	public void setTitle(Model model)
	{
		model.addAttribute("title",webSiteTitle);
	}

}
