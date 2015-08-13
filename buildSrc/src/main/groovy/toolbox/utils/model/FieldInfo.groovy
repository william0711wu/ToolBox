package toolbox.utils.model

import com.google.common.base.CaseFormat
import groovy.transform.ToString

import java.sql.Types

/**
 *
 */
@ToString(includeNames=true)
class FieldInfo {
    def dbFieldName;//数据库字段名
    def dbFieldComment;//数据库字段注释
    def fieldShowName;//字段名称，使用数据库字段的注释，截断到第一个空白
    def sqlType;//字段数据类型(对应java.sql.Types中的常量)

    def getFieldShowName() {
        if(!dbFieldComment) return dbFieldName;//无注释，直接使用字段名称
        return  dbFieldComment.split("\\s")[0]
    }

/**
     * 根据数据库字段的命名生成 bean 字段名称，大写驼峰格式
     * @return
     */
    def getFieldNameUc(){
        return  CaseFormat.LOWER_UNDERSCORE.converterTo(CaseFormat.UPPER_CAMEL).convert(dbFieldName)
    }
    /**
     * 根据数据库字段的命名生成 bean 字段名称，小写驼峰格式
     * @return
     */
    def getFieldNameLc(){
        return  CaseFormat.LOWER_UNDERSCORE.converterTo(CaseFormat.LOWER_CAMEL).convert(dbFieldName)
    }
    /**
     * 根据数据库字段类型生成相应的数据类型（java）
     * @return
     */
    def getFieldType(){
        switch (this.sqlType){
            case Types.BIGINT :
                return "Long"
            case Types.INTEGER :
                return "Integer"
            case Types.BIT :
                return "Integer"
            case Types.SMALLINT :
                return "Integer"
            case Types.BOOLEAN :
                return "Boolean"
            case Types.DATE :
                return "Date"
            case Types.TIMESTAMP :
                return "Date"
            case Types.VARCHAR :
                return "String"
            case Types.LONGVARCHAR :
                return "String"
            case Types.DOUBLE :
                return "Double"
            case Types.FLOAT :
                return "Float"
            case Types.NUMERIC :
                return "Double"
            default:
                return "Unkown:"+sqlType
        }
    }


    def getDbFieldName() {
        return dbFieldName
    }

    void setDbFieldName(dbFieldName) {
        this.dbFieldName = dbFieldName
    }
}
