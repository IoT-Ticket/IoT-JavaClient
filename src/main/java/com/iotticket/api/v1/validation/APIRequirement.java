package com.iotticket.api.v1.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface APIRequirement {

    public static final int UNRESTRICTED = -1;

    int maxLength() default UNRESTRICTED;

    String regexPattern() default "";

    boolean nullable() default true;


}
