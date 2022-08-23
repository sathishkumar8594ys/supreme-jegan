package com.springboot.project.controller;

import java.util.ArrayList;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.springboot.project.database.Users;

@Controller
@RequestMapping("onlinequiz")
public class UserController {
	
	@Autowired
	private JdbcTemplate jdbctemplate;
	
	@RequestMapping(value="/home", method=RequestMethod.GET)
	public String homeget()
	{
		return "userstructure";
	}
	
	@RequestMapping(value="/logout", method=RequestMethod.GET)
	public String logoutget()
	{
		return "login";
	}
	
	@RequestMapping(value="/login" , method=RequestMethod.GET)
	public String loinget()
	{
		return "login";
	}
	
	@RequestMapping(value="/login" , method=RequestMethod.POST)
	public String loinpost(@RequestParam String email,@RequestParam String pass )
	{
		ArrayList<Users> list=(ArrayList<Users>) jdbctemplate.query("select * from userinfos",BeanPropertyRowMapper.newInstance(Users.class));
		for(int i=0;i<list.size();i++)
		{
			Users t=list.get(i);
			if(t.getEmail().equals(email) && t.getPassword().equals(pass))
			{
				return "userstructure";
			}
		}
		return "login";
	}
	
	@RequestMapping(value="/register" , method=RequestMethod.GET)
	public String registerget()
	{
		return "register";
	}
	
	@RequestMapping(value="/register" , method=RequestMethod.POST)
	public String registerpost(@RequestParam String name ,@RequestParam String dept,@RequestParam String email, @RequestParam String gender ,@RequestParam long phone, @RequestParam String password)
	{
		ArrayList<Users> list=(ArrayList<Users>) jdbctemplate.query("select * from userinfos",BeanPropertyRowMapper.newInstance(Users.class));
		for(int i=0;i<list.size();i++)
		{
			Users t=list.get(i);
			if(t.getEmail().equals(email) && t.getPassword().equals(password))
			{
				return "login";
			}
		}
		Random random=new Random();
		int key=random.nextInt(99999999);
		String sql="insert into userinfos (student_id,department,email,gender,name,password,phone) values (?,?,?,?,?,?,?)";
		jdbctemplate.update(sql, key,dept,email,gender,name,password,phone);
		return "login";
	}
	
	@RequestMapping(value="/taketest" , method=RequestMethod.GET)
	public String taketestget(Model model)
	{
		
		model.addAttribute("ques1","1.java is 100% objects oriented? A.True B.False");
		model.addAttribute("ques2","2.what is immutable in string? A.change content B.cannot change content");
		model.addAttribute("ques3","3.Range of int? A.-2^31 to 2^31 B.0 to 2^31");
		model.addAttribute("ques4","4.who is father of java? A.dennis ritche B.james gosling");
		model.addAttribute("ques5","5.Are you familiar with java? A.yes B.no");
		return "taketest";
	}
	
	@RequestMapping(value="/checkresult", method=RequestMethod.GET)
	public String check()
	{
		return "result";
	}
	
	@RequestMapping(value="/checkresult" , method=RequestMethod.POST)
	public String checkresult(@RequestParam String a1,@RequestParam String a2,@RequestParam String a3,@RequestParam String a4,@RequestParam String a5,Model model)
	{
		int marks=0;
		if(a1.equals("B"))
			marks++;
		if(a2.equals("B"))
			marks++;
		if(a3.equals("A"))
			marks++;
		if(a4.equals("B"))
			marks++;
		if(a5.equals("A"))
			marks++;
		model.addAttribute("marks", marks);
		String s="";
		if(marks>=5/2)
			s+="pass";
		else s+="fail";
		model.addAttribute("status",s);
		model.addAttribute("testname","Java Mcq");
		model.addAttribute("totalmarks","5");
		return "result";
	}
	
//	@Autowired
//	private JavaMailSender mailsender; 
//	
//	public void sendSimpleMail(String tomail,String body, String sub)
//	{
//		SimpleMailMessage message=new SimpleMailMessage(); 
//		
//		message.setFrom("kavinsellamuthu004@gmail.com");
//		message.setTo(tomail);
//		message.setText(body);
//		message.setSubject(sub);
//		
//		mailsender.send(message);
//	}
}
