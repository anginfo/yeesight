package com.hailian.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.hailian.common.TokenConstants;
import com.hailian.enums.PlatformType;
import com.hailian.redis.RedisUtils;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
/**
 * 
 * @time   2018年9月28日 下午8:45:46
 * @author zuoqb
 * @todo   Token信息
 */
public class JWTUtil {

    // 过期时间1天
    private static final long EXPIRE_TIME = 10*1000;

    /**
     * 校验token是否正确
     * @param token 密钥
     * @param secret 用户的密码
     * @return 是否正确
     */
    public static boolean verify(String token, String userId, String secret) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim(TokenConstants.JWT_KEY, userId)
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     * @return token中包含的用户名
     */
    public static String getUserId(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim(TokenConstants.JWT_KEY).asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 生成签名,5min后过期
     * @param username 用户名
     * @param secret 用户的密码
     * @return 加密的token
     */
    public static String createToken(String userId, String secret) {
        try {
            Date date = new Date(System.currentTimeMillis()+EXPIRE_TIME);
            Algorithm algorithm = Algorithm.HMAC256(secret);
            // 附带user编码信息
            return JWT.create()
                    .withClaim(TokenConstants.JWT_KEY, userId)
                    .withExpiresAt(date)
                    .sign(algorithm);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }
    public static String returnSign(String message) {
    	String hash = "";
    	//别人篡改数据，但是签名的密匙是在服务器存储，密匙不同，生成的sign也不同。
    	//所以根据sign的不同就可以知道是否篡改了数据。
    	String secret = "mystar";//密匙
    	try {
    		Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
    		SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(),"HmacSHA256");
    		sha256_HMAC.init(secret_key);
    		hash = Base64.encodeBase64String(sha256_HMAC.doFinal(message.getBytes()));
    		System.out.println(message+"#####"+hash);
    	} catch (Exception e) {
    		System.out.println("Error");
    	}
    		return hash;
    	}
    /**
     * 生成token
     * @param userId
     * @param platform
     * @param secret:用户md5密码
     * @return
     */
    public static String generateToken(String userId,String secret, PlatformType platform){
        String token = JWTUtil.createToken(userId.toString(), secret);

        String redisKey = TokenConstants.LOGIN_USER_KEY.concat(String.valueOf(userId));
/*
        Object object = RedisUtils.get(redisKey, platform.getPlatform());

        if(object != null){
            RedisUtils.remove(object.toString());
        }
        *//** 永久记忆 **//*
        RedisUtils.put(redisKey, platform.getPlatform(), token, -1);

        *//** 存放token **//*
        RedisUtils.put(TokenConstants.CURRENT_LOGIN_TOKEN, token, String.valueOf(userId), TokenConstants.TOKEN_EXPIRES_TIME);
*/
        return token;
    }


    /**
     * 删除token 重置密码/找回密码
     * @param userId
     */
    public static void deleteToken(Object userId){
        String redisKey = TokenConstants.LOGIN_USER_KEY.concat(String.valueOf(userId));
        /**
         * 删除三端的token
         */
        Object pc = RedisUtils.get(redisKey, PlatformType.PC.getPlatform());
        Object ios = RedisUtils.get(redisKey, PlatformType.IOS.getPlatform());
        Object android = RedisUtils.get(redisKey, PlatformType.ANDROID.getPlatform());

        if(pc != null){
            RedisUtils.remove(TokenConstants.CURRENT_LOGIN_TOKEN, pc.toString());
            RedisUtils.remove(redisKey, PlatformType.PC.getPlatform());
        }

        if(ios != null){
            RedisUtils.remove(TokenConstants.CURRENT_LOGIN_TOKEN, ios.toString());
            RedisUtils.remove(redisKey, PlatformType.IOS.getPlatform());
        }

        if(android != null){
            RedisUtils.remove(TokenConstants.CURRENT_LOGIN_TOKEN, android.toString());
            RedisUtils.remove(redisKey, PlatformType.ANDROID.getPlatform());
        }
    }
    public static void main(String[] args) {
    	System.out.println(returnSign("message"));
	}
}
