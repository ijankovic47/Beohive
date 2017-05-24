package net.beotel.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import net.beotel.dao.ModelDao;
import net.beotel.model.ModelUr;
import net.beotel.model.Partner;


/**
 * @author ivan *
 */
@Controller
@PreAuthorize("hasRole('BEOTEL')")
@RequestMapping(value="/modeli")
public class ModelController {

	@Autowired
	ModelDao modelDao;
	
	private boolean registracija=false;
	private int noviModel;
	
	@RequestMapping(method=RequestMethod.GET)
	public String getModelJsp(Model model){
		
		model.addAttribute("modeli", modelDao.getModels());
		model.addAttribute("model", new ModelUr());
		model.addAttribute("registracija", registracija);
		model.addAttribute("noviModel", noviModel);
		noviModel=0;
		registracija=false;
		
		return "modeli";
	}
	@RequestMapping(value="/insertModel", method=RequestMethod.POST)
	public String insertModel(@Valid @ModelAttribute("model") ModelUr model,  BindingResult bindingResult, Model mod){
		
		if(bindingResult.hasErrors()){
			mod.addAttribute("modeli", modelDao.getModels());
			mod.addAttribute("errors", bindingResult.getErrorCount());
			return "modeli";
		}
		try{
			noviModel=modelDao.insertModel(model);
			mod.addAttribute("dupliPrefiks", false);
			
			registracija=true;
		}
		catch(Exception e){
			mod.addAttribute("poruka", e.getMessage());
			mod.addAttribute("dupliPrefiks", true);
			mod.addAttribute("modeli", modelDao.getModels());
			return "/modeli";
		}		
		return "redirect:/modeli";
	}
	@RequestMapping(value="/getModel", method=RequestMethod.GET)
	@ResponseBody
	public ModelUr getModel(@RequestParam("id") int id){
		
		ModelUr model=modelDao.getModel(id);
		
		return model;
	}
	
	@RequestMapping(value="/deleteModel", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public ResponseEntity<String> deleteModelById(@RequestParam("id") int id){
		try{
			modelDao.deleteModel(id);
			return new ResponseEntity<String>("Model uspesno obrisan !",HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}	
	}
	
	@RequestMapping(value="/updateModel", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_UTF8_VALUE, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public ResponseEntity<String> updateModel(@Valid @RequestBody ModelUr model, BindingResult bindingResult){
		
		if(bindingResult.hasErrors()){
			return new ResponseEntity<String>("Ime modela mora sadrzati izmedju 5 i 30 karaktera, samo slova i '-'.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		try{
			modelDao.editModel(model);
			return new ResponseEntity<String>("Model uspesno izmenjen!",HttpStatus.OK);
		}
		catch(Exception e){
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	
		
		
	}
}
