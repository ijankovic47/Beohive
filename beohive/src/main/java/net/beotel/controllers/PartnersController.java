package net.beotel.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import net.beotel.dao.PartnersDao;
import net.beotel.model.Cenovnik;
import net.beotel.model.Operater;
import net.beotel.model.Partner;
import net.beotel.model.PartnerCenovnik;
import net.beotel.service.CenovnikService;
import oracle.jdbc.proxy.annotation.Post;

/**
 * @author ivan
 * 
 */

@Controller
@RequestMapping(value="/partner")
public class PartnersController {

	@Autowired
	PartnersDao partnersDao;
	
	private boolean register=false; //promenljiva se menja na true ako je uspela registracija a na false ako nije zbog ispisa poruke na strani pri ponovnom dolasku
    private int noviPartnerId;
	
	@RequestMapping(method=RequestMethod.GET)
	public String getPartners(Model model){
		     
		model.addAttribute("register", register);                                                                     
		model.addAttribute("newPartner", new Partner());
		model.addAttribute("partners", partnersDao.getPartners());
		model.addAttribute("noviPartner", noviPartnerId);
		noviPartnerId=0;
		register=false;
		
		return "partners";
	}
	
	@RequestMapping(value="/register", method=RequestMethod.POST)
	public String registerNewUser(@Valid @ModelAttribute("newPartner") Partner newPartner, BindingResult bindingResult, Model model, HttpServletRequest request){

		if(bindingResult.hasErrors()){
			
			register=false;
			model.addAttribute("partners", partnersDao.getPartners());
			model.addAttribute("errors", bindingResult.getErrorCount());
			return "partners";
		}
		try{
			noviPartnerId=partnersDao.savePartner(newPartner); 
			
			register=true;
			return "redirect:/partner";}
		
		catch(Exception e){
			
			register=false;
			model.addAttribute("partners", partnersDao.getPartners());
			model.addAttribute("poruka", false);
			model.addAttribute("prefixName", Integer.valueOf(e.getMessage()));
			return "partners";
		}
		                                                              //registrovanje novog partnera
		
	}
	@RequestMapping(value="/getPartner", method=RequestMethod.GET)
	@ResponseBody
	public Partner getPartnerById(@RequestParam("id") int id){            //prosledjuje se partner na stranu preko ajax-a
	
		Partner p=partnersDao.getPartner(id);
		
		return p;		
	}

	@RequestMapping(value="/updatePartner", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_UTF8_VALUE, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public ResponseEntity<String> updatePartner(@Valid @RequestBody Partner updPartner, BindingResult bindingResult){
		
		if(bindingResult.hasErrors()){
			return new ResponseEntity<String>("Ime partnera mora sadrzati izmedju 5 i 30 karaktera, samo slova i '-'!", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		try{
			partnersDao.editPartner(updPartner);
			return new ResponseEntity<String>("Partner uspesno izmenjen",HttpStatus.OK);
		}
		catch(Exception e){
			
			return new ResponseEntity<String>("Vec postoji partner sa tim imenom !", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	
}
