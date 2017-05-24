package net.beotel.test.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.matchers.Contains;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;

import static org.hamcrest.Matchers.is;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

import net.beotel.config.WebMvcConfiguration;
import net.beotel.model.Partner;
import net.beotel.services.OperaterService;
import net.beotel.test.util.TestUtility;
import net.beotel.controllers.OperaterController;
import net.beotel.dao.OperatorDao;
import net.beotel.dao.PartnersDao;
import net.beotel.model.Operater;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={WebMvcConfiguration.class})
@WebAppConfiguration
public class OperaterControllerTest {

	private MockMvc mockMvc;
	
	@Mock
	private OperatorDao operatorDaoImpl; 
	@Mock
	private PartnersDao partnersDaoImpl;
	@Mock
	private Md5PasswordEncoder md5PasswordEncoder;
	@Mock
	private OperaterService OperaterService;
	@Mock
	private BindingResult bindingResult;
		   		
	@InjectMocks
	private OperaterController operaterController;
	
	@Before
	public void setup(){
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(operaterController).build();
	}		
	
	@Test
	public void testGetAllOperators() throws Exception {
		Mockito.when(operatorDaoImpl.getAllOperators()).thenReturn(getListOfDummyOprs());
		this.mockMvc.perform(get("/operater"))
				.andExpect(status().is2xxSuccessful())
		        .andExpect(view().name("operateri"));
	}
	
	
	@Test
	public void testGetoperatorForUpd() throws Exception{
		Mockito.doReturn(getOperater()).when(operatorDaoImpl).getOperatorById(1);
		this.mockMvc.perform(get("/operater/edit/{id}",1)
					.contentType("application/json;charset=UTF-8")
					)
			        .andExpect(status().is2xxSuccessful())
			        .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
			        .andExpect(MockMvcResultMatchers.jsonPath("$.ime", Matchers.containsString("Test")))
					.andExpect(MockMvcResultMatchers.jsonPath("$.prezime", Matchers.containsString("Testic")))
			        .andDo(MockMvcResultHandlers.print());
	}	
	
	@Test
	public void addNewOperaterTest() throws Exception{
	
		String json2 = "{\"id\":\"999\",\"ime\":\"Marko\",\"prezime\":\"Markovic\",\"username\":\"mmarkovic\",\"password\":\"password\",\"email\":\"markovic@gmail.com\",\"active\":\"0\",\"partner\":{\"id\":\"316\"}}";
						
		Mockito.doReturn(getOperater()).when(operatorDaoImpl).getOperatorById(1);
		Mockito.doNothing().when(operatorDaoImpl).addNewOperator(getOperater());
		Mockito.doReturn(false).when(bindingResult).hasErrors();
				
		this.mockMvc.perform(post("/operater/register")
				.contentType("application/json;charset=UTF-8")
				.content(TestUtility.convertObjectToJsonBytes(getOperater()))
				)
				.andExpect(status().is2xxSuccessful())
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].ime", Matchers.containsString("Test")))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].prezime", Matchers.containsString("Testic")))
				.andDo(MockMvcResultHandlers.print());				
	}
	
	@Test
	public void addWrongOperaterTest() throws Exception{
		Mockito.doReturn(true).when(bindingResult).hasErrors();
		 
		Mockito.doNothing().when(operatorDaoImpl).addNewOperator(getOperater());
		Mockito.doReturn(getOperater()).when(operatorDaoImpl).getOperatorById(1);
		
		Operater invlOpr = getOperater();
		invlOpr.setIme("123");
		invlOpr.setPrezime(null);
		invlOpr.setRole("ROLE_OPERATER");   
		
		this.mockMvc.perform(post("/operater/register")
				.contentType("application/json;charset=UTF-8").header("x-requested-with", "XMLHttpRequest")
				.content(TestUtility.convertObjectToJsonBytes(invlOpr))
				)
				.andExpect(status().is5xxServerError())
				.andDo(MockMvcResultHandlers.print());
	}	
	/*
	 * 	Testiranje dodavanja novog operatera sa forme. Pravim Sting "json" u obliku json objekta
	 * */	
	@Test
	public void updateOperatorTest() throws Exception{
		final String json = "{\"ime\":\"test\",\"prezime\":\"Testic\",\"username\":\"test001\",\"password\":\"password\",\"email\":\"test001@gmail.com\",\"active\":\"0\",\"partner\":{\"id\":\"1\"}}";

		Mockito.doReturn(getOperater()).when(operatorDaoImpl).getOperatorById(1);
		Mockito.doNothing().when(operatorDaoImpl).updateOperator(getOperater());
		this.mockMvc.perform(post("/operater/update/{id}",1)
				.content(json)
				.contentType("application/json;charset=UTF-8")
				)
				.andExpect(status().is2xxSuccessful())
		        .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
		        .andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void deleteOperatorTest() throws Exception{
		Mockito.doNothing().when(operatorDaoImpl).deleteOperator(1);
		this.mockMvc.perform(get("/operater/delete/{id}",1)
				)
				.andExpect(status().is2xxSuccessful())
				.andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN_VALUE))
		        .andDo(MockMvcResultHandlers.print());
	}
	
	private List<Operater> getListOfDummyOprs(){
		List<Operater> oprs = new ArrayList<>(1);
		oprs.add(getOperater());
		return oprs;
	}
	
	// Dummy Operator
	private Operater getOperater(){
		Operater operater = new Operater();
		operater.setId(1);
		operater.setIme("Test");
		operater.setPrezime("Testic");
		operater.setUsername("test001");
		operater.setEmail("test001@gmail.com");
		operater.setPassword("password");
		operater.setPartner(getPartner());
		operater.setActive((byte)0);
		operater.setRole("ROLE_OPERATER");
		return operater;
	}
	
	private Partner getPartner(){
		Partner partner = new Partner();
		partner.setId(316);
		partner.setName("Beotel");
		return partner;
	}	
}