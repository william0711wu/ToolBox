package toolbox.task

import com.google.common.base.CaseFormat
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import toolbox.utils.DbUtils
import toolbox.utils.Keys
import toolbox.utils.ScaffoldingTemplateDslParser

/**
 *
 */
class ScaffoldingTask extends DefaultTask {

        String groupId    //组id
        String artifactId  //项目名称
        String entityName //实体名
        String templateName //模板名称
        String rootDir //输出项目路径
        boolean overwrite //是否覆盖文件

        String driver ="com.mysql.jdbc.Driver" //jdbc 驱动
        String jdbcurl //
        String dbusername //数据库用户名
        String dbpasswd //数据库密码
        String dbname

        String ingnoreTables //需要忽略的表名，使用，号分隔

        DbUtils dbUtils

        @TaskAction
        public void exec () {

            if (!artifactId || !groupId) {
                printHelp()
                return
            }
            if (!entityName && !jdbcurl) {
                printHelp()
                return
            }

            //如果配置了数据库，构建数据库帮助类
            if(jdbcurl){
                dbUtils = new DbUtils(driver,jdbcurl,dbusername,dbpasswd,dbname)
            }
            /**
             * 要生成entity的表名
             */
            def tableNames = []
            if(!entityName || "*".equals(entityName)){
                def igtbs = ingnoreTables ? ingnoreTables.split(",") :[]//需要忽略的表名
                tableNames = dbUtils.getTables(igtbs)
            }else {
                tableNames << CaseFormat.UPPER_CAMEL.converterTo(CaseFormat.LOWER_UNDERSCORE).convert(entityName)
            }

            templateName=templateName ?: "leopaud"
            rootDir = rootDir ?: './${artifactId}'
            def sourceSrc = "src/main/java" //源文件路径
            def templateSrc = "src/main/templates/scaffolding"
            //def targetProjectDir = "${rootDir}${artifactId}"//目标项目路径
            def templateDir = "${project.projectDir}/${templateSrc}/${templateName}"//模板路径

            def binding = new Binding()
            binding[Keys.rootProjectName] = artifactId
            binding[Keys.projectNameInPackage] = artifactId.toLowerCase()
            binding[Keys.groupId] = groupId
            binding[Keys.overwrite] = overwrite
            binding[Keys.projectPackage] =groupId+"."+artifactId.toLowerCase()
            binding[Keys.sourceSrc] = sourceSrc
            binding[Keys.rootDir] = rootDir
           // binding[Keys.targetProjectDir] = targetProjectDir
            binding[Keys.templateDir] = templateDir

            tableNames.each {
                println "==>开始处理表:${it}"
                def model = new Binding(binding.getVariables())
                model[Keys.fieldInfos]=this.dbUtils? this.dbUtils.getFieldInfoFromDatabase(it) : []
                model[Keys.entityName] = CaseFormat.LOWER_UNDERSCORE.converterTo(CaseFormat.UPPER_CAMEL).convert(it)
                parseDslFile(templateDir,model)
                println "==>完成处理表:${it}"
            }

        }


        def printHelp(){
            def helpStr = """
        创建代码框架：
        gradle scaffoldOfLeopaud  -PartifactId=ProjectLeopaud -PgroupId=com.duowan -Poverwrite=true -ProotProjectDir=/Users/wuwenyi/Downloads/ProjectLeopaud
        -PgroupId groupPath com.test
        -PartifactId 项目名 ProjectName
        -Poverwrite [可选]是否覆盖文件 true|false
        -PtemplateName [可选]模板名称 默认使用 leopaud
        -ProotProjectDir [可选]输出项目根路径
        -PentityName=Account [可先]实体名称，在配置以jdbc情况下，可为空或*，将生成所有表的entity

        //数据库配置[可选]
        -Pjdbcurl="jdbc:mysql://183.136.136.98:3306/funding?useUnicode=true&characterEncoding=utf8&autoReconnect=true&zeroDateTimeBehavior=convertToNull"
        -Pdbusername=root 数据库用户名
        -Pdbpasswd=thriftdy@819 数据库密码
        -Pdbname=gos 数据库名
        -PingnoreTables=at,b  忽略处理的表名，逗号分隔
        """
            println(helpStr)
        }

        /**
         * 解释项目个性dsl
         * @param templateDir
         * @param binding
         */
        def void parseDslFile(templateDir, Binding binding) {
            def dslFile = new File("${templateDir}/TemplateDsl.groovy")
            if (dslFile.exists()) {
                //解释项目个性dsl
                ScaffoldingTemplateDslParser dslParser = new ScaffoldingTemplateDslParser()
                dslParser.binding = binding

                binding.config = dslParser.&config
                def shell = new GroovyShell(binding);
                shell.evaluate(dslFile)
            }
        }

}
