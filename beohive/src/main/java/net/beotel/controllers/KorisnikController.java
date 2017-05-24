package net.beotel.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import iptv.rest.dao.IptvRestDaoImplV2;
import iptv.rest.dao.IptvRestDaoV2;
import iptv.rest.util.IptvBInteractiveUtilV2;
import iptv.rest.v2.model.Subscriber;
import net.beotel.dao.KorisnikDao;
import net.beotel.dao.ModelDao;
import net.beotel.dao.UredjajDao;
import net.beotel.dao.impl.KorisnikDaoImpl;
import net.beotel.model.Korisnik;
import net.beotel.model.OperaterDetails;
import net.beotel.service.KorisnikService;

@Controller
@RequestMapping(value="/korisnici")
public class KorisnikController {
	private static final Logger LOG = Logger.getLogger(KorisnikController.class);	
	
	@Autowired
	private UredjajDao uredjajDaoImpl;
	
	@Autowired
	private KorisnikService korisnikServiceImpl;
			
	@ModelAttribute
	public void populateSubscriberPage(Model model){
		model.addAttribute("korisnik", new Korisnik());
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public String showSybscribersHome(Model model){
		OperaterDetails opr = (OperaterDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		final int pid = opr.getPartner().getId();
		final String pname = opr.getPartner().getName();
		
		model.addAttribute("uredjaji", uredjajDaoImpl.getAllFreeDevicesByPartnerId(pid));
		model.addAttribute("paketi", korisnikServiceImpl.getListOfCenovnik(opr.getPartner()));
		
		LOG.info("Uzimam uredjaje koji pripadaju partneru: "+pname+" i popunjavam select Uredjaji");
		return "korisnici";
	}
		
	
	@RequestMapping(value="/dodaj", method=RequestMethod.POST)
	public String dodajNovogKorisnika(@ModelAttribute("korisnik") Korisnik korisnik, @RequestParam("paketi") String[] paketi, RedirectAttributes ra){
		final String respErrDiv = "<div class='sbc-action-response sbc-err-response'>Korisnik nije sačuvan!</div>";
		
		if(paketi.length == 0){
			ra.addFlashAttribute("response", respErrDiv);
			return "redirect:/korisnici";
		}			
		
		final String mergePaketi = korisnikServiceImpl.mergePackagesNames(paketi);
		korisnik.setPaketi(mergePaketi);
		LOG.info("Korisnik je odabrao sledece doplatne pakete: "+paketi.toString());
				
		String uid = korisnikServiceImpl.dodajNovogKorisnika(korisnik);
		
		if(uid != null){
//			final String respSuccDiv = "<div class='sbc-action-response sbc-succ-response'>Uspešno ste registrovali korisnika</div>";
//			ra.addFlashAttribute("response", respSuccDiv);
			return "redirect:/administracija/pretragaUid?uid="+uid;
		}else{			
			ra.addFlashAttribute("response", respErrDiv);
			return "redirect:/korisnici";
		}		
	}		
}