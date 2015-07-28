package toolbox.utils

/**
 *
 */
class ProjectTemplateDslParser {
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
    def renderSourceFile(map){
        def templateFile = "${binding[Keys.templateDir]}/var_template/${map.file}"
        def filePackage = map.package ?: binding[Keys.projectPackage]
        def filePackagePath = filePackage.replace(".","/")
        def targetFile = "${binding[Keys.targetProjectDir]}/${binding[Keys.sourceSrc]}/${filePackagePath}/${map.file}"

        def model = new Binding(binding.getVariables())
        model['filePackage'] = filePackage

        TemplateEngine.render(templateFile,targetFile,model,binding[Keys.overwrite])
    }
}
