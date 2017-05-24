/**
 * 
 */
package net.beotel.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import net.beotel.dao.ModelDao;
import net.beotel.dao.OperatorDao;
import net.beotel.dao.PartnersDao;
import net.beotel.dao.UredjajDao;
import net.beotel.model.Operater;
import net.beotel.model.Partner;
import net.beotel.model.Uredjaj;

/**
 * @author ivan *
 */
@Controller
@RequestMapping(value="/uredjaj")
public class UredjajController {
	
	private static final String XLSX_FILE = "uredjaji.xlsx";
	
	@Autowired
	private PartnersDao partnersDaoImpl;
	@Autowired
	private  UredjajDao uredjajDaoImpl;
	@Autowired
	private ModelDao modelDao;
	
	@Autowired
	private OperatorDao operatorDao;
	
	private boolean registracija=false;
	
	private List<Uredjaj> uredjaji=new ArrayList<>();
	
		

	@ModelAttribute
	public void populateDevices(Model model){
		model.addAttribute("uredjaj", new Uredjaj());	
		model.addAttribute("partneri", partnersDaoImpl.getPartners());
		model.addAttribute("modeli", modelDao.getModels());
		if(uredjaji.isEmpty()){
			model.addAttribute("uredjaji", uredjajDaoImpl.getSlobodneUredjaje(getPartnerId()));
		}
		else{
			model.addAttribute("uredjaji", uredjaji);
			uredjaji=new ArrayList<>();
		}
		model.addAttribute("registracija", registracija);
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public String prikaziUredjaje(Model model){
		
		//model.addAttribute("uredjaji", uredjajDaoImpl.getSlobodneUredjaje());
		
		registracija=false;
		
		return "uredjaji";
	}
	
@RequestMapping(value="/add", method=RequestMethod.POST)
	public String dodajNoveUredjaje(Model model, @Valid @ModelAttribute("uredjaj") Uredjaj uredjaj, BindingResult bs, @RequestParam CommonsMultipartFile[] file){
	
	if(bs.hasErrors()){
		model.addAttribute("errors", bs.getErrorCount());
		model.addAttribute("uredjaji", uredjajDaoImpl.getSlobodneUredjaje(getPartnerId()));
		
		return "uredjaji";
	}
	
	List<String[]> lista=new ArrayList<>();
	
	if(file[0].isEmpty()){
		
		model.addAttribute("poruka", "Niste odabrali fajl !");
		model.addAttribute("uredjaji", uredjajDaoImpl.getSlobodneUredjaje(getPartnerId()));
		
		return "uredjaji";
	}
	
			try
			{
			
				//FileInputStream file = new FileInputStream(new File(url));
				//Create Workbook instance holding reference to .xlsx file
				XSSFWorkbook workbook = new XSSFWorkbook(file[0].getInputStream());

				//Get first/desired sheet from the workbook
				XSSFSheet sheet = workbook.getSheetAt(0);

				//Iterate through each rows one by one
				Iterator<Row> rowIterator = sheet.iterator();
				rowIterator.next();
				
				
				String[] niz;
				while (rowIterator.hasNext()) 
				{
					Row row = rowIterator.next();
					//For each row, iterate through all the columns
					Iterator<Cell> cellIterator = row.cellIterator();
					
					niz=new String[2];
				
					
					int i=0;
					while (i<2) {
						
						Cell cell = cellIterator.next();
						//Check the cell type and format accordingly
						switch (cell.getCellType()) 
						{
							/**case Cell.CELL_TYPE_NUMERIC:
								System.out.print((int)cell.getNumericCellValue() + "\t");
								break;**/
							case Cell.CELL_TYPE_STRING:
								niz[i]=cell.getStringCellValue();
								
								break;
						}
						i++;
					}
					lista.add(niz);
				}
			} 
			catch (Exception e) 
			{
				model.addAttribute("poruka", "Uneli ste los format fajla !");
				model.addAttribute("uredjaji", uredjajDaoImpl.getSlobodneUredjaje(getPartnerId()));
				
				return "uredjaji";
			}
			try{
			
				uredjaji= uredjajDaoImpl.dodajNoveUredjaje(uredjaj, lista);
			//model.addAttribute("uredjaji", uredjajDaoImpl.dodajNoveUredjaje(uredjaj, lista));
			
			registracija=true;
			}
			catch(Exception e){
				
				model.addAttribute("dupliPodaci", true);
				String message=e.getMessage();
				int snMac;
				if(message.charAt(0)=='s'){
					snMac=1;
				}
				else{
					snMac=2;
				}
				model.addAttribute("snMac", snMac);
				model.addAttribute("iter", Integer.valueOf(message.substring(1)));
				model.addAttribute("uredjaji", uredjajDaoImpl.getSlobodneUredjaje(getPartnerId()));
				
				return "uredjaji";
			}
			
			return "redirect:/uredjaj";
	
		}


	@RequestMapping(value="/getUredjaj", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public Uredjaj getUredjaj(@RequestParam("id") int id){
		
		Uredjaj u=uredjajDaoImpl.getUredjaj(id);
		if(u.getPartner()==null){
			Partner p=new Partner();
			p.setId(0);
			u.setPartner(p);
		
		}

		return u;
	}
	@RequestMapping(value="/updateUredjaj", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_UTF8_VALUE, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public ResponseEntity<String> updateUredjaj(@Valid @RequestBody Uredjaj uredjaj, BindingResult bindingResult){
		
		if(bindingResult.hasErrors()){
			if(bindingResult.hasFieldErrors("sn")){
				return new ResponseEntity<String>("Serijski broj mora sadrzati izmedju 5 i 20 karaktera", HttpStatus.INTERNAL_SERVER_ERROR);
			}
			if(bindingResult.hasFieldErrors("macAdresa")){
				return new ResponseEntity<String>("MAC adresa mora sadrzati izmedju 5 i 20 karaktera", HttpStatus.INTERNAL_SERVER_ERROR);
			}
			if(bindingResult.hasFieldErrors("model")){
				return new ResponseEntity<String>("Model mora biti izabran", HttpStatus.INTERNAL_SERVER_ERROR);
			}
			if(bindingResult.hasFieldErrors("partner")){
				return new ResponseEntity<String>("Partner mora biti izabran", HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
	try{
		uredjajDaoImpl.updateUredjaj(uredjaj);
			return new ResponseEntity<String>("Uredjaj uspesno izmenjen",HttpStatus.OK);
		}
		catch(Exception e){
			if(e.getMessage().equals("1")){
				return new ResponseEntity<String>("Vec postoji uredjaj sa tim serijskim brojem !", HttpStatus.INTERNAL_SERVER_ERROR);
			}
			else{
				return new ResponseEntity<String>("Vec postoji uredjaj sa tom MAC adresom !", HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
		}
		
	}
	@RequestMapping(value="/delete", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
    public ResponseEntity<String> delUredjaj(@RequestParam("id") int id){
   
		try{
			uredjajDaoImpl.delUredjaj(id);
			return new ResponseEntity<String>("Uredjaj uspesno obrisan",HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<String>("Greska pri brisanju! Uredjaj nije obrisan.", HttpStatus.INTERNAL_SERVER_ERROR);
		}	
    }
	@RequestMapping("/filter")
	public String filter(@RequestParam("pid") int partnerId, @RequestParam("mid") int modelId, Model model){
		
		if(getPartnerId()!=1503){                  //u slucaju unosa pid a preko urla na kvarno
			partnerId=getPartnerId();
		}
		List<Uredjaj> uredjajiPom=new ArrayList<>();
		uredjajiPom=uredjajDaoImpl.getFilter(partnerId, modelId);
	    model.addAttribute("uredjaji", uredjajiPom);
		
		return "uredjaji";
	}
	/*
	 * 	Metoda koja pravi IO File stream za preuzimanje xlsx fajla sa classpatha našeg projekta
	 * */
	@RequestMapping(value="/download")
    public void downloadFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
		File downloadFile = null;
		
		ServletContext context = request.getServletContext();
        String appPath = context.getRealPath("");
		
     // construct the complete absolute path of the file
        String fullPath = appPath + "resources/downloads/"+XLSX_FILE;      
        downloadFile = new File(fullPath);
        FileInputStream inputStream = new FileInputStream(downloadFile);

        // get MIME type of the file
        String mimeType = context.getMimeType(fullPath);
        if (mimeType == null) {
            // set to binary type if MIME mapping not found
            mimeType = "application/octet-stream";
        }
        
        // set content attributes for the response
        response.setContentType(mimeType);
        response.setContentLength((int) downloadFile.length());
 
        // set headers for the response
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
        response.setHeader(headerKey, headerValue);
 
        // get output stream of the response
        OutputStream outStream = response.getOutputStream();
 
        byte[] buffer = new byte[4096];
        int bytesRead = -1;
 
        // write bytes read from the input stream into the output stream
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, bytesRead);
        }
 
        inputStream.close();
        outStream.close();         
	}	
	
	public int getPartnerId(){
		
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Operater operater=operatorDao.getOperatorByUsername(user.getUsername(), "username");
		
		return operater.getPartner().getId();
	}
	public Operater getOperater(){
		
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Operater operater=operatorDao.getOperatorByUsername(user.getUsername(), "username");
		
		return operater;
	}

}