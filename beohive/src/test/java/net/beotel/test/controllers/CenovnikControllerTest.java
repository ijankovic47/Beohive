package net.beotel.test.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;

import javax.net.ssl.SSLEngineResult.Status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import net.beotel.config.WebMvcConfiguration;
import net.beotel.controllers.CenovnikController;
import net.beotel.dao.PartnersDao;
import net.beotel.model.Cenovnik;
import net.beotel.model.Partner;
import net.beotel.model.PartnerCenovnik;
import net.beotel.service.CenovnikService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={WebMvcConfiguration.class})
@WebAppConfiguration
public class CenovnikControllerTest {

	private MockMvc mockMvc;
	
	@Mock
	private PartnersDao partnersDao;
	
	@Mock
	private CenovnikService cenovnikService;
	
	@InjectMocks
	private CenovnikController cenovnikController;
	
	@Before
	public void setup(){
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");

		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(cenovnikController).setViewResolvers(viewResolver).build();
	}
	@Test
	public void getPaketi() throws Exception{
		
		Partner p=new Partner();
		Mockito.when(partnersDao.getPartner(1)).thenReturn(p);
		
		this.mockMvc.perform(get("/cenovnik/paketi").param("id", "1"))
		.andExpect(view().name("paketiEdit"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("pc"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("dolazak"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("id"))
		.andExpect(status().isOk());
	}
	
	@Test
	public void kombinacije() throws Exception{
		
		
		this.mockMvc.perform(post("/cenovnik/createKombinacije")
				.param("cene[0]", "100.0")
				.param("tipovi[0]", "O")
				.param("cenaOsn", "900.0")
				.param("tipovi[1]", "D")
				.param("cene[1]", ""))
		
		.andExpect(status().is3xxRedirection());
	}
	
	@Test
	public void cenovnik() throws Exception{
		
		this.mockMvc.perform(post("/cenovnik/createCenovnik").param("tipovi[0]", "O")
				.param("cenaOsn", "100.0"))
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/cenovnik/paketiPrikaz?id=0"));
	}
	
	@Test
	public void prikazPaketa() throws Exception{
		
		Partner p=new Partner();
		p.getCenovnici().add(new Cenovnik());
		PartnerCenovnik pc=new PartnerCenovnik();
		
		Mockito.when(partnersDao.getPartner(1)).thenReturn(p);
		Mockito.when(cenovnikService.getCenovnik(p.getCenovnici())).thenReturn(pc);
		Mockito.when(cenovnikService.osnovniPaket(pc)).thenReturn(new String[0]);
		Mockito.when(cenovnikService.getDoplata(pc)).thenReturn(new String[0]);
		Mockito.when(cenovnikService.kombinacije(pc)).thenReturn(new ArrayList<>());
		
		this.mockMvc.perform(get("/cenovnik/paketiPrikaz").param("id", "1")).andExpect(view().name("paketiView"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("osnovniPaket"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("doplatniPaketi"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("kombinacije"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("cenovnikKreiran"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("istorijaO"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("kombinacijaDatum"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("istorijaK"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("istorijaD"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("id"))
		.andExpect(view().name("paketiView"));
	}
}
