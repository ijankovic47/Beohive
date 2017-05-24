package net.beotel.test.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import iptv.rest.dao.IptvRestDaoImplV2;
import iptv.rest.dao.IptvRestDaoV2;
import iptv.rest.service.BInteractiveServiceImplV2;
import iptv.rest.service.IptvService;
import iptv.rest.util.IptvBInteractiveUtilV2;
import iptv.rest.v2.model.Device;
import iptv.rest.v2.model.Subscriber;
import iptv.rest.v2.model.SubscriberPackage;
import net.beotel.config.WebMvcConfiguration;
import net.beotel.dao.AdministracijaDao;
import net.beotel.dao.OperatorDao;
import net.beotel.model.Cenovnik;
import net.beotel.model.Korisnik;
import net.beotel.model.ModelUr;
import net.beotel.model.Operater;
import net.beotel.model.Partner;
import net.beotel.model.Uredjaj;
import net.beotel.service.AdministracijaService;
import net.beotel.service.CenovnikService;
import net.beotel.serviceImpl.AdministracijaServiceImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={WebMvcConfiguration.class})
@WebAppConfiguration
public class AdministracijaServiceTest {

	private MockMvc mockMvc;

	@Mock
	AdministracijaDao administracijaDao;

	@Mock
	private CenovnikService cenovnikService;

	@Mock
	private OperatorDao operatorDao;
	
	@Mock
	private IptvRestDaoV2 iptvRestDaoV2 = new IptvRestDaoImplV2();
	@Mock
	private IptvService iptvService = new BInteractiveServiceImplV2(iptvRestDaoV2);
	@Mock
	private BInteractiveServiceImplV2 bis = new BInteractiveServiceImplV2(iptvRestDaoV2);
	@Mock
	private IptvBInteractiveUtilV2 iptvUtil = IptvBInteractiveUtilV2.getInstance();
	
	@InjectMocks
	private AdministracijaServiceImpl administracijaService;
	
	@Before
	public void setup(){
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(administracijaService).build();
	}
	
	@Test
	public void getOsnovniPaket(){
		
		Partner p=new Partner();
		Cenovnik c=new Cenovnik();
		c.setPaketi("Pink#Arena");
		c.setStatus(1);
		c.setTip('o');
		p.getCenovnici().add(c);
		
		String[] ocekivano={"Pink","Arena"};
		
		String[] rezultat=administracijaService.getOsnovniPaket(p);
		Assert.assertEquals(ocekivano, rezultat);
	}
	
	@Test
	public void getDoplatnipaketi(){
		
		Partner p=new Partner();
		Cenovnik c1=new Cenovnik();
		Cenovnik c2=new Cenovnik();
		c1.setPaketi("Arena");
		c2.setPaketi("Pink");
		c1.setTip('d');
		c2.setTip('p');
		c1.setStatus(1);
		c2.setStatus(1);
	    p.getCenovnici().add(c1);
	    p.getCenovnici().add(c2);
	    
		List<String> rezultat=administracijaService.getDoplatniPaketi(p);
		
		List<String> ocekivano=new ArrayList<>();
		ocekivano.add("Arena");
		ocekivano.add("Pink");
		
		Assert.assertEquals(ocekivano, rezultat);
	}
	
	@Test
	public void proveriSinhronizaciju(){
		
		Subscriber s=new Subscriber();
		List<Device> devices=new ArrayList<>();
		Korisnik k=new Korisnik();
		k.setUredjaj(new Uredjaj());
		k.setStatus("ACTIVE");
		k.setPaketi("Pink Package");
		s.setStatus("ACTIVE");
		s.setUid("c");
		k.setUid("a");
		devices.add(new Device());
		k.getUredjaj().setMacAdresa("a");
		devices.get(0).setUid("a");
		List<SubscriberPackage> sp=new ArrayList<>();
		sp.add(new SubscriberPackage());
		sp.get(0).setPackage_id(10);
		iptv.rest.v2.model.Package p=new iptv.rest.v2.model.Package();
		p.setUid("Pink");
		Map<String, String> mapa=new HashMap<>();
		mapa.put("Pink Package", "Pink");
		
		Mockito.doReturn(s).when(iptvRestDaoV2).findSubscriber("a");
		Mockito.doReturn(devices).when(iptvRestDaoV2).findAllSubscriberDevices("a");
		Mockito.doReturn(k).when(administracijaDao).getKorisnikById(1);
		Mockito.doReturn(sp).when(iptvRestDaoV2).findAllSubscriberPackages("c");
		Mockito.doReturn(p).when(iptvRestDaoV2).findPackageById(10);
		Mockito.doReturn(mapa).when(cenovnikService).mapaPaketa();
		boolean rezultat=administracijaService.proveriSinhronizaciju(1);
		boolean ocekivano=true;
		Assert.assertEquals(ocekivano, rezultat);
	}
	
	@Test
	public void sinhronizuj(){
		
		Korisnik k=new Korisnik();
		Uredjaj ur=new Uredjaj();
		ur.setMacAdresa("a");
		ModelUr model=new ModelUr();
		model.setImeNaPlatformi("b");
		ur.setSn("c");
		ur.setModel(model);
		k.setUredjaj(ur);
		k.setStatus("ACTIVE");
		k.setUid("nemanja");
		Operater o=new Operater();
		Subscriber sub=new Subscriber();
		sub.setStatus("ACTIVE");
		Device dev=new Device();
		dev.setUid("a");
		List<Device> devices=new ArrayList<>();
		devices.add(dev);
		
		User u=getUserFromSecContext();
		
		Mockito.doReturn(o).when(operatorDao).getOperatorByUsername("Ivan", "username");
		Mockito.doReturn(sub).when(iptvRestDaoV2).findSubscriber("nemanja");
		Mockito.doReturn(k).when(administracijaDao).getKorisnikById(1);
		Mockito.doReturn(devices).when(iptvRestDaoV2).findAllSubscriberDevices("nemanja");
		
		administracijaService.sinhronizuj(1);
		
	}
	
	
	
	
	private User getUserFromSecContext(){
		
		User u=new User("Ivan", "Jankovic", true, true, true, true, new ArrayList<>());
		Authentication authentication = Mockito.mock(Authentication.class);
		// Mockito.whens() for your authorization object
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		Mockito.when(authentication.getPrincipal()).thenReturn(u);
		
		return u;
	}
}
