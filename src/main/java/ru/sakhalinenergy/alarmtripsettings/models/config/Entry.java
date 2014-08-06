package ru.sakhalinenergy.alarmtripsettings.models.config;

import java.lang.annotation.ElementType;  
import java.lang.annotation.Target;  
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * Annotation for configuration classes settings fields.
 * 
 * @author Denis Udovenko
 * @version 1.0.2
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Entry {}
