package com.backend.common;

public class Regexps {

	public static final String EMPTY = "^$";
	
	public static final String MOBILE = "^[1][3,4,5,6,7,8,9][0-9]{9}$";
	
	public static final String SMS_CODE = "^[0-9]{6}$";

//	public static final string email = "[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+";
	public static final String EMAIL = "^([a-zA-Z0-9]+[_|_|.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|_|.]?)*[a-zA-Z0-9]+\\.(?:com|cn)$";

}
