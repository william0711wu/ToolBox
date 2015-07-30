package toolbox.utils

import com.google.common.base.CaseFormat

/**
 *
 */
class ScaffoldingTemplateDslParser {
    def Binding binding

    /**
     * dsl 处理入口
     */
    def config ={ closure ->
        closure.delegate = delegate
        closure()
    }

    /**
     * 处理代码源文件模板。处理参数如下：
     * <pre>
     * file : 模板文件
     * package : 目标包路径，如果为空，使用默认的项目路径 projectPackage
     * </pre>
     * @param map
     */
    def renderFile(map){

        def templateFile = "${binding[Keys.templateDir]}/${map.template}"
        def filePackage = map.package ?: binding[Keys.projectPackage]
        def filePackagePath = filePackage.replace(".","/")


        def targetProjectDir = map.projectName ? "${binding[Keys.rootDir]}/${map.projectName}" : "${binding[Keys.rootDir]}"
        def targetFile = "${targetProjectDir}/${binding[Keys.sourceSrc]}/${filePackagePath}/${map.className}.${map.fileType}"

        binding['filePackage'] = filePackage
        binding[Keys.className] = map[Keys.className]
        binding[Keys.lcEntityName] =CaseFormat.UPPER_CAMEL.converterTo(CaseFormat.LOWER_UNDERSCORE).convert(binding[Keys.entityName].toString())

        TemplateEngine.render(templateFile,targetFile,binding,binding[Keys.overwrite])
    }
}
