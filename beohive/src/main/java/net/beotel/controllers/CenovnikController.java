package net.beotel.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import net.beotel.dao.PartnersDao;
import net.beotel.model.Partner;
import net.beotel.model.PartnerCenovnik;
import net.beotel.service.CenovnikService;
/**
 * @author ivan *
 */
@Controller
@RequestMapping(value = "/cenovnik")
public class CenovnikController {

	@Autowired
	PartnersDao partnersDao;

	@Autowired
	CenovnikService cenovnikService;

	private List<String> komime;
	private List<String> komcena;

	private String poruka;
	private boolean cenovnikKreiran=false;

	@RequestMapping("/paketi")
	public String getPaketi(Model model, @RequestParam("id") int id) {

		List<String> paketi = cenovnikService.listaPaketa();
		Partner p=partnersDao.getPartner(id);

		model.addAttribute("paketi", paketi);
		model.addAttribute("partnerId", id);
		if (p.getCenovnici().size() > 0) {
			PartnerCenovnik pc = cenovnikService.getCenovnik(p.getCenovnici());
			kombinacije = pc.getKombinacije();
			kombinacijeIme = pc.getKombinacijeIme();
			model.addAttribute("pc", pc);
			model.addAttribute("dolazak", 1);
			model.addAttribute("poruka", poruka);
			komime = pc.getKombinacijeIme();
			komcena = pc.getKombinacije();
			model.addAttribute("komime", komime);
			model.addAttribute("komcene", komcena);
			model.addAttribute("id", id);
			poruka = null;
			pcp=pc;
		} else {
			model.addAttribute("pc", new PartnerCenovnik());
			model.addAttribute("dolazak", 1);
			model.addAttribute("poruka", poruka);
			model.addAttribute("id", id);
			poruka = null;
			pcp.setPartnerId(id);
		}
		return "paketiEdit";
	}

	@RequestMapping(value = "/createKombinacije", method = RequestMethod.POST)
	public String kombinacije(@ModelAttribute("pc") PartnerCenovnik pc, Model model) {

		if(pc.getCene().size()<11){
			for(int i=pc.getCene().size(); i<11;i++){
				pc.getCene().add(null);                               //disabled polja se u ovom koraku ne tretiraju kao null, nzm zasto 
			}
		}
		if (pc.getTipovi().size() == 0) {

			poruka = "Niste odabrali nijedan paket !";
			return "redirect:/cenovnik/paketi?id=" + pc.getPartnerId();

		}
		boolean o = false;
		for (String s : pc.getTipovi()) {
			if (s != null) {
				if (s.equals("O")) {
					o = true;
					break;
				}
			} else {
				continue;
			}
		}
		if (o == false) {
			poruka = "Niste odabrali nijedan paket kao osnovni !";
			return "redirect:/cenovnik/paketi?id=" + pc.getPartnerId();
		}

		if (pc.getCenaOsn().equals("")) {
			poruka = "Niste uneli cenu osnovnog paketa !";
			return "redirect:/cenovnik/paketi?id=" + pc.getPartnerId();
		}
		for (int i = 0; i < pc.getTipovi().size(); i++) {
			if (pc.getTipovi().get(i) != null) {
				if (pc.getTipovi().get(i).equals("D") || pc.getTipovi().get(i).equals("P")) {
					if (pc.getCene().get(i).equals("")) {
						poruka = "Niste uneli sve cene doplatnih paketa !";
						return "redirect:/cenovnik/paketi?id=" + pc.getPartnerId();
					}
				}
			}
		}

		String[] doplatniPaketi = cenovnikService.getDoplata(pc);   //kako ovo testirati
		Stack<String> stack = new Stack<>();
		List<String> kombinacije = new ArrayList<>(1024);
		for (int i = 2; i <= doplatniPaketi.length; i++) {

			cenovnikService.createKombinacije(kombinacije, stack, doplatniPaketi, 0, i);
		}

		List<String> paketi = cenovnikService.listaPaketa();

		model.addAttribute("kombinacije", kombinacije);
		model.addAttribute("paketi", paketi);
		pc.setKombinacijeIme(kombinacijeIme);
		pc.setKombinacije(this.kombinacije);
		model.addAttribute("pc", pc);
		model.addAttribute("dolazak", 2);
		model.addAttribute("komime", komime);
		model.addAttribute("komcene", komcena);
		model.addAttribute("cena", 1);
        model.addAttribute("osnovniPaket", cenovnikService.osnovniPaket(pc));
        model.addAttribute("doplatniPaket", cenovnikService.doplatniPaketi(pc));
		model.addAttribute("id", pc.getPartnerId());
		pcp=pc;
		
		return "paketiEdit";
	}

