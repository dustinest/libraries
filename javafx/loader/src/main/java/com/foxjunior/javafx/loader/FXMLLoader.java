package com.foxjunior.javafx.loader;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FXMLLoader {
	boolean controller() default true;
	boolean root() default true;
	String fileName() default "";
}
