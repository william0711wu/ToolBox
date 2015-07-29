package com.duowan.${projectNameInPackage}.handler.impl;

import com.duowan.leopard.web.userinfo.service.LoginHandler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class LoginHandlerImpl extends LoginHandler {

	@Override
	public boolean isEnableTimeLog() {
		return false;
	}

	@Override
	public List<String> getExcludeUris() {
		List<String> list = new ArrayList<String>();
		//在这里添加不被拦截的url
//		list.add("/personal/recharge_return.do");
		return list;
	}
	
	@Override
	public boolean isUseYyuid() {
		return true;
	}
}
