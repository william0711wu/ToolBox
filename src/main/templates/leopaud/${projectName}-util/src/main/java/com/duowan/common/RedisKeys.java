package com.duowan.common;

/**
 *
 */
public class RedisKeys {

    public static String getKey(String template,Object...params){
      return  String.format(template,params);
    }
}
