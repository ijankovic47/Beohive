package net.beotel.serviceImpl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import net.beotel.dao.CenovnikDao;
import net.beotel.dao.OperatorDao;
import net.beotel.model.Cenovnik;
import net.beotel.model.Operater;
import net.beotel.model.PartnerCenovnik;
import net.beotel.service.CenovnikService;

@Service
public class CenovnikServiceImpl implements CenovnikService {

	@Autowired
	CenovnikDao cenovnikDao;
	
	@Autowired
	OperatorDao operatorDao;
	

		@Override
		public void createCenovnik(PartnerCenovnik pc) {
			
			List<String> paketi=listaPaketa();
			List<Cenovnik> cenovnici=new ArrayList<>();
			
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			Operater operater=operatorDao.getOperatorByUsername(user.getUsername(), "username");
		
			
			Cenovnik o=new Cenovnik();
			Cenovnik c;
			o.setPaketi("");
			
			for(int i=0;i<pc.getTipovi().size();i++){
			
				if(pc.getTipovi().get(i)!=null){
				
				if(pc.getTipovi().get(i).equals("O")){
					
					o.setPaketi(o.getPaketi()+"#"+paketi.get(i));
				}
				else if(pc.getTipovi().get(i).equals("D")||pc.getTipovi().get(i).equals("P")){
					c=new Cenovnik();
					c.setPaketi(paketi.get(i));
					c.setCena(Double.valueOf(pc.getCene().get(i)));
					c.setDatumOd(LocalDateTime.now());
					c.setStatus(1);
					c.setOperater(operater);
					if(pc.getTipovi().get(i).equals("D")){
						c.setTip('d');
					}
					else{
						c.setTip('p');
					}
					
					cenovnici.add(c);
				}
			}}
			o.setCena(Double.valueOf(pc.getCenaOsn()));
			o.setDatumOd(LocalDateTime.now());
			o.setStatus(1);
			o.setTip('o');
			o.setPaketi(o.getPaketi().replaceFirst("#", ""));
			o.setOperater(operater);
			cenovnici.add(o);
			
			for(int i=0;i<pc.getKombinacije().size();i++){
				
				if(pc.getKombinacije().get(i).equals("")==false && Double.valueOf(pc.getKombinacije().get(i))>0){
					
					c=new Cenovnik();
					c.setCena(Double.valueOf(pc.getKombinacije().get(i)));
					c.setDatumOd(LocalDateTime.now());
					c.setPaketi(pc.getKombinacijeIme().get(i));
					c.setStatus(1);
					c.setTip('k');
					c.setOperater(operater);
					cenovnici.add(c);
				}
			}
			
			cenovnikDao.insertCenovnik(pc.getPartnerId(), cenovnici);
			
		}

		
		@Override
		public PartnerCenovnik getCenovnik(List<Cenovnik> cenovnici) {             //konvertuje cenovnike iz baze u cenovnik za prikaz
			
			PartnerCenovnik pc=new PartnerCenovnik();
			
			Cenovnik o=new Cenovnik();
			List<Cenovnik> d=new ArrayList<>();
			List<Cenovnik> k=new ArrayList<>();
			
			for(Cenovnik c:cenovnici){
				if(c.getTip()=='o'&&c.getStatus()==1){
					o=c;
				}
				else if((c.getTip()=='d'||c.getTip()=='p')&&c.getStatus()==1){
					d.add(c);
				}
				else if(c.getTip()=='k'&&c.getStatus()==1){
					k.add(c);
				}
			}
			List<String> listaPaketa=listaPaketa();
			List<String> cene=new ArrayList<>();
			pc.setOsnDate(o.getDatumOd());
			List<String> tipovi=new ArrayList<>(10);
			
			String[] osnovniPaket=o.getPaketi().split("#");
			
			for(int i=0;i<11;i++){
				tipovi.add(null);
				cene.add(null);
				pc.getDopDate().add(i,null);
			}
			
			for(String s:osnovniPaket){
				
				int index=listaPaketa.indexOf(s);
				tipovi.remove(index);
				tipovi.add(index, "O");
			}
			
			
			
			for(Cenovnik c:d){
				if(c.getTip()=='p'){
					
					int index=listaPaketa.indexOf(c.getPaketi());
					tipovi.remove(index);
					cene.remove(index);
					tipovi.add(index, "P");
					cene.add(index, String.valueOf(c.getCena()));
				    pc.getDopDate().remove(index);
				    pc.getDopDate().add(index, c.getDatumOd());
				}
				else{
				int index=listaPaketa.indexOf(c.getPaketi());
				tipovi.remove(index);
				cene.remove(index);
				tipovi.add(index, "D");
				cene.add(index, String.valueOf(c.getCena()));
			    pc.getDopDate().remove(index);
				pc.getDopDate().add(index, c.getDatumOd());
			}
			}
			for(Cenovnik c:k){
					
				pc.getKombinacijeIme().add(c.getPaketi());
				pc.getKombinacije().add(String.valueOf(c.getCena()));
				pc.getKomDate().add(c.getDatumOd());
			}
			
			pc.setTipovi((ArrayList<String>) tipovi);
			pc.setCenaOsn(String.valueOf(o.getCena()));
			//pc.setKombinacije(kombinacije);
			pc.setCene(cene);
			
			return pc;
		}