	@RequestMapping(value = "/createCenovnik", method = RequestMethod.POST)
	public String cenovnik(@ModelAttribute("pc") PartnerCenovnik pc) {

		if (pc.getTipovi().size() == 0) {

			poruka = "Niste odabrali nijedan paket !";
			return "redirect:/cenovnik/paketi?id=" + pc.getPartnerId();

		}
		boolean o = false;
		for (String s : pc.getTipovi()) {
			if (s != null) {
				if (s.equals("O")) {
					o = true;
					break;
				}
			} else {
				continue;
			}
		}
		if (o == false) {
			poruka = "Niste odabrali nijedan paket kao osnovni !";
			return "redirect:/cenovnik/paketi?id=" + pc.getPartnerId();
		}
		if (pc.getCenaOsn().equals("")) {
			poruka = "Niste uneli cenu osnovnog paketa !";
			return "redirect:/paketi?id=" + pc.getPartnerId();
		}
		for (int i = 0; i < pc.getTipovi().size(); i++) {
			if (pc.getTipovi().get(i) != null) {
				if (pc.getTipovi().get(i).equals("D")) {
					if (pc.getCene().get(i).equals("")) {
						poruka = "Niste uneli sve cene doplatnih paketa !";
						return "redirect:/cenovnik/paketi?id=" + pc.getPartnerId();
					}
				}
			}
		}

		cenovnikService.createCenovnik(pc);
		
		cenovnikKreiran=true;

		return "redirect:/cenovnik/paketiPrikaz?id=" + pc.getPartnerId();
	}
	
	@RequestMapping("/paketiPrikaz")
	public String prikazPaketa(@RequestParam("id") int id, Model model){
		
		Partner p=partnersDao.getPartner(id);
		if(p.getCenovnici().isEmpty()){
			
			model.addAttribute("id", id);
			
			return "paketiView";
		}
		PartnerCenovnik pc=cenovnikService.getCenovnik(p.getCenovnici());
		model.addAttribute("osnovniPaket", cenovnikService.osnovniPaket(pc));
		model.addAttribute("doplatniPaketi", cenovnikService.doplatniPaketi(pc));
		model.addAttribute("kombinacije", cenovnikService.kombinacije(pc));
		model.addAttribute("cenovnikKreiran", cenovnikKreiran);
		cenovnikKreiran=false;
		model.addAttribute("kombinacijaDatum", pc.getKomDate());
		model.addAttribute("istorijaO", cenovnikService.istorija(p.getCenovnici(), 'o'));
		model.addAttribute("istorijaK", cenovnikService.istorija(p.getCenovnici(), 'k'));
		model.addAttribute("istorijaD", cenovnikService.istorija(p.getCenovnici(), 'd'));
		
		model.addAttribute("id", id);
		
		return "paketiView";
	}
	/**
	@RequestMapping("/back")
	private String goBack(Model model){
		
		List<String> paketi = cenovnikService.listaPaketa();
		model.addAttribute("pc", pcp);
		model.addAttribute("paketi", paketi);
		model.addAttribute("dolazak", 1);
		model.addAttribute("id", pcp.getPartnerId());
		return "paketiEdit";
	}
	**/

	private PartnerCenovnik pcp=new PartnerCenovnik();
	private List<String> kombinacijeIme;
	private List<String> kombinacije;
}
