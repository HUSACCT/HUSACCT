package presentation.annotations;

import java.lang.annotation.Documented;

@Documented
public @interface Copyright {
	public String author() default "Themaopdracht 7 tester";
	public String version() default "0.1";
	public String created() default "2012";
}