		@Override
		public void createKombinacije(List<String> kombinacije,Stack<String> buffer, String[] array, int start, int length) { //kreiranje svih mogucih kombinacija dp
			
		
			if (buffer.size() >= length) {
				return;
				} else {
				for (int i = start; i < array.length; i++) {
				buffer.push(array[i]);
				if (buffer.size() == length) {
					String add="";
				for(String s:buffer){
					add+="+"+s;
				}
				add=add.replaceFirst("\\+", "");
				kombinacije.add(add);
				}
				createKombinacije(kombinacije,buffer, array, i + 1, length);
				buffer.pop();
				}
				}
				}
			
		//pravi sve moguce kombinacije od unetok niza stringova i pakuje u listu (nesortirano)
		
		public List<String> listaPaketa(){    //lista postojecih paketa
      	  
			
        	List<String> paketi=new ArrayList<>();
      		paketi.add("Basic Package");
      		paketi.add("Pink Package");
      		paketi.add("BeoInfo");
      		paketi.add("Arena234 Package");
      		paketi.add("Pink XXX Package");
      		paketi.add("Basic Catchup");
      		paketi.add("Basic Timeshift");
      		paketi.add("Basic Recorder");
      		paketi.add("SRBNetInfo");
      		paketi.add("Test paket");
      		paketi.add("Basic Live");
      		
      		return paketi;
          }
		public Map<String,String> mapaPaketa(){             //za komunikaciju sa platformom
			
			Map<String, String> mapa=new HashMap<>();
			
			mapa.put("Basic Package", "BeoTV Basic");
			mapa.put("Pink Package", "Pink");
			mapa.put("BeoInfo", "BeoInfo");
			mapa.put("Arena234 Package", "Arena Sport 234");
			mapa.put("Pink XXX Package", "PinkXXX");
			mapa.put("Basic Catchup", "basiccatchup");
			mapa.put("Basic Timeshift", "basictimeshift");
			mapa.put("Basic Recorder", "basicrecorder");
			mapa.put("SRBNetInfo", "SRBInfo");
			mapa.put("Test paket", "TestPaket");
			mapa.put("Basic Live", "basiclive");
			
			return mapa;
		}
			 //vraca listu svih paketa
		
