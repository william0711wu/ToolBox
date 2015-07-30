package toolbox.utils

import groovy.sql.Sql
import toolbox.utils.model.FieldInfo

import java.sql.DatabaseMetaData
import java.sql.ResultSet
import java.sql.Types

/**
 *
 */
class DbUtils {


    def sql
    def dbName //数据库名

    DbUtils(driver,url,username,password,dbName){
        this.dbName =dbName
        this.sql = Sql.newInstance(url,username,password,driver);
    }


    /**
     * 获取数据库中的表，过滤过不需求的表
     * @param ingnoreTable
     * @return
     */
    def getTables( ingnoreTables){
        def  tbs = [];
        sql.eachRow("show tables"){
            tbs << it[0]
        }
        return tbs.findAll(){
            !ingnoreTables.contains(it)
        }
    }

    /**
     * 获取某个表的相关字段信息
     * @param dbName
     * @param tableName
     * @return
     */
    def getFieldInfoFromDatabase(tableName){
        def fieldInfos = []
        DatabaseMetaData metaData = sql.getConnection().getMetaData()
        ResultSet rs =  metaData.getColumns(null,this.dbName,tableName,null)

        while (rs.next()){
            FieldInfo fieldInfo = new FieldInfo()
            fieldInfo.dbFieldName = rs.getString("COLUMN_NAME")
            fieldInfo.dbFieldComment = rs.getString("REMARKS")
            fieldInfo.sqlType = rs.getInt("DATA_TYPE")

            fieldInfos << fieldInfo
        }

        return fieldInfos
    }

}
