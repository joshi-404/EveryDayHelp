package com.test.everyday.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.test.everyday.entity.Booking;
import com.test.everyday.entity.Feedback;
import com.test.everyday.entity.UserDetails;
import com.test.everyday.repository.BookingRepository;
import com.test.everyday.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController 
{
	 @Autowired
	    private BookingRepository bookingRepository;
	
	private final UserService userService;

	public UserController(UserService userService) {
		super();
		this.userService = userService;
	}
	@GetMapping("/UserRegistration")
	public String UserRegistration()
	{
		return "user/UserRegistration";
	}
	
	@PostMapping("/registerUser")
	public String registerUser(RedirectAttributes rda,UserDetails userDetails,@RequestParam("profilepic")MultipartFile file)
	{
		
		// We use @RequestParam to take file upload. In (), we write the value of name attribute that we set in html. Then variable pic(which can be
				// anything not just pic) of type MultipartFile is set
				
		// When we take data from form, model attribute works internally, here we didn't use it cause there was no need
		
		userService.registerUser(userDetails,file);
		rda.addFlashAttribute("msg","Thank You for registering");
		return "redirect:/UserRegistration";
	}

	@GetMapping("/user_login")
	public String user_login (){
		return"user/user_login";
	}
	
	@PostMapping("user_login")
	public String  userLogin(@RequestParam String email,@RequestParam("password") String pass,Model model,HttpSession session,RedirectAttributes rd)
	//we write column name in ("") only if parameter name and html variable name is different.
	{
		UserDetails user=userService.userLogin(email,pass);
		if(user != null)
		{
			model.addAttribute("userInfo",user);//forward -> only on admin_home for single request
			session.setAttribute("userKey", user);//to track for multiple request
			return "user/user_home";//html page name(view name)
		}
		rd.addFlashAttribute("mas","Invalid email/password");
		return "redirect:/user/user_login";//redirect takes endpoint
	}
	
	@GetMapping("userHome")
	public String adminHome(HttpSession session,Model model)
	{
		UserDetails user=(UserDetails)session.getAttribute("userKey");
		model.addAttribute("userInfo",user);
		return "user/user_home";
	}
	
	@GetMapping("userLogout")
	public String userLogout(HttpSession session,RedirectAttributes rd)
	{
		session.removeAttribute("userKey");
		session.invalidate();
		rd.addFlashAttribute("msg","Successfully Logged Out");
		return "redirect:/user_login";
	}
	
	 @GetMapping("booking")
	public String showBookingForm(Model model) {
        model.addAttribute("booking", new Booking());
        return "user/booking";
    }

	 @PostMapping("booking")
	    public String submitBooking(@ModelAttribute("booking") Booking booking) {
	        userService.saveBooking(booking);
	        return "redirect:/booking";
	    }
	 
	 @GetMapping("my-bookings")
	 public String userBookings(Model model) {
	     List<Booking> bookings = bookingRepository.findAll(); // Filter by user if login session exists
	     model.addAttribute("bookings", bookings);
	     return "user/user_bookings";
	 }
	 
	 @GetMapping("userFeedback")
		public String userFeedback(HttpSession session, Model model, RedirectAttributes rda)
		{
			 UserDetails user = (UserDetails)session.getAttribute("userKey");
			 if(user!=null)
			 {
				 model.addAttribute("userInfo", user);
				 return "user/user_feedback";
			 }
			 else
			 {
				 rda.addFlashAttribute("msg", "Unauthorized access, Please login first");
				 return "redirect:/user_login";			 }
		}
		
		@PostMapping("userFeedback")
		public String addFeedback(@ModelAttribute Feedback feedback, RedirectAttributes rda)
		{
			userService.addFeedback(feedback);
			rda.addFlashAttribute("msg", "Thank you for your feedback");
			
			return "redirect:/userHome";
		}
}