			 public String[] getDoplata(PartnerCenovnik pc){    //pravi se lista doplatnih kanala
				 
					List<String> doplatniPaketi=new ArrayList<>();
					List<String> listaPaketa=listaPaketa();
					
					for(int i=0;i<pc.getTipovi().size();i++){
						if(pc.getTipovi().get(i)!=null){
						if(pc.getTipovi().get(i).equals("D") || pc.getTipovi().get(i).equals("P")){
							
							doplatniPaketi.add(listaPaketa.get(i));
						}
					}
					}
					String[] niz=new String[doplatniPaketi.size()];
					int i=0;
					for(String s:doplatniPaketi){
						niz[i]=s;
						i++;
					}
					return niz;
			 }
              //iz objekta uzima samo doplatne pakete i smesta ih u niz
			@Override
			public List<List<String>> istorija(List<Cenovnik> cenovnici, char tip) {  //kreira se istorija cenovnika na osnovu 'o', 'd' ili 'k'
				
				List<Cenovnik> osnovniCenovnici=new ArrayList<>();
				List<List<String>> prikaz=new ArrayList<>();
				
				if(tip=='d'){
					for(Cenovnik c:cenovnici){
						if((c.getTip()=='d'||c.getTip()=='p') && c.getStatus()==0){
						osnovniCenovnici.add(c);
						}
					}
				}
				else{
					
					for(Cenovnik c:cenovnici){
						if(c.getTip()==tip && c.getStatus()==0){
						osnovniCenovnici.add(c);
						}
					}
				}
					
				osnovniCenovnici=sortCenovnici(osnovniCenovnici);
				
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyy HH:mm");
				for(int i=0;i<osnovniCenovnici.size();i++){
					prikaz.add(i, new ArrayList<String>());
					prikaz.get(i).add(0, osnovniCenovnici.get(i).getPaketi());
					prikaz.get(i).add(1, String.valueOf(osnovniCenovnici.get(i).getCena()));
					prikaz.get(i).add(2, osnovniCenovnici.get(i).getOperater().getIme()+" "
					+osnovniCenovnici.get(i).getOperater().getPrezime()+ "("+ osnovniCenovnici.get(i).getOperater().getPartner().getPrefix()+")");
					
					prikaz.get(i).add(3, osnovniCenovnici.get(i).getDatumOd().format(formatter));
					prikaz.get(i).add(4, osnovniCenovnici.get(i).getDatumDo().format(formatter));
					
					if(tip=='d'){
						if(osnovniCenovnici.get(i).getTip()=='d'){
							prikaz.get(i).add(5, "Ne");
						}
						else{
							prikaz.get(i).add(5, "Da");
						}
						
					}
					
				}
				
				return prikaz;
			}
		
			
			public String[] osnovniPaket(PartnerCenovnik pc){   //pravi se prikaz aktuelnog osnovnog paketa
			
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyy HH:mm");
				List<String> paketi=listaPaketa();
				String[] op={"","",""};
				for(int i=0;i<pc.getTipovi().size();i++){
					
					if(pc.getTipovi().get(i)!=null && pc.getTipovi().get(i).equals("O")){
						op[0]+="#"+paketi.get(i);
					}

				}
				
				op[0]=op[0].replaceFirst("#", "");
				op[1]=pc.getCenaOsn();
				if(pc.getOsnDate()!=null){
				op[2]=pc.getOsnDate().format(formatter);
				}
				
				return op;
			}
			public List<String[]> doplatniPaketi(PartnerCenovnik pc){  //pravi se lista aktuelnih doplatnih kanala
				
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyy HH:mm");
				List<String> paketi=listaPaketa();
				List<String[]> dp=new ArrayList<>();
				String[] op={"","",""};
                for(int i=0;i<pc.getTipovi().size();i++){
					
                	if(pc.getTipovi().get(i)!=null){
                	
					if(pc.getTipovi().get(i).equals("D") || pc.getTipovi().get(i).equals("P")){
						op=new String[4];
						op[0]=paketi.get(i);
						op[1]=pc.getCene().get(i);
						
						if(!pc.getDopDate().isEmpty()){
							op[2]=pc.getDopDate().get(i).format(formatter);
						}
						
						if(pc.getTipovi().get(i).equals("D")){
							op[3]="Ne";
						}
						else{
							op[3]="Da";
						}
						dp.add(op);
					}
                	}
				}
                return dp;
			}
			public List<String[]> kombinacije(PartnerCenovnik pc){   //pravi se lista aktuelnih kombinacija
				
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyy HH:mm");
				List<String[]> kombinacije=new ArrayList<>();
				String[] s;
				for(int i=0;i<pc.getKombinacije().size();i++){
					
					s=new String[3];
					s[0]=pc.getKombinacijeIme().get(i);
					s[1]=pc.getKombinacije().get(i);
					s[2]=pc.getKomDate().get(i).format(formatter);
					kombinacije.add(s);
				}
				return kombinacije;
			}
private List<Cenovnik> sortCenovnici(List<Cenovnik> cenovnici){   //bubble sort za sortiranje istorije po datumima (od najskorije promene)
				
				boolean a=true;
				Cenovnik pom;
				while(a){
					a=false;
					for(int i=0;i<cenovnici.size()-1;i++){
						if(cenovnici.get(i).getDatumDo().isBefore(cenovnici.get(i+1).getDatumDo())){
							
							a=true;
							pom=cenovnici.get(i);
							cenovnici.set(i, cenovnici.get(i+1));
							cenovnici.set(i+1, pom);
						}
					}
				}
				
				
				return cenovnici;
			}
}
