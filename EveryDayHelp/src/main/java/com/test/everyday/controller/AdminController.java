package com.test.everyday.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.test.everyday.entity.Admin;
import com.test.everyday.entity.AdminNotice;
import com.test.everyday.entity.Booking;
import com.test.everyday.entity.Contacts;
import com.test.everyday.entity.Employee;
import com.test.everyday.entity.UserDetails;
import com.test.everyday.repository.BookingRepository;
import com.test.everyday.service.AdminService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("admin")
@RequiredArgsConstructor
public class AdminController 
{
	 
	private final AdminService adminService;
	
//	@NonNull
//	private String name;

//	public AdminController(AdminService adminService) {
//		super();
//		this.adminService = adminService;
//	}
	
	@GetMapping("allUsers")
	public String allUsers(Model model) {
		
		List<UserDetails> userList = adminService.allUsers();
		
		model.addAttribute("users", userList);
		
		return "admin/all_users";
	}

	
	
	@GetMapping("editUser")
	public String editUser(@RequestParam String email,Model model)
	{
		UserDetails user=adminService.editUser(email);
		model.addAttribute("user",user);//"user" is used in the th:object
		return "admin/edit_user";
	}
	
	@PostMapping("editUser")
	public String editUserFinal(@ModelAttribute UserDetails modifiedUser,Model model)
	{
		adminService.editUserFinal(modifiedUser);
		return "redirect:/admin/allUsers";
	}
	
	@GetMapping("allEmployees")
	public String allEmployees(Model model) {
		
		List<Employee> employeeList = adminService.allEmployees();
		
		model.addAttribute("employees", employeeList);
		
		return "admin/all_employees";
	}
	
	@GetMapping("allContacts")
	public String allContacts(Model model) {
		
		List<Contacts> contactList = adminService.allContacts();
		
		model.addAttribute("contacts", contactList);
		
		return "admin/all_contacts";
	}
	
	@GetMapping("/admin_login")
	public String admin_login (){
		return"admin/admin_login";
	}
	
	@GetMapping("/addNotice")
	public String addNotice(Model model)
	{
		AdminNotice notice=new AdminNotice();
		model.addAttribute("noticeObj",notice);
		return "admin/admin_notice";
	}
	
	@PostMapping("/postNotice")
	public String addNotice(@ModelAttribute AdminNotice notice,RedirectAttributes rda)
	{
		adminService.addNotice(notice);
		rda.addFlashAttribute("msg","Notice Posted");
		return "redirect:/admin/addNotice";
		
	}
	
	@GetMapping("/allNotices")
	public String allNotices(Model model) {
	    List<AdminNotice> notices = adminService.getAllNotices();
	   System.out.println(notices.size());
	   
	    model.addAttribute("notices", notices);//send to html
	    return "admin/all_notices";//page name
	}
	
	@GetMapping("/addEmployee")
	public String addEmployee()
	{	
	return "admin/employee";
	}
	@PostMapping("addEmployee")
	public String addEmployee(@ModelAttribute Employee employee ,@RequestParam("profileImage")MultipartFile file,RedirectAttributes rda)
	{
		adminService.addEmployee(employee,file);
		//model.addAttribute("ms", "employee added");//send to html
		rda.addFlashAttribute("msg","Employee added");
		return "redirect:/admin/addEmployee";//URL end point	
		// return "admin/employee";//page name
	} 
	
	@GetMapping("/editEmployee")
	public String editEmployee(@RequestParam("id") String employeeId, Model model) {
	    Employee employee = adminService.getEmployeeById(employeeId);
	    model.addAttribute("employee", employee);
	    return "admin/edit_employee";
	}

	@PostMapping("/editEmployee")
	public String saveEditedEmployee(@ModelAttribute Employee employee, RedirectAttributes rd) {
	    adminService.updateEmployee(employee);
	    rd.addFlashAttribute("msg", "Employee updated successfully.");
	    return "redirect:/admin/allEmployees";
	}
	
	@PostMapping("admin_login")
	public String  adminLogin(@RequestParam String email,@RequestParam("password") String pass,Model model,HttpSession session,RedirectAttributes rd)
	//we write column name in ("") only if parameter name and html variable name is different.
	{
		Admin admin=adminService.adminLogin(email,pass);
		if(admin != null)
		{
			model.addAttribute("adminInfo",admin);//forward -> only on admin_home for single request
			session.setAttribute("adminKey", admin);//to track for multiple request
			return "admin/admin_home";//html page name(view name)
		}
		rd.addFlashAttribute("msg","Invalid email/password");
		return "redirect:/admin/admin_login";//redirect takes endpoint
	}
	
	@GetMapping("adminHome")
	public String adminHome(HttpSession session,Model model ,RedirectAttributes rd)
	{
		Admin admin=(Admin)session.getAttribute("adminKey");
		if(admin!=null)
		{
		model.addAttribute("adminInfo",admin);
		return "admin/admin_home";
		}
		else
		{
			rd.addFlashAttribute("msg","Unauthorised access,login first");
			return "admin/admin_login";
		}
	}
	
	@GetMapping("adminLogout")
	public String adminHome(HttpSession session,RedirectAttributes rd)
	{
		session.removeAttribute("adminKey");
		session.invalidate();
		rd.addFlashAttribute("msg","Successfully Logged Out");
		return "redirect:/admin/admin_login";
	}
	
	@GetMapping("deleteUser")
	public String deleteUser(@RequestParam String email,Model model)
	{
		adminService.deleteUser(email);
		return "redirect:/admin/allUsers";
	}
	
	@GetMapping("deleteContact")
	public String deleteContact(@RequestParam String email,Model model)
	{
		adminService.deleteContact(email);
		return "redirect:/admin/allContacts";
	}
	
	@GetMapping("/deleteNotice")
	public String deleteNotice(@RequestParam("id") String id, RedirectAttributes rd) {
	    adminService.deleteNoticeById(Integer.parseInt(id));
	    rd.addFlashAttribute("msg", "Notice Deleted Successfully");
	    return "redirect:/admin/allNotices";
	}
	
	@GetMapping("/bookings")
	public String viewBookings(Model model) {
	    List<Booking> bookings = adminService.getAllBookings();
	    model.addAttribute("bookings", bookings);
	    return "admin/admin_bookings";
	}

	    @PostMapping("/booking/approve/{id}")
	    public String approveBooking(@PathVariable("id") Integer id) {
	        adminService.approveBooking(id);
	        return "redirect:/admin/bookings";
	    }

	    @PostMapping("/booking/reject/{id}")
	    public String rejectBooking(@PathVariable("id") Integer id) {
	        adminService.rejectBooking(id);
	        return "redirect:/admin/bookings";
	    }
	    
	}


