package net.beotel.controllers;


import net.beotel.model.Partner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
	
	
	@RequestMapping(value="/login", method=RequestMethod.GET)
	public String showlogin(Model model, @RequestParam(value="error", required = false) String error){
		
		if(error != null)
			model.addAttribute("error", "Uneti podaci nisu ispravni.");
		
		
		return "login";
	}
	
	@RequestMapping(value="/doLogIn", method=RequestMethod.POST)
	public String doLogIn(@ModelAttribute("partner") Partner partner){
		
		
		return "redirect:/";
	}	
	
	
}