package net.beotel.test.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.AssertTrue;

import org.hamcrest.Matchers;
import org.hibernate.HibernateException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.google.gson.Gson;

import net.beotel.config.WebMvcConfiguration;
import net.beotel.controllers.PartnersController;
import net.beotel.dao.PartnersDao;
import net.beotel.model.Operater;
import net.beotel.model.Partner;
import net.beotel.test.util.TestUtility;
import static org.mockito.Matchers.*;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={WebMvcConfiguration.class})
@WebAppConfiguration
public class PartnerControllerTest {

	private MockMvc mockMvc;
		
	@Mock
	private PartnersDao partnersDao;
	@Mock
	private BindingResult bindingResult;
	@InjectMocks
    private PartnersController partnersController;
	
	@Before
	public void setup(){
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");

		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(partnersController).setViewResolvers(viewResolver).build();
	}
	
	
	@Test
	public void testGetAllPartners() throws Exception {     //good

		
		List<Partner> partneri=new ArrayList<>();
		Partner p1=new Partner();
				p1.setId(1);
				p1.setName("Ivan");
		Partner p2=new Partner();
				p2.setId(2);
				p2.setName("Nemanja");
				partneri.add(p1);
				partneri.add(p2);
				
				Mockito.when(partnersDao.getPartners()).thenReturn(partneri);
				
		this.mockMvc.perform(get("/partner"))
		.andExpect(status().isOk())
		      .andExpect(view().name("partners"))
		      .andExpect(MockMvcResultMatchers.model().attribute("partners", hasSize(2)))
		      .andExpect(MockMvcResultMatchers.model().attributeExists("register"))
		      .andExpect(MockMvcResultMatchers.model().attributeExists("newPartner"))
		      .andExpect(MockMvcResultMatchers.model().attribute("partners", hasSize(2)))
		     .andExpect(MockMvcResultMatchers.model().attribute("partners", hasItem(
                     allOf(
                             hasProperty("id", is(1)),
                             hasProperty("name", is("Ivan"))
                             
                     )
             )))
             .andExpect(MockMvcResultMatchers.model().attribute("partners", hasItem(
                     allOf(
                             hasProperty("id", is(2)),
                             hasProperty("name", is("Nemanja"))
                       
                     )
             )));

	}
	
	@Test
	public void registerPartner() throws Exception {      //???
		
		Partner p=new Partner();
		p.setPrefix("aasdasd");
		p.setName("basdasd");
		Mockito.when(partnersDao.savePartner(Mockito.any(Partner.class))).thenThrow(new HibernateException("1"));  //mnogo dobra stvar
		
		this.mockMvc.perform(post("/partner/register").param("name", p.getName()).param("prefix", p.getPrefix()))
		        .andExpect(status().isOk())
		        .andExpect(MockMvcResultMatchers.model().attributeExists("partners"))
		        .andExpect(MockMvcResultMatchers.model().attributeExists("poruka"))
		        .andExpect(MockMvcResultMatchers.model().attributeExists("prefixName"))
		        .andExpect(view().name("partners"));
	}
	
	@Test
	public void getPartneById() throws Exception {
		
		
		Mockito.when(partnersDao.getPartner(1)).thenReturn(getPartner());
		
	this.mockMvc.perform(get("/partner/getPartner").param("id", "1").contentType("application/json;charset=UTF-8"))
		        .andExpect(status().is2xxSuccessful())
		        .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(getPartner().getId())))
		        .andExpect(MockMvcResultMatchers.jsonPath("$.prefix", is(getPartner().getPrefix())))
		        .andExpect(MockMvcResultMatchers.jsonPath("$.name", is(getPartner().getName())))
		        .andExpect(MockMvcResultMatchers.jsonPath("$.status", is(getPartner().getStatus())))
		        .andExpect(MockMvcResultMatchers.jsonPath("$.maxNoOp", is(getPartner().getMaxNoOp())))
		        .andExpect(MockMvcResultMatchers.jsonPath("$.operators", is(getPartner().getOperators())))
		        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
	
	}
	@Test
	public void updatePartner() throws Exception {
		
		Mockito.doNothing().when(partnersDao).editPartner(getPartner());
		this.mockMvc.perform(post("/partner/updatePartner").content("{\"prefix\":\"werwe2r\"}")
						.contentType(MediaType.APPLICATION_JSON_UTF8))
		.andExpect(status().is5xxServerError())
		.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
	
	}
	
	private Partner getPartner(){
		Partner p=new Partner();
		p.setId(1);
		p.setMaxNoOp(10);
		p.setName("IvanJ");
		p.setPrefix("BEOTEL");
		p.setStatus(1);
		List<Operater> operateri=new ArrayList<>();
		p.setOperators(operateri);
		return  p;
	}

}
