package net.beotel.test.controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import net.beotel.config.WebMvcConfiguration;
import net.beotel.dao.CenovnikDao;
import net.beotel.dao.OperatorDao;
import net.beotel.model.Cenovnik;
import net.beotel.model.Operater;
import net.beotel.model.Partner;
import net.beotel.model.PartnerCenovnik;
import net.beotel.service.CenovnikService;
import net.beotel.serviceImpl.CenovnikServiceImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={WebMvcConfiguration.class})
@WebAppConfiguration
public class CenovnikServiceTest {


	private MockMvc mockMvc;
		
	@Mock
	private CenovnikDao cenovnikDao;
	@Mock
	private OperatorDao operaterDao;
	@Mock
	private BindingResult bindingResult;
	@InjectMocks
    private CenovnikServiceImpl cenovnikService;
	
	@Before
	public void setup(){
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");

		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(cenovnikService).build();
	}
	
	@Test
	public void getCenovnik(){
	
		PartnerCenovnik ocekivano=new PartnerCenovnik();
		ocekivano.setCenaOsn("900.0");
		List<String> tipovi=new ArrayList<>();
		tipovi.add("O");
		tipovi.add("O");
		tipovi.add("X");
		tipovi.add("D");
		List<String> cene=new ArrayList<>();
		for(int i=0;i<4;i++){
			cene.add(i, null);
		}
		cene.add(3,"100.0");
		ocekivano.setCene(cene);
		ocekivano.setTipovi(tipovi);
		
		PartnerCenovnik rezultat=cenovnikService.getCenovnik(getCenovnici());
		
		Assert.assertEquals(ocekivano.getCenaOsn(), rezultat.getCenaOsn());
		Assert.assertEquals(ocekivano.getTipovi().get(0), rezultat.getTipovi().get(0));
		Assert.assertEquals(ocekivano.getTipovi().get(1), rezultat.getTipovi().get(1));
		Assert.assertEquals(ocekivano.getTipovi().get(3), rezultat.getTipovi().get(3));
		Assert.assertEquals(ocekivano.getCene().get(3), rezultat.getCene().get(3));
	}
	@Test
	public void listaPaketa(){
		
		List<String> ocekivano=new ArrayList<>();
		ocekivano.add("Basic Package");
		ocekivano.add("Pink Package");
		ocekivano.add("BeoInfo");
		ocekivano.add("Arena234 Package");
		ocekivano.add("Pink XXX Package");
		ocekivano.add("Basic Catchup");
		ocekivano.add("Basic Timeshift");
		ocekivano.add("Basic Recorder");
		ocekivano.add("SRBNetInfo");
		ocekivano.add("Test paket");
		ocekivano.add("Basic Live");
		
		List<String> rezultat=cenovnikService.listaPaketa();
		
		Assert.assertEquals(ocekivano,rezultat);
	}
	@Test
	public void mapaPaketa(){
		
		Map<String, String> ocekivano=new HashMap<>();
		
		ocekivano.put("Basic Package", "BeoTV Basic");
		ocekivano.put("Pink Package", "Pink");
		ocekivano.put("BeoInfo", "BeoInfo");
		ocekivano.put("Arena234 Package", "Arena Sport 234");
		ocekivano.put("Pink XXX Package", "PinkXXX");
		ocekivano.put("Basic Catchup", "basiccatchup");
		ocekivano.put("Basic Timeshift", "basictimeshift");
		ocekivano.put("Basic Recorder", "basicrecorder");
		ocekivano.put("SRBNetInfo", "SRBInfo");
		ocekivano.put("Test paket", "TestPaket");
		ocekivano.put("Basic Live", "basiclive");
		
		Map<String, String> rezultat=cenovnikService.mapaPaketa();
		
		Assert.assertEquals(ocekivano,rezultat);
	}
	@Test
	public void getDoplata(){
		
		String[] ocekivano={"Basic Package"};
		
		String[] rezultat=cenovnikService.getDoplata(getPartnerCenovnik());
		Assert.assertEquals(ocekivano,rezultat);
	}
	@Test
	public void istorija(){
		
		List<Cenovnik> cenovnici=getCenovnici();
		List<List<String>> rezultat=cenovnikService.istorija(cenovnici, 'o');
		
		List<String> ocekivano=new ArrayList<>();
		ocekivano.add("Basic Package#Pink Package");
		ocekivano.add("1000.0");
		ocekivano.add("Ivan Jankovic(beotel)");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyy HH:mm");
		ocekivano.add(cenovnici.get(2).getDatumOd().format(formatter));
		ocekivano.add(cenovnici.get(2).getDatumDo().format(formatter));
		
		Assert.assertEquals(ocekivano,rezultat.get(0));
		
	}
	@Test
	public void osnovnipaket(){
		
		PartnerCenovnik pc=new PartnerCenovnik();
		List<String> tipovi=new ArrayList<>();
		tipovi.add("O");
		tipovi.add("O");
		tipovi.add("O");
		pc.setTipovi(tipovi);
		pc.setCenaOsn("100.0");
		
		String[] rezultat=cenovnikService.osnovniPaket(pc);
		
		String[] ocekivano={"Basic Package#Pink Package#BeoInfo","100.0",""};
		
		Assert.assertEquals(ocekivano,rezultat);
	}
	@Test
	public void doplatniPaketi(){
		
		
		PartnerCenovnik pc=new PartnerCenovnik();
		List<String> tipovi=new ArrayList<>();
		tipovi.add("D");
		tipovi.add("P");
		List<String> cene=new ArrayList<>();
		cene.add("100.0");
		cene.add("80.0");
		List<LocalDateTime> datumi=new ArrayList<>();
		datumi.add(LocalDateTime.now());
		datumi.add(LocalDateTime.now());
		
		pc.setDopDate(datumi);
		pc.setTipovi(tipovi);
		pc.setCene(cene);
		
		List<String[]> rezultat=cenovnikService.doplatniPaketi(pc);
		
		List<String[]> ocekivano=new ArrayList<>();
		
		String[] niz1={"Basic Package","100.0", datumi.get(0).format(formatter),"Ne"};
		String[] niz2={"Pink Package","80.0", datumi.get(1).format(formatter),"Da"};
		
		ocekivano.add(niz1);
		ocekivano.add(niz2);
		
		Assert.assertEquals(ocekivano.get(0),rezultat.get(0));
		Assert.assertEquals(ocekivano.get(1),rezultat.get(1));
	}
	
