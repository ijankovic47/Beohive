package net.beotel.dao;

import java.util.List;

import net.beotel.model.Operater;

public interface OperatorDao {

	public List<Operater> getAllOperators();
	public void addNewOperator(Operater operater);
	public Operater getOperatorById(int id);
	public void updateOperator(Operater updOperater);
	public void deleteOperator(int id);
	public Operater getOperatorByUsername(String value, String field);
	public List<Operater> getAllOperatersForPartner(int partId);
	public List<Operater> getAllOperatersByPartnerPrefix(String prefix);
	public void changePasswordForOpr(int id, String password);
}
