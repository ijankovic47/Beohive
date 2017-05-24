package net.beotel.aop;

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import java.util.List;

import net.beotel.model.OperaterDetails;

@Component
@Aspect
public class ApplicationLogging2 {

	private static final Logger LOG = Logger.getLogger(ApplicationLogging.class);
	
	@AfterReturning(pointcut="execution(public * net.beotel.controllers.*.*(..)) "
			+ "&& !execution(public String net.beotel.controllers.*.*(..))"
			+ "&& !execution(public void net.beotel.controllers.*.*(..))"
			+ "&& !execution(public org.springframework.http.ResponseEntity<*> net.beotel.controllers.*.*(..))",
			returning="result")
	public void afterReturning(JoinPoint point , Object result){
		

		Object[] args=point.getArgs();
		String args1="";
		for(Object o:args){
			args1+=", "+o;
		}
		args1=args1.replaceFirst(",", "");
		OperaterDetails opr = getOperaterFromSecContext();
		LOG.info("["+opr.getIme()+" "+opr.getPrezime()+"] pozivam "+point.getSignature()+" args:"+args1+" result:"+result.toString());
	}
	
	@AfterReturning(pointcut="execution(public * net.beotel.serviceImpl.*.*(..))"
			+"&& !execution(public * net.beotel.serviceImpl.CenovnikServiceImpl.createKombinacije(..))"
			+"&& !execution(public java.util.List<String[]> net.beotel.serviceImpl.CenovnikServiceImpl.*(..))",
			returning="result")
	public void afterReturning1(JoinPoint point , Object result){
		

		OperaterDetails opr = getOperaterFromSecContext();
		Object[] args=point.getArgs();
		String res="";
		String args1="";
		for(Object o:args){
			args1+=", "+o;
		}
		args1=args1.replaceFirst(",", "");
		if(result!=null){
		if(result.getClass().isArray()){
			Object[] niz=(Object[]) result;
			res=Arrays.deepToString(niz);
			LOG.info("["+opr.getIme()+" "+opr.getPrezime()+"] pozivam "+point.getSignature()+" args:"+args1+" result:"+res);
		}
		else{
			LOG.info("["+opr.getIme()+" "+opr.getPrezime()+"] pozivam "+point.getSignature()+" args:"+args1+" result:"+result.toString());
		}}
		else{
			LOG.info("["+opr.getIme()+" "+opr.getPrezime()+"] pozivam "+point.getSignature()+" args:"+args1);
		}
		
	}
	@AfterReturning(pointcut="execution(public java.util.List<String[]> net.beotel.serviceImpl.CenovnikServiceImpl.*(..))",
			returning="result")
	public void afterReturnig2(JoinPoint point , Object result){
		
		OperaterDetails opr = getOperaterFromSecContext();
		Object[] args=point.getArgs();
		String res="";
		String pom="";
		String args1="";
		for(Object o:args){
			args1+=", "+o;
		}
		List<String[]> r=(List<String[]>) result; 
		for(String[] s:r){
			pom="[";
			
		   for(int i=0;i<s.length;i++){
			   pom+=", "+s[i];
		   }
		   pom+="]";
		   pom=pom.replaceFirst(", ", "");
		   res+=" "+pom;
		}
		LOG.info("["+opr.getIme()+" "+opr.getPrezime()+"] pozivam "+point.getSignature()+" args:"+args1+" result:"+res);
	}
	
	
	private static OperaterDetails getOperaterFromSecContext(){
		OperaterDetails opr = (OperaterDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return opr;
	}
}