	@Test
	public void kombinacije(){
		
		PartnerCenovnik pc=new PartnerCenovnik();
		List<String> kombinacijeIme=new ArrayList<>();
		kombinacijeIme.add("Basic Package+Pink Package");
		List<String> kombinacije=new ArrayList<>();
		kombinacije.add("200.0");
		List<LocalDateTime> datumi=new ArrayList<>();
		datumi.add(LocalDateTime.now());
		
		pc.setKomDate(datumi);
		pc.setKombinacije(kombinacije);
		pc.setKombinacijeIme(kombinacijeIme);
		
		List<String[]> rezultat=cenovnikService.kombinacije(pc);
		
		String[] ocekivano={"Basic Package+Pink Package","200.0",pc.getKomDate().get(0).format(formatter)};
		
		Assert.assertEquals(ocekivano,rezultat.get(0));
	}
	
	
	
	
	
	
	private List<Cenovnik> getCenovnici(){
		
		List<Cenovnik> cenovnici=new ArrayList<>();
		
Operater op=new Operater();
		
		op.setIme("Ivan");
		op.setPrezime("Jankovic");
		
		Partner p=new Partner();
		
		p.setPrefix("beotel");
		
		op.setPartner(p);
		
		Cenovnik c=new Cenovnik();
		
		c.setCena(900);
		c.setPaketi("Basic Package#Pink Package");
		c.setStatus(1);
		c.setTip('o');
		cenovnici.add(c);
		c=new Cenovnik();
		c.setTip('d');
		c.setCena(100);
		c.setPaketi("Arena234 Package");
		c.setStatus(1);
		cenovnici.add(c);
		
		c=new Cenovnik();
		
		c.setCena(1000);
		c.setPaketi("Basic Package#Pink Package");
		c.setStatus(0);
		c.setTip('o');
		c.setOperater(op);
		c.setPartner(p);
		c.setDatumOd(LocalDateTime.now());
		c.setDatumDo(LocalDateTime.now());
		cenovnici.add(c);
		
		
		return cenovnici;
	}
	
	private PartnerCenovnik getPartnerCenovnik(){
		
		PartnerCenovnik pc=new PartnerCenovnik();
		List<String> tipovi =new ArrayList<>();
		tipovi.add(0,"D");
		pc.setTipovi(tipovi);
		List<String> cene =new ArrayList<>();
		cene.add(0,"100.0");
		pc.setCene(cene);
		
		return pc;
	}
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyy HH:mm");
	
}
