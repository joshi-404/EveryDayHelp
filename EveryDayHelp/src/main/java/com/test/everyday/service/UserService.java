package com.test.everyday.service;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.test.everyday.entity.Admin;
import com.test.everyday.entity.Booking;
import com.test.everyday.entity.Feedback;
import com.test.everyday.entity.UserDetails;
import com.test.everyday.repository.BookingRepository;
import com.test.everyday.repository.FeedbackRepository;
import com.test.everyday.repository.UserRepository;
@Service
public class UserService 
{
	@Autowired
	JdbcTemplate template;
	
	@Autowired
    private BookingRepository bookingRepository;
	
	private final BCryptPasswordEncoder passwordEncoder;//password encoding
	
	

	private final UserRepository userRepository;
	private final FeedbackRepository feedbackRepository;
	
	public UserService(UserRepository userRepository,FeedbackRepository feedbackRepository) {
		super();
		this.userRepository = userRepository;
		this.feedbackRepository = feedbackRepository;
		this.passwordEncoder=new BCryptPasswordEncoder();
	}

	public void registerUser(UserDetails userDetails, MultipartFile file) {
		try 
		{
			//get root directory of running project
			String projectRoot = System.getProperty("user.dir");
			System.out.println("ProjectRoot is "+projectRoot);
			
			//define folder name inside project
			String folderName="uploads";
			
			//construct full path for upload directory
			String uploadPath=projectRoot+File.separator+folderName;
			System.out.println("Upload Path is "+uploadPath);
			
			//create the upload directory if it dosen't exists
			File uploadDir=new File(uploadPath);
			
			if(!uploadDir.exists())
			{
				uploadDir.mkdirs();//create parent directories if needed
				System.out.println("Created upload folder at: "+uploadDir.getAbsolutePath());
			}
			
			//get file name from the MultiPart
			String originalFilename=file.getOriginalFilename();
			System.out.println("File name is "+originalFilename);
			
			String uniqueFilename=System.currentTimeMillis()+"_"+originalFilename;
			
			//create the destination file
			File destinationFile=new File(uploadDir,uniqueFilename);
			
			//transfer the file from MultipartFile to the destination
			file.transferTo(destinationFile);
			System.out.print("Image saved to: "+destinationFile.getAbsolutePath());
			
			//Save relative path (folderName/fileName) to database
			String DB_PATH=folderName+"/"+uniqueFilename;
			userDetails.setPic(DB_PATH);
			
		String encryptedPassword=passwordEncoder.encode(userDetails.getPassword());
		System.out.println("password after encryption "+encryptedPassword);
		userDetails.setPassword(encryptedPassword);
		userRepository.save(userDetails);
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
//	public UserDetails userLogin(String email, String pass)
//	{
//		UserDetails user=userRepository.findByEmailAndPassword(email, pass);
//		return user;
//	}

	//
	public UserDetails userLogin(String email, String rawpassword)
	{
		UserDetails user=null;
		String sql="select * from user_details where email = ?";
		try
		{
			 user=template.queryForObject(sql, new RowMapper<UserDetails>() {

				@Override
				public UserDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
					UserDetails ud=new UserDetails();
					//email, address, name, password, phone, pic
					ud.setEmail(rs.getString("email"));
					ud.setAddress(rs.getString("address"));
					ud.setName(rs.getString("name"));
					ud.setPhone(rs.getString("phone"));
					ud.setPic(rs.getString("pic"));
					ud.setPassword(rs.getString("password"));
					
					return ud;//it might be null if email and password does not match
				}
			}, email);
			 
			 //Verify password using BCrypt
			 BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
			 boolean passwordMatches=encoder.matches(rawpassword, user.getPassword());
			 
			 if(passwordMatches)
			 {
				 return user;
			 }
			 else {
				 return null;
			 }
		}
		catch(EmptyResultDataAccessException erda)
		{
			erda.printStackTrace();
		}
		return user;
	}

	 public void saveBooking(Booking booking) {
	        booking.setStatus("pending");
	        booking.setDate(LocalDateTime.now());
	        bookingRepository.save(booking);
	    }
	 
	 public void addFeedback(Feedback feedback) 
		{
			Feedback f = feedbackRepository.findByEmail(feedback.getEmail());
			if (f != null) {
		        // Update the existing feedback with new values
		        f.setRemarks(feedback.getRemarks());
		        f.setRating(feedback.getRating());
		        // Add any other fields that need to be updated
		        feedbackRepository.save(f);
		    } else {
		        // No existing feedback; save new one
		        feedbackRepository.save(feedback);
		    }				
		}
	
}
