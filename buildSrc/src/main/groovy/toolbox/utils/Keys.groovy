package toolbox.utils

/**
 * Biding相关变量key
 */
class Keys {
    final static String artifactId = "artifactId" //项目名称
    final static String projectName = "projectName" //项目名称 ProjectName
    final static String groupId = "groupId" //项目group id 例如：com.test
    final static String projectPackage = "projectPackage" //项目package id 例如：com.test.projectname
    final static String overwrite = "overwrite" //是否覆盖文件
    final static String outDir = "outDir" //输出项目路径

    final static String sourceSrc = "sourceSrc" //源文件路径"src/main/java"
    final static String targetProjectDir = "targetProjectDir" //目标项目路径"${outDir}${artifactId}"
    final static String templateDir = "templateDir" //模板路径
}
