package com.duowan.funding.${projectNameInPackage}.controller.system;

import com.duowan.admin.center.sdk.AdminCenterSdk;
import com.duowan.admin.center.sdk.UserPrivilegeHolder;
import com.duowan.game.common.oauth.client.udb.http.UdbHttpHelper;
import com.duowan.leopard.util.CookieUtil;
import com.duowan.leopard.web.mvc.JsonView;
import com.duowan.leopard.web.userinfo.util.SessionKey;
import com.duowan.udb.auth.UserinfoForOauth;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping(value = "/user", method = { RequestMethod.GET,
		RequestMethod.POST })
public class AuthController {

	private static final Logger logger = Logger.getLogger(AuthController.class);
	
	@Autowired
	@Qualifier("udbAppId")
	private String appId;

	@Autowired
	@Qualifier("udbAppKey")
	private String appKey;

	@Autowired
	private AdminCenterSdk adminCenterSdk;

	@SuppressWarnings("deprecation")
	@RequestMapping(value = "/auth")
	public ModelAndView auth(HttpServletRequest req, HttpServletResponse resp) {
		boolean idValid = false;
		String userName = null;
		String yyuid = null;
		try {
			UserinfoForOauth userInfo = UdbHttpHelper.validateRequest(req,
					resp, appId, appKey);
			if (userInfo != null) {
				userName = userInfo.getUsername();
				yyuid = userInfo.getYyuid();
				if (userName != null) {
					Set<String> set = UserPrivilegeHolder.getPrivileges();
					if (set != null && set.size() > 0) {
						idValid = true;
					}
					idValid = true;
				}
				logger.info("User [yyuid=" + yyuid + ",userName=" + userName
						+ "] login successfully!");
			}
		} catch (Exception e) {
			logger.error(e, e);
			e.printStackTrace();
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("valid", idValid);
		map.put("userName", userName);
		return new JsonView(map);
	}
	
	@RequestMapping(value = "/logout")
	@ResponseBody
	public ModelAndView logout(HttpServletRequest request , HttpServletResponse response) {
		String serverName = request.getServerName();
		String topDomain = getTopDomain(serverName);
		HttpSession session = request.getSession();
		session.removeAttribute(SessionKey.USERNAME_SESSION_NAME);
		session.removeAttribute(SessionKey.YYUID_SESSION_NAME);
		session.removeAttribute(SessionKey.ADMIN_USERNAME);
		CookieUtil.deleteCookie("username", topDomain, response);
		CookieUtil.deleteCookie("password", topDomain, response);
		CookieUtil.deleteCookie("oauthCookie", topDomain, response);
		CookieUtil.deleteCookie("oauthCookiePrivate", serverName, response);
		
		return new JsonView();
	}
	
	private String getTopDomain(String serverName) {
		String topDomain = "";
		if (serverName.endsWith("yy.com")) {
			topDomain = "yy.com";
		} else if (serverName.endsWith("duowan.com")) {
			topDomain = "duowan.com";
		} else {
			throw new IllegalArgumentException("未知域名[" + serverName + "].");
		}
		return topDomain;
	}
}
