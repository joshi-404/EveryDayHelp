package com.test.everyday.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.test.everyday.entity.Admin;
import com.test.everyday.entity.AdminNotice;
import com.test.everyday.entity.Booking;
import com.test.everyday.entity.Contacts;
import com.test.everyday.entity.Employee;
import com.test.everyday.entity.UserDetails;
import com.test.everyday.repository.AdminNoticeRepository;
import com.test.everyday.repository.AdminRepository;
import com.test.everyday.repository.BookingRepository;
import com.test.everyday.repository.ContactRepository;
import com.test.everyday.repository.EmployeeRepository;
import com.test.everyday.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class AdminService 
{

	 @Autowired
	    private JdbcTemplate jdbcTemplate;
	
	private final EmployeeRepository employeeRepository;
	private final AdminNoticeRepository adminNoticeRepository;
	private final AdminRepository adminRepository;
	private final UserRepository userRepository;
	private final ContactRepository contactRepository;
	private final BookingRepository bookingRepository;
	
	public void addEmployee(Employee employee, MultipartFile file) {
		try 
		{
			
			String projectRoot = System.getProperty("user.dir");
			System.out.println("ProjectRoot is "+projectRoot);
			
			String folderName="employees";
			
			String uploadPath=projectRoot+File.separator+folderName;
			System.out.println("Upload Path is "+uploadPath);
			
			File uploadDir=new File(uploadPath);
			
			if(!uploadDir.exists())
			{
				uploadDir.mkdirs();
				System.out.println("Created upload folder at: "+uploadDir.getAbsolutePath());
			}
			
			String originalFilename=file.getOriginalFilename();
			System.out.println("File name is "+originalFilename);
			
			String uniqueFilename=System.currentTimeMillis()+"_"+originalFilename;
			
			File destinationFile=new File(uploadDir,uniqueFilename);
			
			file.transferTo(destinationFile);
			System.out.print("Image saved to: "+destinationFile.getAbsolutePath());
			
			String DB_PATH=folderName+"/"+uniqueFilename;
			employee.setEmployeePic(DB_PATH);
			
			employeeRepository.save(employee);
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
		
	
}

public void addNotice(AdminNotice notice) {
		
	    adminNoticeRepository.save(notice); 
	}


	public List<UserDetails> allUsers() 
	{
		List<UserDetails> userList = userRepository.findAll();
		return userList;
	}

	public List<Employee> allEmployees() 
	{
		List<Employee> employeeList = employeeRepository.findAll();
		return employeeList;
	}

	public List<Contacts> allContacts() 
	{
		List<Contacts> contactList = contactRepository.findAll();
		return contactList;
	}

	public Admin adminLogin(String email, String pass)
	{
		Admin admin=adminRepository.findByEmailAndPassword(email, pass);
		return admin;
	}

	@Transactional//it is a transaction task like in dbms
	public void deleteUser(String email) {
		userRepository.deleteByEmail(email);
		
	}

	@Transactional
	public void deleteContact(String email) {
		contactRepository.deleteByEmail(email);
		
	}

	public UserDetails editUser(String email) {
	    return userRepository.findByEmail(email);
	}

	public void editUserFinal(UserDetails modifiedUser) {
		String email=modifiedUser.getEmail();
		UserDetails oldUser=userRepository.findByEmail(email);
		oldUser.setName(modifiedUser.getName());
		oldUser.setPhone(modifiedUser.getPhone());
		oldUser.setAddress(modifiedUser.getAddress());
		userRepository.save(oldUser);
	}

	public Employee getEmployeeById(String employeeId) {
	    return employeeRepository.findByEmployeeId(employeeId);
	}

	public void updateEmployee(Employee updatedEmployee) {
	    Employee existing = employeeRepository.findByEmployeeId(updatedEmployee.getEmployeeId());
	    if (existing != null) {
	        existing.setPhone(updatedEmployee.getPhone());
	        existing.setQualification(updatedEmployee.getQualification());
	        existing.setExperience(updatedEmployee.getExperience());
	        employeeRepository.save(existing);
	    }
	}
	
	public List<AdminNotice> getAllNotices() {
		List<AdminNotice> noticeList = adminNoticeRepository.findAll();
		return noticeList;
	}

	public void deleteNoticeById(int id) {
        String sql = "DELETE FROM admin_notice WHERE notice_id = ?";
        jdbcTemplate.update(sql, id);
    }

	public List<Booking> getAllBookings() {
	    return bookingRepository.findAll();
	}
	
	public void approveBooking(Integer id) {
	    Booking booking = bookingRepository.findById(id).orElse(null);
	    if (booking != null) {
	        booking.setStatus("approved");
	        bookingRepository.save(booking);
	    }
	}

	public void rejectBooking(Integer id) {
	    Booking booking = bookingRepository.findById(id).orElse(null);
	    if (booking != null) {
	        booking.setStatus("rejected");
	        bookingRepository.save(booking);
	    }
	}

	}


