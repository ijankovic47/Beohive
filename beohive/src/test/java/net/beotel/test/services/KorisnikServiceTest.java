package net.beotel.test.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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
import iptv.rest.v2.model.Package;
import iptv.rest.v2.model.Subscriber;
import net.beotel.config.WebMvcConfiguration;
import net.beotel.dao.AdministracijaDao;
import net.beotel.dao.CenovnikDao;
import net.beotel.dao.KorisnikDao;
import net.beotel.dao.OperatorDao;
import net.beotel.dao.PartnersDao;
import net.beotel.dao.UredjajDao;
import net.beotel.model.Korisnik;
import net.beotel.model.ModelUr;
import net.beotel.model.Operater;
import net.beotel.model.OperaterDetails;
import net.beotel.model.Partner;
import net.beotel.model.Uredjaj;
import net.beotel.service.KorisnikService;
import net.beotel.service.KorisnikServiceImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={WebMvcConfiguration.class})
@WebAppConfiguration
public class KorisnikServiceTest {

	private MockMvc mockMvc;
	
	@Mock
	private KorisnikDao korisnikDaoImpl;
	@Mock
	private PartnersDao partnersDaoImpl; 
	@Mock
	private UredjajDao uredjajDaoImpl; 
	@Mock
	private CenovnikDao cenovnikDaoImpl;
	@Mock
	private OperatorDao operatorDaoImpl;
	@Mock
	private IptvRestDaoV2 iptvRestDaoV2 = new IptvRestDaoImplV2();
	@Mock
	private IptvService iptvService = new BInteractiveServiceImplV2(iptvRestDaoV2);
	@Mock
	private IptvBInteractiveUtilV2 interactiveUtil;
	@Mock
	private KorisnikService korisnikServiceImpl;
	@Mock
	private AdministracijaDao administracijaDaoImpl;
	@Mock
	private SecurityContext securityContext; 
	@Mock
	private Authentication authentication;
		
	@InjectMocks
	private KorisnikServiceImpl korisnikServiceImpln;
	
	private Date now = new Date();
	public static final String PAKETI_KORISNIKA= "Basic Package#Arena234 Package#Pink Package#Pink XXX Package#BeoInfo";
	public static final String[] IMENA_PAKETA = new String[]{"Basic Package","Arena234 Package","Pink Package","Pink XXX Package","BeoInfo"};
	public static final String IME_MODELA_NA_PLATF = "VIP1113";
	
	private final Korisnik korisnik = getDummyKorisnik();
	private final Operater operater = getDummyOpr();
	private final Partner partner = getDummyPartner();
	private final String[] imePrezime = new String[]{korisnik.getIme(),korisnik.getPrezime()};

	@Before
	public void setup(){
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(korisnikServiceImpln).build();
	}
	@Test
	public void dodajNovogKorisnikaTest() {		
		Mockito.doReturn(authentication).when(securityContext).getAuthentication();
		SecurityContextHolder.setContext(securityContext);
		Mockito.doReturn(getDummyOprDetails()).when(authentication).getPrincipal();
		
		Mockito.doReturn(partner).when(partnersDaoImpl).getPartner(1);
		Mockito.doReturn(getDummyuredjaj()).when(uredjajDaoImpl).getUredjaj(1);
		Mockito.doReturn(operater).when(operatorDaoImpl).getOperatorById(1);
		
		Mockito.doReturn(imePrezime)
				.when(korisnikServiceImpl).setDefaultImePrezimeIfEmpty(korisnik.getIme(), korisnik.getPrezime(), partner);
		
		Mockito.doReturn(korisnik.getUid()).when(korisnikServiceImpl).createUidWithFirstAndListName(korisnik.getIme(), korisnik.getPrezime(), partner);
		Mockito.doNothing().when(korisnikDaoImpl).dodajNovogKorisnika(korisnik);
		Mockito.doNothing().when(korisnikServiceImpl).convertToSubscriberAndSend(korisnik);
		
		Mockito.doNothing().when(korisnikServiceImpl).addDevicesToKorisnik(getDummyuredjaj(), korisnik);
		Mockito.doReturn(true).when(korisnikServiceImpl).addPackagesToSubscriber(PAKETI_KORISNIKA, korisnik.getUid());
		Mockito.doNothing().when(korisnikServiceImpl).createInitialCreationLog(korisnik, operater);
		
		Assert.assertEquals(korisnik.getUid().toString(),
							korisnikServiceImpln.dodajNovogKorisnika(korisnik));
	}
	
