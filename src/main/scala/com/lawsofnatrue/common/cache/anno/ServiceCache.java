package com.lawsofnatrue.common.cache.anno;

import com.lawsofnatrue.common.cache.enumeration.CacheMethod;

/**
 * Created by fangzhongwei on 2016/12/7.
 */
public @interface ServiceCache {
    CacheMethod method();
    String keyDir() default "";
    int expireSeconds() default 3600;
}
