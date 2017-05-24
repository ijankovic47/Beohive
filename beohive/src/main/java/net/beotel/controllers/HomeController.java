package net.beotel.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;


import net.beotel.dao.OperatorDao;
import net.beotel.dao.UredjajDao;

@Controller
public class HomeController {

	@Autowired
	OperatorDao operaterDao;
	
	@Autowired
	UredjajDao uredjajDao;
	
	@RequestMapping("/")
	public String showHome(){
		
		return "index";
	}
	
	@RequestMapping("/welcome")
	public String successLoginRedirect(HttpServletRequest request){
		
		return "index";
	}
	
}
