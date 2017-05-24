package net.beotel.controllers;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.annotation.RequestScope;

import net.beotel.dao.OperatorDao;
import net.beotel.dao.PartnersDao;
import net.beotel.model.Operater;
import net.beotel.model.OperaterDetails;
import net.beotel.service.KorisnikServiceImpl;
import net.beotel.services.OperaterService;

/**
 * @author nemanja
 * 
 */
@Controller
@RequestMapping(value="/operater")
public class OperaterController {

	@Autowired
	private OperatorDao operatorDaoImpl;
	@Autowired
	private PartnersDao partnersDaoImpl;
	@Autowired
	private OperaterService operaterService; 
	private static final Logger LOG = Logger.getLogger(OperaterController.class);
		
	@ModelAttribute
	public void setOperatorModel(Model model){		
		model.addAttribute("partneri", partnersDaoImpl.getPartners());
		model.addAttribute("opr", new Operater());
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public String showAllOperators(Model model){
		model.addAttribute("operateri", operatorDaoImpl.getAllOperators());	
		return "operateri";
	}
	
	@RequestMapping(value="/{partnerId}", method=RequestMethod.GET)
	public String showAllOprsForPartner(@PathVariable("partnerId") int id, Model model, Authentication auth){
		OperaterDetails opd = (OperaterDetails) auth.getPrincipal();
		
		if(opd.getPartner().getId() != id)
			return "operateri";
				
		model.addAttribute("operateri", operatorDaoImpl.getAllOperatersForPartner(id));
		return "operateri";
	}

	@RequestMapping(value="/{prefix}/operateri", method=RequestMethod.GET)
	public String showAllOprsForSelectedPartners(@PathVariable("prefix") String prefix, Model model){		
		model.addAttribute("prefix", prefix);
		model.addAttribute("operateri", operatorDaoImpl.getAllOperatersByPartnerPrefix(prefix));
		return "operateri";
	}
	
	/*
	 * Sa forme i preko ajax POST f-je dobija podatke sa forme za unos opr. 
	 * Proverava dobijene podatke pomoću @Valid i ukoliko ima nevalidnih podataka uzima objekte grešaka iz BindingResolt obj
	 * i smešta ih u niz Objekata i vraća ga kao ResponseEntity<Object[]>.
	 * Ako je validacija prošla vraća dobijeni objekat kako bi ga ajax funkcija prikazala kao novi red sa dodatim operaterom.
	 * */
	@RequestMapping(value="/register", method=RequestMethod.POST, 
					consumes=MediaType.APPLICATION_JSON_UTF8_VALUE, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public ResponseEntity<Object[]> registerNewOperator(@Valid @RequestBody Operater operater, BindingResult result){
		if(result.hasErrors()){
			Object[] errors = result.getFieldErrors().toArray();
			return new ResponseEntity<Object[]>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
		}			
				
		String encPass = new Md5PasswordEncoder().encodePassword(operater.getPassword(), null);
		operater.setPassword(encPass);
		operatorDaoImpl.addNewOperator(operater);
		Operater opr = operatorDaoImpl.getOperatorById(operater.getId());
		Object[] response = {opr};
		return new ResponseEntity<Object[]>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value="/edit/{id}", method=RequestMethod.GET)
	@ResponseBody
	public Operater editUserById(@PathVariable("id") int id){
		Operater opr = operatorDaoImpl.getOperatorById(id);	
		return opr;
	}
	
	@RequestMapping(value="/update/{id}", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Object[]> updateUserById(@Valid @RequestBody Operater updOperater, BindingResult result,
									@PathVariable("id") int id){
		if(result.hasErrors()){
			Object[] errors = result.getFieldErrors().toArray();
			return new ResponseEntity<Object[]>(errors, HttpStatus.INTERNAL_SERVER_ERROR); 
		}
		try{
			operatorDaoImpl.updateOperator(updOperater);
			Operater opr = operatorDaoImpl.getOperatorById(id);
			Object[] response = {opr};
			return new ResponseEntity<Object[]>(response, HttpStatus.OK);
		}catch(Exception ex){
			return new ResponseEntity<Object[]>(HttpStatus.INTERNAL_SERVER_ERROR);
		}		
				
	}
	@RequestMapping(value="/delete/{id}", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> deleteUserById(@PathVariable("id") int id){
		try{
			operatorDaoImpl.deleteOperator(id);
			return new ResponseEntity<String>("Operater uspešno obrisan",HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<String>("Greska pri brisanju! Operater nije obrisan.", HttpStatus.INTERNAL_SERVER_ERROR);
		}	
	}
	
	@RequestMapping(value="/chpasswd/{id}", method=RequestMethod.GET)
	public String changePasswordPage(@PathVariable("id") int id, Model model){
		model.addAttribute("oprId", id);
		return "chpasswd";
	}
	

	@RequestMapping(value="/chpasswd/{id}", method=RequestMethod.POST)
	public ResponseEntity<String> changePasswordPage(@RequestParam String password, @RequestParam String retypePasswd, @PathVariable("id") int id){
		
		try{
			operaterService.proveriPoklapanjeLozinke(id, password, retypePasswd);
			return new ResponseEntity<String>("Lozinka promenjena.", HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
		}
	}
}