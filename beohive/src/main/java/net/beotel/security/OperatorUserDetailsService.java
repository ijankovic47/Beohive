/**
 * 
 */
package net.beotel.security;

import java.util.ArrayList;
import java.util.List;

import javax.jws.soap.SOAPBinding.Use;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import net.beotel.dao.OperatorDao;
import net.beotel.model.Operater;
import net.beotel.model.OperaterDetails;

/**
 * @author nemanja
 *
 */
@Service("operatorUserDetailsService")
public class OperatorUserDetailsService implements UserDetailsService{
	
	@Autowired
	private OperatorDao operatorDaoImpl;
	private String field;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		/*
		 * 	Proverava prosleÄ‘eni strinh username, da li sadrÅ¾i @ karakter.
		 * ako sadrzi znaÄ�i da je korisnik uneo mail adresu kao kredencijal, u suprotnom uneo je svoje korisniÄ�ko ime
		 * Na osnovu toga promenljiva field se setuje na email/username vrednosti koje Ä‡e se setovati u HQL query-ju
		 * */
			if(username.contains("@")){
				field = "email";
			}else{
				field = "username";
			}
			Operater opr = operatorDaoImpl.getOperatorByUsername(username, field);
			
			System.out.println(opr);
			if(opr==null || opr.getActive()==0){
				throw new UsernameNotFoundException("Operater nije pronaÄ‘en");
			}			
			OperaterDetails oprDetails = new OperaterDetails(opr);			
				
		return oprDetails;
	}
}