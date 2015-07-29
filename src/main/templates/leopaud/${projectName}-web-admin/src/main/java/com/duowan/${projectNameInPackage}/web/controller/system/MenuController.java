package com.duowan.funding.${projectNameInPackage}.controller.system;

import com.duowan.admin.center.sdk.UserPrivilegeHolder;
import com.duowan.game.common.menu.SystemMenuUtil;
import com.duowan.game.common.oauth.client.udb.http.UdbHttpHelper;
import com.duowan.game.common.ucclient.dto.FuncDto;
import com.duowan.game.common.ucclient.support.RetCode;
import com.duowan.leopard.web.mvc.JsonView;
import com.duowan.udb.auth.UserinfoForOauth;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping(value = "/menu",method = { RequestMethod.GET, RequestMethod.POST })
public class MenuController {
	private static final Logger logger = Logger.getLogger(MenuController.class);

	@Autowired
	@Qualifier("privilegeInitPath")
	private String privilegeInitPath;

	@Autowired
	@Qualifier("udbAppId")
	private String appId;

	@Autowired
	@Qualifier("udbAppKey")
	private String appKey;

	private static String MENU_XML = null;

	@SuppressWarnings("deprecation")
	@RequestMapping(value = "/query")
	@ResponseBody
	public JsonView list(String gameId, HttpServletRequest request,
			HttpServletResponse resp) {
		try {
			List<FuncDto> items = new ArrayList<FuncDto>();
			UserinfoForOauth userInfo = UdbHttpHelper.validateRequest(request,
					resp, appId, appKey);
			if (userInfo != null) {
				String userName = userInfo.getUsername();
				Set set = UserPrivilegeHolder.getPrivileges();
				items = SystemMenuUtil.queryUserMenuItems(userName,MENU_XML);
			}
			return new JsonView(items);
		} catch (Exception e) {
			e.printStackTrace();
			return new JsonView(RetCode.SYS_ERR, e.getMessage());
		}
	}

	@PostConstruct
	private void getMenuXMLFileContent() {
		InputStream is = null;
		try {
			is = this.getClass().getResourceAsStream(privilegeInitPath);
			if (is == null) {
				is = new FileInputStream(privilegeInitPath);
			}
			MENU_XML = IOUtils.toString(is);
		} catch (Exception e) {
			logger.error("获取本地菜单xml文件内容失败：" + e.getMessage());
		} finally {
			IOUtils.closeQuietly(is);
		}
	}

}
