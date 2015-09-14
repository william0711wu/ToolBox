package toolbox.task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.springframework.util.AntPathMatcher
import toolbox.utils.Keys
import toolbox.utils.ProjectTemplateDslParser
import toolbox.utils.TemplateEngine

import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

/**
 * 生成项目 gradle Task
 */
class GenProjectTask extends DefaultTask {

    private AntPathMatcher pathMatcher = new AntPathMatcher()
    //不用模板处理，只需要复制的文件
    private def ingnorePattern = [
            '/**/log4j*.properties',
            '/**/mail*.properties',
            '/**/webapp/commons/**/*.*',
            '/**/webapp/css/**/*.*',
            '/**/webapp/images/**/*.*',
            '/**/webapp/img/**/*.*',
            '/**/webapp/js/**/*.*',
            '/**/webapp/udbsdk/**/*.*',
            '/**/webapp/assets/**/*.*',
            '/**/webapp/layout/**/*.*',
            '/**/webapp/WEB-INF/jsp/**/*.*',
            '/**/webapp/index.html',
            '/**/webapp/favicon.ico',
    ]

    String groupId
    String artifactId
    String templateName //模板名称
    String outDir //输出项目路径
    boolean overwrite //是否覆盖文件

    @TaskAction
    def gen() {
        if (!artifactId || !groupId) {
            printHelp()
            return
        }
        println("开始生成项目[${artifactId}]...")
        templateName=templateName ?: "spring-boot"
        outDir = outDir ?: './'
        def sourceSrc = "src/main/java" //源文件路径
        def templateSrc = "src/main/templates"
        def targetProjectDir = "${outDir}${artifactId}"//目标项目路径
        def templateDir = "${project.projectDir}/${templateSrc}/${templateName}"//模板路径

        def binding = new Binding()
        binding[Keys.projectName] = artifactId
        binding[Keys.projectNameInPackage] = artifactId.toLowerCase()
        binding[Keys.groupId] = groupId
        binding[Keys.overwrite] = overwrite
        binding[Keys.projectPackage] =groupId+"."+artifactId.toLowerCase()
        binding[Keys.sourceSrc] = sourceSrc
        binding[Keys.outDir] = outDir
        binding[Keys.targetProjectDir] = targetProjectDir
        binding[Keys.templateDir] = templateDir


        new File(targetProjectDir).mkdirs()//创建项目目录

        new File(templateDir).eachFileRecurse() {
            def targetSubDir = it.path.replace(templateDir, "")
            def targetFilePath = targetProjectDir + replaceVarInPath(binding.variables,targetSubDir) //目标文件路径
            def templateFilePath = it.path //模板路径
            println("开始处理文件："+templateFilePath)
            if (it.name.equals("var_template") || it.parentFile.name.equals("var_template")){
                println("跳过文件："+templateFilePath)
            }else {
                if (it.isDirectory()) {
                    new File(targetFilePath).mkdirs()
                }  else {
                    if(matchIgnore(templateFilePath)){
                        Files.copy(Paths.get(templateFilePath),Paths.get(targetFilePath), StandardCopyOption.REPLACE_EXISTING)
                    }else {
                        TemplateEngine.render(templateFilePath, targetFilePath, binding, overwrite)
                    }
                }
            }
            logger.info("完成处理文件："+templateFilePath)
        }

        parseDslFile(templateDir, binding)



    }

    def printHelp() {
        def helpStr = """
        创建新的项目：
        gradle genProject -PgroupId=com.test -PartifactId=ProjectName -Poverwrite=true -PtemplateName=spring-boot -PoutDir=/Users/wuwenyi/Downloads/"
        -PgroupId groupPath com.test
        -PartifactId 项目名 ProjectName
        -Poverwrite [可选]是否覆盖文件 true|false
        -PtemplateName [可选]模板名称
        -PoutDir [可选]输出项目路径
        """
        println(helpStr)
    }

    /**
     * 解释项目个性dsl
     * @param templateDir
     * @param binding
     */
    def void parseDslFile(templateDir, Binding binding) {
        def dslFile = new File("${templateDir}/var_template/TemplateDsl.groovy")
        if (dslFile.exists()) {
            //解释项目个性dsl
            ProjectTemplateDslParser dslParser = new ProjectTemplateDslParser()
            dslParser.binding = binding

            binding.config = dslParser.&config
            def shell = new GroovyShell(binding);
            shell.evaluate(dslFile)
        }
    }


    /**
     * 替换在目录路径中的变量
     * @param binding
     * @param path
     */
    def replaceVarInPath( binding, path){
        path = path.replace("\${projectName}",binding[Keys.projectName])
        path = path.replace("\${projectNameInPackage}",binding[Keys.projectNameInPackage])
        return path
    }

    /**
     * 是否满足忽略模板
     * @param filePath
     * @return
     */
    def matchIgnore(filePath){
        for(String pattern : ingnorePattern){
            if(pathMatcher.match(pattern,filePath)) return true
        }
        return false
    }

}
