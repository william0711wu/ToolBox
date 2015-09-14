/**
 * crud 代码框架生成描述dsl 使用ScaffoldingTemplateDslParser处理
 */
config{



    //生成Controller
    renderFile(template:'Controller.template',//使用的模板
            package:"com.duowan.${projectNameInPackage}", //生成的文件包路径
            projectName:"",//文件所在项目名称，如果没有多项目，设置为空
            fileType:'groovy',//生成文件类型
            sourceSrc:'grails-app/controller',//源文件目录
            className:"${entityName}Controller"
    )



}