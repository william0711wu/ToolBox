package toolbox.task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import toolbox.utils.Keys
import toolbox.utils.ProjectTemplateDslParser
import toolbox.utils.TemplateEngine

/**
 * 生成项目 gradle Task
 */
class GenProjectTask extends DefaultTask {

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
        def templateName = "spring-boot"
        def sourceSrc = "src/main/java" //源文件路径
        def templateSrc = "src/main/templates"
        def targetProjectDir = "${outDir}${artifactId}"//目标项目路径
        def templateDir = "${project.projectDir}/${templateSrc}/${templateName}"//模板路径
        outDir = outDir ?: './'

        def binding = new Binding()
        binding[Keys.projectName] = artifactId
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
            def targetFilePath = targetProjectDir + targetSubDir //目标文件路径
            def templateRelPath = it.path //相对模板路径

            if (it.name.equals("var_template") || it.parentFile.name.equals("var_template")){
                println("jump var template and files...")
            }else {
                if (it.isDirectory()) {
                    new File(targetFilePath).mkdirs()
                }  else {
                    println(templateRelPath)
                    println(targetFilePath)
                    TemplateEngine.render(templateRelPath, targetFilePath, binding, overwrite)
                }
            }
        }

        //解释项目个性dsl
        ProjectTemplateDslParser dslParser = new ProjectTemplateDslParser()
        dslParser.binding = binding

        binding.config = dslParser.&config

        //
        def dslFile = new File("${templateDir}/var_template/TemplateDsl.groovy")
        def shell = new GroovyShell(binding);
        shell.evaluate(dslFile)
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

}
