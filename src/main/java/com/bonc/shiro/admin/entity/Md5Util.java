package com.bonc.shiro.admin.entity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Base64.Encoder;


public class Md5Util {

	private static final String salt = "brand.bonc.com.cn";
	
	public static String encoding(String pwd){
		String newpwd =  "";
		try {
			MessageDigest md5 = MessageDigest.getInstance("md5");
			md5.update(salt.getBytes());
			Encoder encoder = Base64.getEncoder();
			newpwd  =  encoder.encodeToString(md5.digest(pwd.getBytes())).replaceAll("[+]", "");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return newpwd;
	}
	
	public static void main(String[] args) {
		String a = encoding("123");
		System.out.println(a);//JCVQJ/HPyAneV6bhPQPcg==
	}
}
