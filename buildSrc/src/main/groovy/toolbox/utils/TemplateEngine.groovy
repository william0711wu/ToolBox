package toolbox.utils
import groovy.text.GStringTemplateEngine
/**
 *
 */
public class TemplateEngine {

    /**
     * 使用模板引擎生成文件
     * @param templateFile 模板文件
     * @param destinationFile 目标文件
     * @param model 数据变量
     * @param overwrite 是否覆盖
     * @return
     */
    def static render(String templateFilePath,String destinationFilePath,Binding model,boolean  overwrite ){
        render(new File(templateFilePath),new File(destinationFilePath),model,overwrite)
    }


    def static render(File templateFile,File  destinationFile,Binding model,boolean  overwrite){
        def txt =  templateFile.text
        def engine = new GStringTemplateEngine()
        def template = engine.createTemplate(txt).make(model.variables)//创建模板
        if(!destinationFile.exists()){ //目标文件不存储
            destinationFile.parentFile.mkdirs()
            destinationFile.write(template.toString())
            println("生成:"+destinationFile.getAbsolutePath())
        }else if(overwrite){
            destinationFile.write(template.toString())
            println("覆盖:"+destinationFile.getAbsolutePath())
        }else {
            println("跳过:"+destinationFile.getAbsolutePath())
        }
    }


}
