package toolbox.utils.model

/**
 * 字段模型，通过解释domain获取相应的字段信息，主要用于其它crud等文件的生成
 */
class FieldModel {
    String fieldName//字段名称
    String fieldType//字段类型
    String fieldComment//注释

    /**
     * 从字段注释中获取字段中文名，第一个空白之前的字段串为有效名称，如果没有设置则默认使用字段名称
     */
    def getFieldNameOfCn(){
        if(!fieldComment) return fieldName
        def cnname = fieldComment.split("\\s")[0]
        return cnname.replace("//","")
    }
}
