package net.beotel.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import net.beotel.model.ModelUr;
import net.beotel.model.Operater;
//import net.beotel.dao.UserDao;
//import net.beotel.dao.impl.UserDaoImpl;
import net.beotel.model.Partner;


@Configuration
@ComponentScan(basePackages="net.beotel")
@EnableTransactionManagement
public class HibernateConfiguration {

	@Bean(name="dataSource")
	public DataSource getDataSource(){
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName("oracle.jdbc.OracleDriver");
		dataSource.setUrl("jdbc:oracle:thin:@localhost:1521:orcl");
		dataSource.setUsername("beohive");
		dataSource.setPassword("Jupiterc1");
		return dataSource;		
	}
	

//   CREATE SEQUENCE  "BEOHIVE"."HIBERNATE_SEQUENCE"  MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 41 CACHE 20 NOORDER  NOCYCLE ;
	 	
	@Autowired
	@Bean(name="sessionFactory")
	public SessionFactory getSessionFactory(DataSource dataSource){
		LocalSessionFactoryBuilder sessionFactory = new LocalSessionFactoryBuilder(dataSource);
	//	sessionFactory.addAnnotatedClass(Partner.class).addAnnotatedClass(Operater.class).addAnnotatedClass(ModelUr.class);
		sessionFactory.scanPackages("net.beotel.model");
		sessionFactory.addProperties(getHibernateProperties());
		return sessionFactory.buildSessionFactory();
	}
	
	@Autowired
	@Bean(name="transactionManager")
	public HibernateTransactionManager getTransactionManager(SessionFactory sessionFactory){
		HibernateTransactionManager txManager = new HibernateTransactionManager(sessionFactory);
		return txManager;
	}
	
//	@Autowired
//	@Bean(name="userDaoImpl")
//	public UserDao getUserDao(SessionFactory sessionFactory){
//		UserDaoImpl userDao = new UserDaoImpl(sessionFactory);
//		return userDao;
//	}	
	
	private Properties getHibernateProperties(){
		Properties hibernateProperties = new Properties();
		hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.Oracle10gDialect");
//		hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "update");
		return hibernateProperties;	
	}
}