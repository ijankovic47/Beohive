package net.beotel.test.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import net.beotel.config.WebMvcConfiguration;
import net.beotel.controllers.AdministracijaController;
import net.beotel.dao.AdministracijaDao;
import net.beotel.service.AdministracijaService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={WebMvcConfiguration.class})
@WebAppConfiguration
public class AdministracijaControllerTest {

	private MockMvc mockMvc;
	
	@Mock
	AdministracijaDao administracijaDao;
	@Mock
	AdministracijaService administracijaService;
	
	@InjectMocks
	AdministracijaController administracijaController;
	
	@Before
	public void setup(){
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");

		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(administracijaController).setViewResolvers(viewResolver).build();
	}
	
	
	@Test
	public void getKorisniciSnMac() throws Exception{
		
		this.mockMvc.perform(get("/administracija/pretragaSnMac").param("sn", "a"))
		.andExpect(status().is3xxRedirection());
	}
	
	@Test
	public void getKorisniciByUid() throws Exception{
		
		this.mockMvc.perform(get("/administracija/pretragaUid").param("uid", "a"))
		.andExpect(status().is3xxRedirection());
	}
	
	@Test
	public void actDeact() throws Exception{
		
		this.mockMvc.perform(get("/administracija/actDeact").param("idKor", "1"))
		.andExpect(status().is3xxRedirection());
	}
	
	@Test
	public void zamenaUredjaja() throws Exception{
		
		this.mockMvc.perform(get("/administracija/zamenaUredjaja").param("korId", "1").param("uredjaj", "1"))
		.andExpect(status().is3xxRedirection());
	}
	
	@Test
	public void packageAddRemove() throws Exception{
		
		this.mockMvc.perform(get("/administracija/packageAddRemove").param("korId", "1").param("package", "1"))
		.andExpect(status().is3xxRedirection());
	}
	
	@Test
	public void stop() throws Exception{
		
		this.mockMvc.perform(get("/administracija/stop").param("korId", "1"))
		.andExpect(status().is3xxRedirection());
	}
	
	@Test
	public void sinhronizuj() throws Exception{
		
		this.mockMvc.perform(get("/administracija/sinhronizuj").param("korId", "1"))
		.andExpect(status().is3xxRedirection());
	}
	
	@Test
	public void obracun() throws Exception{
		
		this.mockMvc.perform(get("/administracija/obracun").param("id", "1"))
		.andExpect(status().is3xxRedirection());
	}

}
