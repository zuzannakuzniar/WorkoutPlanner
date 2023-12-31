package com.zkuzniar.workoutplanner.client;

import java.lang.annotation.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.core.annotation.AliasFor;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@FeignClient
public @interface AuthorizedFeignClient {
    @AliasFor(annotation = FeignClient.class, attribute = "name")
    String name() default "";

    @AliasFor(annotation = FeignClient.class, attribute = "configuration")
    Class<?>[] configuration() default OAuth2InterceptedFeignConfiguration.class;

    String url() default "";

    boolean decode404() default false;

    Class<?> fallback() default void.class;

    String path() default "";
}
