package net.beotel.test.controllers;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
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
import net.beotel.controllers.UredjajController;
import net.beotel.dao.ModelDao;
import net.beotel.dao.OperatorDao;
import net.beotel.dao.PartnersDao;
import net.beotel.dao.UredjajDao;
import net.beotel.model.ModelUr;
import net.beotel.model.Operater;
import net.beotel.model.Partner;
import net.beotel.model.Uredjaj;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={WebMvcConfiguration.class})
@WebAppConfiguration
public class UredjajControllerTest {

	private MockMvc mockMvc;
	
	@Mock
	private UredjajDao uredjajDao;
	@Mock
	private ModelDao modelDao;
	@Mock
	private PartnersDao partnerDao;
	@Mock
	private BindingResult bindingResult;
	@Mock
	private OperatorDao operatorDao;
	
	@InjectMocks
    private UredjajController uredjajController;
	
	@Before
	public void setup(){
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");
        
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(uredjajController).setViewResolvers(viewResolver).build();
	}
	
	@Test
	public void uredjaj() throws Exception{
		
		setSecurity();
		mockMvc.perform(get("/uredjaj"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("uredjaj"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("partneri"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("modeli"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("registracija"));
	}
	
	@Test
	public void getUredjajById() throws Exception {
		
		setSecurity();
		Mockito.when(uredjajDao.getUredjaj(1)).thenReturn(getUredjaj());
		
	mockMvc.perform(get("/uredjaj/getUredjaj").param("id", "1").contentType("application/json;charset=UTF-8"))
		        .andExpect(status().is2xxSuccessful())
		        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
		        .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(getUredjaj().getId())))
		        .andExpect(MockMvcResultMatchers.jsonPath("$.macAdresa", is(getUredjaj().getMacAdresa())))
		        .andExpect(MockMvcResultMatchers.jsonPath("$.sn", is(getUredjaj().getSn())));
		        
	
	}
	
	@Test
	public void updateUredjaj() throws Exception {
		
		setSecurity();
		
		String json=new Gson().toJson(getUredjaj());
		
		this.mockMvc.perform(post("/uredjaj/updateUredjaj").content(json)
						.contentType(MediaType.APPLICATION_JSON_UTF8))
		.andExpect(status().is5xxServerError())
		.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8));
	
	}
	
	@Test
	public void deleteUredjaj() throws Exception {
		
		setSecurity();
		Mockito.doThrow(HibernateException.class).when(uredjajDao).delUredjaj(1);;
		this.mockMvc.perform(get("/uredjaj/delete").param("id", "1")
						.contentType(MediaType.APPLICATION_JSON_UTF8))
		.andExpect(status().is5xxServerError());
	
	}
	@Test
	public void filter() throws Exception{
		
		setSecurity();
		this.mockMvc.perform(get("/uredjaj/filter").param("pid", "1").param("mid", "1"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("uredjaji"))
		.andExpect(view().name("uredjaji"));
	}
	
	private Uredjaj getUredjaj(){
		
		Uredjaj u=new Uredjaj();
		u.setId(1);
		u.setMacAdresa("12345");
		u.setSn("54321");
		u.setPartner(null);
		u.setModel(new ModelUr());
		u.setStatus(1);
		
		return u;
	}
	private void setSecurity(){
		User u=new User("Ivan", "Jankovic", true, true, true, true, new ArrayList<>());
		Authentication authentication = Mockito.mock(Authentication.class);
		// Mockito.whens() for your authorization object
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		Mockito.when(authentication.getPrincipal()).thenReturn(u);
		Operater op=new Operater();
		Partner p=new Partner();
		p.setId(1);
		op.setPartner(p);
		Mockito.when(operatorDao.getOperatorByUsername("Ivan", "username")).thenReturn(op);
	}
}
