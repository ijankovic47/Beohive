package net.beotel.onapp;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotacija koja cilja metod unutar klase koje se koristi. Ideja je da se
 * nacilja getter metod nekog onApp (wrapper) objekta unutar klase sa suffixom 'Wraper'. <br>
 * <br>
 * NPR. Unutar klase UserWrapper na metod getUser() postavljamo anotacije, time
 * naznacavamo da je klasa UserWraper omot za onApp klasu User, sto ce pomoci
 * onAppManageru da dohvati user iz omot klase UserWrapper
 * 
 * @author nenad
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface UnwrapElement {
	
}
