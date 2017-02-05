package com.lawsofnatrue.common.cache.anno;

import com.lawsofnatrue.common.cache.enumeration.CacheMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by fangzhongwei on 2016/12/7.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ServiceCache {
    CacheMethod method();
    String keyDir() default "";
    int expireSeconds() default 3600;
}
