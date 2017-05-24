package net.beotel.aop;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import net.beotel.model.Operater;
import net.beotel.model.OperaterDetails;

@Component
@Aspect
public class ApplicationLogging {
	
	private static final Logger LOG = Logger.getLogger(ApplicationLogging.class);
	
	@Before("execution(public String net.beotel.controllers.*.*(..)) "
			+ "&& !execution(public * net.beotel.controllers.HomeController.*(..))"
			+ "&& !execution(public * net.beotel.controllers.LoginController.*(..))")
	public void beforeCall(JoinPoint point){
		
		Object[] args=point.getArgs();
		String args1="";
		for(Object o:args){
			args1+=", "+o;
		}
		args1=args1.replaceFirst(",", "");
		OperaterDetails opr = getOperaterFromSecContext();
		LOG.info("["+opr.getIme()+" "+opr.getPrezime()+"] pozivam "+point.getSignature()+" args:"+args1);
	}
	
	@AfterThrowing(pointcut = "execution(public * net.beotel.controllers.*.*(..))", throwing="ex")
	public void afterControllerMethodsThrows(JoinPoint point, Exception ex){		
		LOG.error("Greska na metodi "+point.getSignature().toShortString()+" "+ex.getMessage());
	}
			
	@Around("execution(public String net.beotel.controllers.OperaterController.showAllOprsForPartner(..))")
	public String logShowAllOprsForPartner(ProceedingJoinPoint point){
		OperaterDetails opr = getOperaterFromSecContext();		
		String result = null;
		try {
			result = (String) point.proceed();
			LOG.info("["+opr.getIme()+" "+opr.getPrezime()+"] Prikazujem sve operatere za partnera "+opr.getPartner().getName());
		} catch (Throwable e) {
			LOG.error("Greska pri prikazu svih operatera ");
		}		
		return result;
	}
		
	@Around("execution(public String net.beotel.controllers.HomeController.successLoginRedirect(..))")
	public String logAfterSuccessLogin(ProceedingJoinPoint point){
		String view = null;
		try {		
			Object[] args = point.getArgs();
			HttpServletRequest request = (HttpServletRequest) args[0];
			OperaterDetails opr = getOperaterFromSecContext();
			view = (String) point.proceed();
			LOG.info("Operater: "+opr.getIme()+" "+opr.getPrezime()+" uspesno prijavljen na sistem sa IP Adrese: "+request.getRemoteAddr());
		} catch (Throwable e) {
			LOG.error("Neuspesan pokusaj prijavljivanja operatera na sistem!");
			e.printStackTrace();
		}		
		return view;
	}
	/*
	 * 	 Logovanje metoda na Controller-ima koje za povratni tip imaju ResponseEntity<*>
	 * */	
	@AfterReturning(pointcut="execution(public org.springframework.http.ResponseEntity<*> net.beotel.controllers.*.*(..)) ",
					returning="result")	
	public void logResponseEntityreturnTypeMethods(JoinPoint joinPoint , Object result){
		@SuppressWarnings("unchecked")
		ResponseEntity<Object> response = (ResponseEntity<Object>) result;	
		final int status = response.getStatusCodeValue();
		
		Object obj;
		OperaterDetails opr = getOperaterFromSecContext();
		final String oprImePre = "["+opr.getIme()+" "+opr.getPrezime()+"]";		
		
		LOG.info(oprImePre+" pozivam "+joinPoint.getSignature()+" HTTP Status: "+status);
		if(response.getBody().getClass().isArray()){
			Object[] nizObj = (Object[]) response.getBody();			
			for(int i=0; i<nizObj.length; i++)
				LOG.info(oprImePre+" metoda "+joinPoint.getSignature().toShortString()+" RETURN: "+i+". "+nizObj[0].toString());
		}else{
			 obj = response.getBody();
			 LOG.info(oprImePre+" metoda "+joinPoint.getSignature().toShortString()+" RETURN: "+obj.toString());
		}
	}
	
	private static OperaterDetails getOperaterFromSecContext(){
		OperaterDetails opr = (OperaterDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return opr;
	}
}
