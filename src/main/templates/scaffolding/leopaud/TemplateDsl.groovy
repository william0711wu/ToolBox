/**
 * crud 代码框架生成描述dsl 使用ScaffoldingTemplateDslParser处理
 */
config{

    //生成entity
    renderFile(template:'Entity.template',//使用的模板
            package:"com.duowan.${projectNameInPackage}.model", //生成的文件包路径
            projectName:"${rootProjectName}-model",//文件所在项目名称，如果没有多项目，设置为空
            fileType:'java',//生成文件类型
            className:"${entityName}"
    )

    //生成entity 查询表单
    renderFile(template:'EntityForm.template',//使用的模板
            package:"com.duowan.${projectNameInPackage}.form", //生成的文件包路径
            projectName:"${rootProjectName}-model",//文件所在项目名称，如果没有多项目，设置为空
            fileType:'java',//生成文件类型
            className:"${entityName}Form"
    )

    //生成Dao接口
    renderFile(template:'DaoInterface.template',//使用的模板
            package:"com.duowan.${projectNameInPackage}.dao", //生成的文件包路径
            projectName:"${rootProjectName}-dao",//文件所在项目名称，如果没有多项目，设置为空
            fileType:'java',//生成文件类型
            className:"${entityName}Dao"
    )
    //生成Dao,Mysql实现接口
    renderFile(template:'DaoInterfaceImpl.template',//使用的模板
            package:"com.duowan.${projectNameInPackage}.dao.mysql", //生成的文件包路径
            projectName:"${rootProjectName}-dao",//文件所在项目名称，如果没有多项目，设置为空
            fileType:'java',//生成文件类型
            className:"${entityName}DaoMysqlImpl"
    )

    //生成Service接口
    renderFile(template:'ServiceInterface.template',//使用的模板
            package:"com.duowan.${projectNameInPackage}.service", //生成的文件包路径
            projectName:"${rootProjectName}-service",//文件所在项目名称，如果没有多项目，设置为空
            fileType:'java',//生成文件类型
            className:"${entityName}Service"
    )
    //生成Service实现
    renderFile(template:'ServiceInterfaceImpl.template',//使用的模板
            package:"com.duowan.${projectNameInPackage}.service.impl", //生成的文件包路径
            projectName:"${rootProjectName}-service",//文件所在项目名称，如果没有多项目，设置为空
            fileType:'java',//生成文件类型
            className:"${entityName}ServiceImpl"
    )

    //生成Controller
    renderFile(template:'Controller.template',//使用的模板
            package:"com.duowan.${projectNameInPackage}.web.controller", //生成的文件包路径
            projectName:"${rootProjectName}-web-admin",//文件所在项目名称，如果没有多项目，设置为空
            fileType:'java',//生成文件类型
            className:"${entityName}Controller"
    )

    //生成页面文件
    rednerPage(template:'page_index.template',//首页模板
            projectName:"${rootProjectName}-web-admin",//文件所在项目名称，如果没有多项目，设置为空
            pagePath:'WEB-INF/jsp',//生成路径
            fileName:'index.jsp'//生成文件名称
    )
    rednerPage(template:'page_create.template',//新建模板
            projectName:"${rootProjectName}-web-admin",//文件所在项目名称，如果没有多项目，设置为空
            pagePath:'WEB-INF/jsp',//生成路径
            fileName:'create.jsp'//生成文件名称
    )
    rednerPage(template:'page_edit.template',//编辑模板
            projectName:"${rootProjectName}-web-admin",//文件所在项目名称，如果没有多项目，设置为空
            pagePath:'WEB-INF/jsp',//生成路径
            fileName:'edit.jsp'//生成文件名称
    )
    rednerPage(template:'page_show.template',//查看模板
            projectName:"${rootProjectName}-web-admin",//文件所在项目名称，如果没有多项目，设置为空
            pagePath:'WEB-INF/jsp',//生成路径
            fileName:'show.jsp'//生成文件名称
    )
    rednerPage(template:'page_form.template',//查看模板
            projectName:"${rootProjectName}-web-admin",//文件所在项目名称，如果没有多项目，设置为空
            pagePath:'WEB-INF/jsp',//生成路径
            fileName:'form.jsp'//生成文件名称
    )
    rednerPage(template:'page_list.template',//列表模板
            projectName:"${rootProjectName}-web-admin",//文件所在项目名称，如果没有多项目，设置为空
            pagePath:'WEB-INF/jsp',//生成路径
            fileName:'list.jsp'//生成文件名称
    )
    rednerPage(template:'page_page_bar.template',//页头模板
            projectName:"${rootProjectName}-web-admin",//文件所在项目名称，如果没有多项目，设置为空
            pagePath:'WEB-INF/jsp',//生成路径
            fileName:'page_bar.jsp'//生成文件名称
    )

}