package net.beotel.services;

import java.util.InputMismatchException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.beotel.dao.OperatorDao;

@Service
public class OperaterService {
	
	@Autowired
	private OperatorDao operatorDaoImpl;

	
	public void proveriPoklapanjeLozinke(int id, String pass1, String pass2){
		if(pass1.equals(pass2)){
			operatorDaoImpl.changePasswordForOpr(id, pass1);
		}else{
			throw new InputMismatchException("Unete lozinke se ne poklapaju");
		}			
	}
}
