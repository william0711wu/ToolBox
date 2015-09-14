package toolbox.task

import com.google.common.base.CaseFormat
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import toolbox.utils.GroovySourceParser
import toolbox.utils.JavaSourceParser
import toolbox.utils.Keys
import toolbox.utils.ScaffoldingTemplateDslParser

/**
 * 从domain中，生成curd scaffolding代码
 */
class GenScaffoldingFromDomainTask extends DefaultTask{
    String groupId    //组id
    String artifactId  //项目名称
    String entityName //实体名
    String templateName //模板名称
    String rootDir //输出项目路径
    String entityDir //实体类所在路径
    String entityFileType //实体类文件类型，暂时支持groovy 与 java
    boolean overwrite //是否覆盖文件

    @TaskAction
    public void exec () {

        if (!artifactId || !groupId || !entityName || !rootDir || !templateName) {
            printHelp()
            return
        }

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

        def domainSourcePath = "${entityDir}/${entityName}.${entityFileType}".toString() //domain 源文件路径


        println "==>开始处理:${domainSourcePath}"
        def model = new Binding(binding.getVariables())
        if("groovy".equals(entityFileType)){
            model[Keys.fieldModels]= GroovySourceParser.parser(domainSourcePath)
        }else if("java".equals(entityFileType)){
            model[Keys.fieldModels]= JavaSourceParser.parser(domainSourcePath)
        }else {
            println("未知entityFileType")
            return
        }
        model[Keys.entityName] = entityName
        println("处理template dir:${templateDir}")
        parseDslFile(templateDir,model)
        println "==>完成处理表:${entityName}"
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

    def printHelp(){
        def helpStr = """
        创建代码框架：
        gradle scaffoldFromDomain  -PartifactId=ProjectLeopaud -PgroupId=com.duowan -Poverwrite=true -ProotProjectDir=/Users/wuwenyi/Downloads/ProjectLeopaud
        -PgroupId groupPath com.test
        -PartifactId 项目名 ProjectName
        -Poverwrite [可选]是否覆盖文件 true|false
        -PtemplateName 模板名称
        -ProotDir 输出项目根路径
        -PentityName=Account [可先]实体名称，在配置以jdbc情况下，可为空或*，将生成所有表的entity
        """
        println(helpStr)
    }
}
