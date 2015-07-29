package com.duowan.${projectNameInPackage}.handler.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.duowan.leopard.web.userinfo.service.LoginHandler;

@Component
public class LoginHandlerImpl extends LoginHandler {

	@Override
	public boolean isEnableTimeLog() {
		return false;
	}

	@Override
	public List<String> getExcludeUris() {
		List<String> list = new ArrayList<String>();		
		return list;
	}
	
	@Override
	public boolean isUseYyuid() {
		return true;
	}
}
