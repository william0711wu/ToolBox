package com.duowan.common.interceptor;

import com.duowan.admin.center.sdk.AdminCenterSdk;
import com.duowan.admin.center.sdk.UserPrivilegeHolder;
import com.duowan.admin.enums.CenterInfoType;
import com.duowan.game.common.menu.SystemMenuUtil;
import com.duowan.game.common.oauth.client.udb.http.UdbHttpHelper;
import com.duowan.game.common.support.SystemRegular;
import com.duowan.game.common.ucclient.dto.FuncDto;
import com.duowan.udb.auth.UserinfoForOauth;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 *
 * 权限拦截，拦截后台应用url。功能如下
 * <pre>
 *     1.ipRestrict = true 开启ip白名单限制，需要在http://download.game.yy.com/cgame/functree/regular.xml 配置白名单
 *     2.获取用户的具有的权限集合，并放入到UserPrivilegeHolder中
 *     3.获取用户具有权限的菜单集合，并入到每个request中
 *     4.TODO:判断每个url用户是否可以访问，限制只有允许的url才可以访问
 * </pre>
 */
public class AuthInterceptor extends HandlerInterceptorAdapter {
    private static final Logger log = LoggerFactory.getLogger(AuthInterceptor.class);

    private static final String CDN_SYS_REGULAR_URL = "http://download.game.yy.com/cgame/functree/regular.xml";//ip限制配置文件地址

    private String appId;
    private String appKey;
    private Long cacheTime=10L; //缓存时间
    private AdminCenterSdk adminCenterSdk;
    private String sysName;//系统名称
    private boolean ipRestrict= false;//是否启用ip限制


    //用户权限id缓存，缓存用户权限id set
    private LoadingCache<String,Set<String>> userPrivileges;
    private LoadingCache<String,List<SystemRegular>> ipRegulars; //ip限制缓存
    private LoadingCache<String,List<FuncDto>> userMenus; //用户具有权限的菜单列表

    @PostConstruct
    public void init(){
        userPrivileges = CacheBuilder.newBuilder().expireAfterWrite(cacheTime, TimeUnit.MINUTES).build(new CacheLoader<String, Set<String>>() {
            @Override
            public Set<String> load(String userName) throws Exception {
                return adminCenterSdk.getCenterInfo(userName, CenterInfoType.PRIVILEGE);
            }
        });
        ipRegulars =CacheBuilder.newBuilder().expireAfterWrite(cacheTime,TimeUnit.MINUTES).build(new CacheLoader<String, List<SystemRegular>>() {
            @Override
            public List<SystemRegular> load(String sysName) throws Exception {
                List<SystemRegular>  list =  getRegulars(sysName);
                return list;
            }
        });
        userMenus =CacheBuilder.newBuilder().expireAfterWrite(cacheTime,TimeUnit.MINUTES).build(new CacheLoader<String, List<FuncDto>>() {
            @Override
            public List<FuncDto> load(String userName) throws Exception {
                return  SystemMenuUtil.queryUserMenuItems(userName, getMenuXMLFileContent());
            }
        });
    }



    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        UserinfoForOauth curUser = UdbHttpHelper.validateRequest(request, response, this.appId, this.appKey);
        if(curUser == null){
            log.info("用户未登录。。");
            return false;
        }
        String requestUri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String url = requestUri.substring(contextPath.length());
        if(url.equalsIgnoreCase("/pageNotFound.do")) {
            log.info("page not found! redirect to index.html");
            response.sendRedirect("/index.html");
            return false;
        }

        if(ipRestrict){
            String ip = UserinfoForOauth.getRealIp(request);
            if(!isIpLeagal(url,ip)) return false;//ip不合法
        }

        Set<String> userPrivilegeSet = userPrivileges.get(curUser.getUsername());
        UserPrivilegeHolder.holdPrivileges(userPrivilegeSet);

