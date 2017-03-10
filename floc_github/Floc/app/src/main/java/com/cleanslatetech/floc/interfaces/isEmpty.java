package com.cleanslatetech.floc.interfaces;

import com.cleanslatetech.floc.utilities.FormValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by pimpu on 3/10/2017.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface isEmpty {

    boolean value() default true;

    String errorMsg();

    FormValidator.DisplayType errorDisplayType();
}
