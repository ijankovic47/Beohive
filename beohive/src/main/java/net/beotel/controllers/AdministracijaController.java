package net.beotel.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import net.beotel.dao.AdministracijaDao;
import net.beotel.model.Korisnik;
import net.beotel.model.PartnerCenovnik;
import net.beotel.service.AdministracijaService;
/**
 * @author ivan *
 */
@Controller
@RequestMapping(value = "/administracija")
public class AdministracijaController {

	@Autowired
	AdministracijaDao administracijaDao;
	
	@Autowired
	AdministracijaService administracijaService;
	
	Korisnik korisnik=null;
	
	@RequestMapping(method=RequestMethod.GET)
	public String getAdmin(Model model){
		
		
			model.addAttribute("korisnik", korisnik);
			if(korisnik!=null){
				model.addAttribute("uredjaji", administracijaDao.getSlobodneUredjajePartnera(korisnik.getPartner().getId()));
				String[] osnovniPaket=administracijaService.getOsnovniPaket(korisnik.getPartner());
				model.addAttribute("osnovniPaket", osnovniPaket);
				List<String> doplatniPaketi=administracijaService.getDoplatniPaketi(korisnik.getPartner());
				model.addAttribute("doplatniPaketi", doplatniPaketi);
				String[] doplatniPaketiKorisnika=null;
				if(korisnik.getPaketi()!=null){
				       doplatniPaketiKorisnika=korisnik.getPaketi().split("#");
					}
					else{
						doplatniPaketiKorisnika=new String[0];
					}
				model.addAttribute("doplatniPaketiKorisnika", doplatniPaketiKorisnika);
				
				model.addAttribute("paketi", paketi);
				paketi=false;
				model.addAttribute("zamena", zamena);
				zamena=false;
				model.addAttribute("actDeact", actDeact);
				actDeact=false;
				model.addAttribute("uredjaj", uredjaj);
				uredjaj=false;
				
				model.addAttribute("sinhronizacija", administracijaService.proveriSinhronizaciju(korisnik.getId()));
				
			}
			korisnik=null;
		
		
		return "administracija";
	}
	
	@RequestMapping("/pretragaSnMac")
	public String getKorisniciSnMac(@RequestParam("sn") String sn){
	
		korisnik=administracijaDao.findKorisnikByUredjaj(sn);
		return "redirect:/administracija";
	}
	@RequestMapping("/pretragaUid")
	public String getKorisniciUid(@RequestParam("uid") String uid){
	
		korisnik=administracijaDao.findKorisnikByUid(uid);
		return "redirect:/administracija";
	}
	
	@RequestMapping("/actDeact")
	public String actDeact(@RequestParam("idKor") int idKor){
		
		administracijaDao.actDeact(idKor);
		actDeact=true;
		korisnik=administracijaDao.getKorisnikById(idKor);
		
		return "redirect:/administracija";
	}
	@RequestMapping("/zamenaUredjaja")
	public String zamenaUredjaja(@RequestParam("uredjaj") int id, @RequestParam("korId") int korId){
		
		if(id==0){
			
			uredjaj=true;
			korisnik=administracijaDao.getKorisnikById(korId);
			return "redirect:/administracija";
		}
		
		administracijaDao.zameniUredjaj(korId, id);
		korisnik=administracijaDao.getKorisnikById(korId);
		zamena=true;
		
		return "redirect:/administracija";
	}
	
	@RequestMapping("/packageAddRemove")
	public String packageAddRemove(@RequestParam("korId") int korId, @RequestParam("package") String paket){
		
		administracijaDao.packageAddRemove(korId, paket);
		korisnik=administracijaDao.getKorisnikById(korId);
		
		return "redirect:/administracija";
	}
	
	@RequestMapping("/stop")
	public String stopirajKorisnika(@RequestParam("korId") int korId){
		
		administracijaDao.stopKorisnik(korId);
		korisnik=administracijaDao.getKorisnikById(korId);
		
		return "redirect:/administracija";
	}
	@RequestMapping("/sinhronizuj")
	public String sinhronizujKorisnika(@RequestParam("korId") int korId){
		
		administracijaService.sinhronizuj(korId);
		
        korisnik=administracijaDao.getKorisnikById(korId);
		return "redirect:/administracija";
		
	}
	
	@RequestMapping("/obracun")
	public String getObracun(@RequestParam("id") int korId){
	
		administracijaService.getObracun(korId);
		
		return "redirect:/administracija";
	}
	
	private boolean paketi=false;
	private boolean zamena=false;
	private boolean actDeact=false;
	private boolean uredjaj=false;
}
