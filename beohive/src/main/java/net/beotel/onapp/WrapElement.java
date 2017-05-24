package net.beotel.onapp;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotacija koja cilja metod unutar klase koje se koristi. Ideja je da se
 * nacilja setter metod nekog onApp (wrapper) objekta unutar klase sa suffixom 'Wraper'. <br>
 * <br>
 * NPR. Unutar klase UserWrapper na metod setUser() postavljamo anotacije, time
 * naznacavamo da je klasa UserWraper omot za onApp klasu User, sto ce pomoci
 * onAppManageru da omota user-a
 * 
 * @author nenad
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface WrapElement {

}