        //获取用户菜单
        List<FuncDto> funcList = userMenus.get(curUser.getUsername());
        request.setAttribute("_userFuncList",funcList);
        request.setAttribute("_username",curUser.getUsername());
        //TODO:限制只有允许的url才可以访问
        return true;
    }

    /**
     * 判断相应ip是否可以访问某url
     * @param url
     * @param ip
     * @return
     * @throws Exception
     */
    private boolean isIpLeagal(String url,String ip) throws Exception{
        List<SystemRegular> systemRegulars = ipRegulars.get(this.sysName);
        if(CollectionUtils.isEmpty(systemRegulars)){
            log.warn("开始ip限制，但未在"+CDN_SYS_REGULAR_URL+"配置相关选项");
            return false;
        }
        for(SystemRegular systemRegular : systemRegulars){
            if(ip.matches(systemRegular.getIp()) && url.matches(systemRegular.getUrl())){
                return true;
            }
        }
        log.info("IP:"+ip+",不请允访问此URL:"+url);
        return false;
    }

    /**
     * 获取本地权限配置文件
     */
    private String getMenuXMLFileContent() {
        InputStream is = null;
        try {
            Field field = adminCenterSdk.getClass().getDeclaredField("privilegeInitPath");
            field.setAccessible(true);
            String privilegeInitPath =  (String)field.get(adminCenterSdk);
            is = this.getClass().getResourceAsStream(privilegeInitPath);
            if (is == null) {
                is = new FileInputStream(privilegeInitPath);
            }
            return IOUtils.toString(is);
        } catch (Exception e) {
            log.error("获取本地权限配置文件xml文件内容失败：" + e.getMessage());
        } finally {
            IOUtils.closeQuietly(is);
        }
        return null;
    }


    /**
     * 获取ip白名单信息
     * @param sysName
     * @return
     */
    private List<SystemRegular> getRegulars(String sysName) {
        try {
            SAXReader e = new SAXReader();
            Document document = e.read("http://download.game.yy.com/cgame/functree/regular.xml");
            Node node = document.selectSingleNode("//system [@id=\'" + sysName.trim() + "\']");
            if(node == null) {
                return null;
            } else {
                ArrayList list = new ArrayList();
                Iterator iterator = ((Element)node).elementIterator("regular");
                SystemRegular tmp = null;
                Attribute attr = null;
                Element e1 = null;
                String id = null;
                String ip = null;
                String url = null;

                for(Integer sequence = Integer.valueOf(-999); iterator.hasNext(); list.add(tmp)) {
                    tmp = new SystemRegular();
                    e1 = (Element)iterator.next();
                    attr = e1.attribute("id");
                    if(attr != null) {
                        id = attr.getStringValue().trim();
                        tmp.setId(new Integer(id.trim()));
                    } else {
                        tmp.setId(sequence);
                        sequence = Integer.valueOf(sequence.intValue() + 1);
                    }

                    attr = e1.attribute("ip");
                    if(attr != null) {
                        ip = attr.getStringValue().trim();
                        tmp.setIp(ip);
                    } else {
                        tmp.setIp(".*");
                    }

                    attr = e1.attribute("url");
                    if(attr != null) {
                        url = attr.getStringValue().trim();
                        tmp.setUrl(url);
                    } else {
                        tmp.setUrl(".*");
                    }
                }

                Collections.sort(list);
                return list;
            }
        } catch (Exception var14) {
            log.error("获取ip白名称出错：",var14);
            return null;
        }
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public Long getCacheTime() {
        return cacheTime;
    }

    public void setCacheTime(Long cacheTime) {
        this.cacheTime = cacheTime;
    }

    public AdminCenterSdk getAdminCenterSdk() {
        return adminCenterSdk;
    }

    public void setAdminCenterSdk(AdminCenterSdk adminCenterSdk) {
        this.adminCenterSdk = adminCenterSdk;
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public boolean isIpRestrict() {
        return ipRestrict;
    }

    public void setIpRestrict(boolean ipRestrict) {
        this.ipRestrict = ipRestrict;
    }
}



