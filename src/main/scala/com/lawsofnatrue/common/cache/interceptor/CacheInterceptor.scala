package com.lawsofnatrue.common.cache.interceptor

import java.lang.reflect.{Method, Parameter}

import com.lawsofnatrue.common.cache.anno.{CacheKey, ServiceCache}
import com.lawsofnatrue.common.cache.enumeration.CacheMethod
import com.lawsofnature.common.redis.RedisClientTemplate
import org.aopalliance.intercept.{MethodInterceptor, MethodInvocation}
import org.slf4j.{Logger, LoggerFactory}

/**
  * Created by fangzhongwei on 2016/12/7.
  */
trait CacheInterceptor extends MethodInterceptor {

}

class CacheInterceptorImpl(redisClientTemplate: RedisClientTemplate) extends CacheInterceptor {
  val logger: Logger = LoggerFactory.getLogger(getClass)
  // CacheMethod, keyDir, keyParamIndex, valueParamIndex, expireSeconds
  var methodMap: scala.collection.mutable.Map[Method, (CacheMethod, String, Int, Class[_], Int)] = scala.collection.mutable.Map[Method, (CacheMethod, String, Int, Class[_], Int)]()

  def apply(redisClientTemplate: RedisClientTemplate): CacheInterceptorImpl = new CacheInterceptorImpl(redisClientTemplate)

  override def invoke(methodInvocation: MethodInvocation): AnyRef = {
    val method: Method = methodInvocation.getMethod
    val config: (CacheMethod, String, Int, Class[_], Int) =
      methodMap.get(method) match {
        case None => extractTuple4Cache(method)
        case Some(tuple) => tuple
      }

    val cacheMethod: CacheMethod = config._1
    val keyDir: String = config._2
    val keyParamIndex: Int = config._3
    val entityClass: Class[_] = config._4
    val expireSeconds: Int = config._5
    val key: String = new StringBuilder(keyDir).append(methodInvocation.getArguments()(keyParamIndex).toString).toString()

    cacheMethod match {
      case CacheMethod.SELECT =>
        redisClientTemplate.get(key, entityClass) match {
          case Some(result) =>
            result.asInstanceOf[AnyRef]
          case None =>
            val proceedResult: AnyRef = methodInvocation.proceed()
            if (proceedResult != null && proceedResult.getClass.eq(entityClass)) {
              redisClientTemplate.set(key, proceedResult, expireSeconds)
            }
            proceedResult
        }
      case CacheMethod.DELETE =>
        redisClientTemplate.delete(key)
        methodInvocation.proceed()
      case _ => methodInvocation.proceed()
    }
  }

  def extractTuple4Cache(method: Method): (CacheMethod, String, Int, Class[_], Int) = {
    val serviceCache: ServiceCache = method.getAnnotation[ServiceCache](classOf[ServiceCache])
    val cacheMethod: CacheMethod = serviceCache.method()
    val keyDir: String = serviceCache.keyDir()
    val expireSeconds: Int = serviceCache.expireSeconds()

    val parameters: Array[Parameter] = method.getParameters
    val entityType: Class[_] = method.getReturnType

    var keyParamIndex = -1
    keyParamIndex = parameters.zipWithIndex.filter(
      _._1.getAnnotation[CacheKey](classOf[CacheKey]) != null
    ).head._2

    methodMap += (method -> (cacheMethod, keyDir, keyParamIndex, entityType, expireSeconds))
    (cacheMethod, keyDir, keyParamIndex, entityType, expireSeconds)
  }
}