	@Test
	public void convertToSubscriberAndSendTest(){
		Mockito.doNothing().when(korisnikServiceImpl).convertToSubscriberAndSend(korisnik);
	}
	@Test
	public void addDevicesToKorisnikTest(){
		Uredjaj ur = getDummyuredjaj();
		final String deviceUid = ur.getMacAdresa();
		final String model = IME_MODELA_NA_PLATF;
		final String serialNo = ur.getSn();
		final String subscriberUid = korisnik.getUid();
		
		Mockito.doReturn(true).when(iptvService).addDevice(deviceUid, model, serialNo, subscriberUid);
		
		Assert.assertTrue(iptvService.addDevice(deviceUid, model, serialNo, subscriberUid));
	}
//	
	@Test
	public void preparePackagesForSubscriberTest(){
		Mockito.doReturn(getDummyPackageList()).when(iptvService).getAllPackages();

		Assert.assertArrayEquals(getDummyPackageUidList().toArray(), korisnikServiceImpln.preparePackagesForSubscriber(PAKETI_KORISNIKA).toArray());
	}
	@Test
	public void createUidWithFirstAndListNameTest(){
		Mockito.doReturn(0).when(korisnikDaoImpl).chcekNoSubscribersWithSameUid(korisnik.getUid());
		
		Assert.assertEquals(korisnik.getUid().toString(), 
						korisnikServiceImpln.createUidWithFirstAndListName(korisnik.getIme(), korisnik.getPrezime(), partner).toString());
		
	}
	@Test
	public void mergePackagesNamesTest(){
		Assert.assertEquals(PAKETI_KORISNIKA, korisnikServiceImpln.mergePackagesNames(IMENA_PAKETA));
	}
	
	/*	DUMMY OBJECTS	*/
	
	private Operater getDummyOpr(){
		Operater opr = new Operater();
		opr.setId(1);
		opr.setIme("Test");
		opr.setUsername("test");
		opr.setPassword("password");
		opr.setEmail("test@isp.beotel.net");
		opr.setPartner(partner);
		return opr;
	}	
	
	private OperaterDetails getDummyOprDetails(){
		OperaterDetails opd = new OperaterDetails(getDummyOpr());
		opd.setActive((byte) 0);
		return opd;
	}
	
	private Korisnik getDummyKorisnik(){
		Korisnik kor = new Korisnik();		
		kor.setIme("Test");
		kor.setPrezime("Testic");
		kor.setOperater(getDummyOpr());
		kor.setStatus("ACTIVE");
		kor.setUid("beotel_test.testic");
		kor.setUpdated_at(now);
		kor.setCreated_at(now);
		kor.setPaketi("Basic Package#Pink Package#BeoInfo#Arena234 Package#Pink XXX Package#BeoInfo");
		kor.setPartner(getDummyPartner());
		kor.setUredjaj(getDummyuredjaj());
		return kor;
	}
	
	private Partner getDummyPartner(){
		Partner partner = new Partner();
		partner.setId(1);
		partner.setPrefix("BEOTEL");
		partner.setName("Beotel");
		partner.setMaxNoOp(10);
		return partner;
	}
	
	private Subscriber getDummySubscriber(){
		Subscriber sub = new Subscriber();
		sub.setFirst_name("Test");
		sub.setLast_name("Testic");
		sub.setStatus("ACTIVE");
		sub.setUid("beotel_test.testic");
		sub.setCreated_at(now);
		sub.setUpdated_at(now);
		return sub;
	}
	
	private List<Package> getDummyPackageList(){
		List<Package> pkgSaPlatf = new ArrayList<>();
		Package basic = new Package();
		basic.setUid("BeoTV Basic");
		basic.setName("Basic Package");
		pkgSaPlatf.add(basic);
		
		Package arena = new Package();
		arena.setUid("Arena Sport 234");
		arena.setName("Arena234 Package");
		pkgSaPlatf.add(arena);
		
		Package pink = new Package();
		pink.setUid("Pink");
		pink.setName("Pink Package");
		pkgSaPlatf.add(pink);
		
		Package pinkXXX = new Package();
		pinkXXX.setUid("PinkXXX");
		pinkXXX.setName("Pink XXX Package");
		pkgSaPlatf.add(pinkXXX);
		
		Package beoInfo = new Package();
		beoInfo.setUid("BeoInfo");
		beoInfo.setName("BeoInfo");
		pkgSaPlatf.add(beoInfo);
		
		return pkgSaPlatf;
	}
	
	private List<String> getDummyPackageUidList(){
		List<String> raspak = new ArrayList<>();
		raspak.add(0, "BeoTV Basic");
		raspak.add(1, "Arena Sport 234");
		raspak.add(2, "Pink");
		raspak.add(3, "PinkXXX");
		raspak.add(4, "BeoInfo");
		return raspak;
	}
	
	private Uredjaj getDummyuredjaj(){
		Uredjaj dev = new Uredjaj();
		ModelUr modDev = new ModelUr();
		modDev.setId(1);
		modDev.setImeNaPlatformi(IME_MODELA_NA_PLATF);
		
		dev.setId(1);
		dev.setMacAdresa("01:02:9b:9c:0b:09");
		dev.setSn("002N-MUK0-1230-OP09");
		dev.setPartner(getDummyPartner());
		dev.setModel(modDev);
		dev.setStatus(1);
		return dev;
	}
}