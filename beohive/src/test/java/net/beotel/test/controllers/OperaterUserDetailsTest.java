package net.beotel.test.controllers;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import net.beotel.config.WebMvcConfiguration;
import net.beotel.dao.OperatorDao;
import net.beotel.model.Operater;
import net.beotel.security.OperatorUserDetailsService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={WebMvcConfiguration.class})
@WebAppConfiguration
public class OperaterUserDetailsTest {

	private MockMvc mockMvc;
	
	@Mock
	private OperatorDao operatorDaoImpl;
	
	@InjectMocks
	private OperatorUserDetailsService operatorUserDetailsService;
	
	@Before
	public void setup(){
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(operatorUserDetailsService).build();
	}
	/*
	 * 	Testira metodu getUserByUsername iz UserDetails sa ispravnim (postojećim) kredencijalima
	 * */
	@Test
	public void getUserByUsernameAndEmailTest(){
		Operater opr = getDummyOpr();
		Mockito.doReturn(opr).when(operatorDaoImpl).getOperatorByUsername("test", "username");
		Mockito.doReturn(opr).when(operatorDaoImpl).getOperatorByUsername("test@isp.beotel.net", "email");
		
		Assert.assertSame(operatorDaoImpl.getOperatorByUsername("test", "username"), opr);
		Assert.assertSame(operatorDaoImpl.getOperatorByUsername("test@isp.beotel.net", "email"), opr);
	}
	/*
	 * 	Testira metodu getUserByUsername iz UserDetails sa NEispravnim (NEpostojećim) kredencijalima
	 * */	
	@Test
	public void getUserByWrongCredentials(){
		Operater opr = getDummyOpr();
		Mockito.doReturn(opr).when(operatorDaoImpl).getOperatorByUsername("test", "username");
		Mockito.doReturn(opr).when(operatorDaoImpl).getOperatorByUsername("test@isp.beotel.net", "email");
		
		Assert.assertNotSame(operatorDaoImpl.getOperatorByUsername("tset", "username"), opr);
		Assert.assertNotSame(operatorDaoImpl.getOperatorByUsername("tset@isp.beotel.net", "email"), opr);
	}
		
	
	private Operater getDummyOpr(){
		Operater opr = new Operater();
		opr.setUsername("test");
		opr.setEmail("test@isp.beotel.net");
		return opr;
	}	
}