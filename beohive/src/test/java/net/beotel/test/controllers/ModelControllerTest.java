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
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.google.gson.Gson;

import net.beotel.config.WebMvcConfiguration;
import net.beotel.controllers.ModelController;
import net.beotel.dao.ModelDao;
import net.beotel.dao.PartnersDao;
import net.beotel.model.ModelUr;
import net.beotel.model.Partner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={WebMvcConfiguration.class})
@WebAppConfiguration
public class ModelControllerTest {

	private MockMvc mockMvc;
	
	@Mock
	private ModelDao modelDao;
	
	@InjectMocks
    private ModelController modelController;
	
	@Before
	public void setup(){
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");

		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(modelController).setViewResolvers(viewResolver).build();
	}
	
	@Test
	public void testGetAllModels() throws Exception {

		
		List<ModelUr> modeli=new ArrayList<>();
		ModelUr m1=new ModelUr();
				m1.setId(1);
				m1.setIme("Android");
				ModelUr m2=new ModelUr();
				m2.setId(2);
				m2.setIme("Tobisa");
				modeli.add(m1);
				modeli.add(m2);
				
				Mockito.when(modelDao.getModels()).thenReturn(modeli);
				
		this.mockMvc.perform(get("/modeli"))
		.andExpect(status().isOk())
		      .andExpect(view().name("modeli"))
		      .andExpect(MockMvcResultMatchers.model().attribute("modeli", hasSize(2)))
		      .andExpect(MockMvcResultMatchers.model().attributeExists("registracija"))
		      .andExpect(MockMvcResultMatchers.model().attributeExists("model"))
		      .andExpect(MockMvcResultMatchers.model().attribute("modeli", hasSize(2)))
		     .andExpect(MockMvcResultMatchers.model().attribute("modeli", hasItem(
                     allOf(
                             hasProperty("id", is(1)),
                             hasProperty("ime", is("Android"))
                             
                     )
             )))
             .andExpect(MockMvcResultMatchers.model().attribute("modeli", hasItem(
                     allOf(
                             hasProperty("id", is(2)),
                             hasProperty("ime", is("Tobisa"))
                       
                     )
             )));
	}
	
	@Test
	public void insertModel() throws Exception {
		
		ModelUr m=new ModelUr();
		m.setIme("And");
		m.setId(1);
		String json=new Gson().toJson(m);
		
		this.mockMvc.perform(post("/modeli/insertModel").content(json))
		        .andExpect(status().is3xxRedirection())
		        .andExpect(view().name("redirect:/modeli"));
	}
	
	@Test
	public void getModelById() throws Exception {
		
		
		Mockito.when(modelDao.getModel(1)).thenReturn(getModel());
		
	this.mockMvc.perform(get("/modeli/getModel").param("id", "1").contentType("application/json;charset=UTF-8"))
		        .andExpect(status().is2xxSuccessful())
		        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
		        .andExpect(MockMvcResultMatchers.jsonPath("$.ime", is(getModel().getIme())))
		        .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(getModel().getId())));
		        
	
	}
	
	@Test
	public void updateModel() throws Exception {
		
		ModelUr m=new ModelUr();
		m.setId(1);
		m.setIme("And");
		String json=new Gson().toJson(m);
		this.mockMvc.perform(post("/modeli/updateModel").content(json)
						.contentType(MediaType.APPLICATION_JSON_UTF8))
		.andExpect(status().is5xxServerError())
		.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8));
	
	}
	@Test
	public void deleteModel() throws Exception {
		
		Mockito.doThrow(HibernateException.class).when(modelDao).deleteModel(1);
		this.mockMvc.perform(get("/modeli/deleteModel").param("id", "1")
						.contentType(MediaType.APPLICATION_JSON_UTF8))
		.andExpect(status().is5xxServerError());
	
	}
	
	private ModelUr getModel(){
		
		ModelUr m=new ModelUr();
		m.setId(1);
		m.setIme("Android");
		
		return m;
	}
	
}