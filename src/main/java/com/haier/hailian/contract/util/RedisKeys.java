
package com.haier.hailian.contract.util;


/**
 * Redis所有Keys
 **/
public class RedisKeys {

    public static String build(String identify,String bz,
                               ERedisKeyType keyType){
        return String.format("%s:%s:%s", identify,bz,keyType.toString());
    }
    public static String build(String identify,String bz){
        return build(identify,bz
        ,ERedisKeyType.STRING);
    }

    public enum ERedisKeyType {
        LIST,STRING,SET,ZSET
    }
}
