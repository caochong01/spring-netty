package com.autumn.annotation;

import java.lang.annotation.*;

/**
 * Annotation which indicates that a method parameter should be bound to a URI template
 * variable.
 *
 * <p>If the method parameter is {@link java.util.Map Map&lt;String, Object&gt;}
 * then the map is populated with all path variable names and values.
 *
 * @author caochong
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PathVariable {

    /**
     * The name of the path variable to bind to.
     */
    String value();

    /**
     * Whether the path variable is required.
     * <p>Defaults to {@code true}, leading to an exception being thrown if the path
     * variable is missing in the incoming request. Switch this to {@code false} if
     * you prefer a {@code null} or Java 8 {@code java.util.Optional} in this case.
     * e.g. on a {@code ModelAttribute} method which serves for different requests.
     */
    boolean required() default true;

}