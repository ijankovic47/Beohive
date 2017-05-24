package net.beotel.dao;
import java.util.List;

import net.beotel.model.ModelUr;

public interface ModelDao {

	public List<ModelUr> getModels();
	public int insertModel(ModelUr model);
	public ModelUr getModel(int id);
	public void editModel(ModelUr model);
	public void deleteModel(int id);
}
