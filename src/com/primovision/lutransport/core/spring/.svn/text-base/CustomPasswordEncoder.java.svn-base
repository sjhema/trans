package com.primovision.lutransport.core.spring;

import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.encoding.PasswordEncoder;

public class CustomPasswordEncoder implements PasswordEncoder {

	@Override
	public String encodePassword(String rawPass, Object arg1)
			throws DataAccessException {
		//return PasswordUtil.encryptPassword(rawPass);
		return rawPass;
	}

	@Override
	public boolean isPasswordValid(String encPass, String rawPass, Object arg2)
			throws DataAccessException {
		if (rawPass==null)
			return false;
		//String encRaw = PasswordUtil.encryptPassword(rawPass);
		return rawPass.equalsIgnoreCase(encPass);
	}

}
