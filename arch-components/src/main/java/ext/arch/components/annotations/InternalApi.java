package ext.arch.components.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import ext.arch.components.model.HTTPResponse;
import ext.arch.components.model.Response;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface InternalApi {
    Class<? extends Response> value() default HTTPResponse.class;
}
