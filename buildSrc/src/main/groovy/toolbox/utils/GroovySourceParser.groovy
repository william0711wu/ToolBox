package toolbox.utils

import org.codehaus.groovy.groovydoc.GroovyFieldDoc
import org.codehaus.groovy.groovydoc.GroovyRootDoc
import org.codehaus.groovy.tools.groovydoc.GroovyDocTool
import toolbox.utils.model.FieldModel

/**
 *
 */
class GroovySourceParser {
    public static List<FieldModel> parser(def sourceFilePath){
        def fields = []
        GroovyDocTool gdt = new GroovyDocTool()
        gdt.add([sourceFilePath])
        GroovyRootDoc rootDoc = gdt.rootDoc

        GroovyFieldDoc[] fieldDoc = rootDoc.classes()[1].properties()
        fieldDoc.each {
            FieldModel fi = new FieldModel();
            fi.fieldName = it.name()
            fi.fieldType = it.type().toString()
            fi.fieldComment = it.commentText()

            fields<<fi
        }
        return fields
    }
}
