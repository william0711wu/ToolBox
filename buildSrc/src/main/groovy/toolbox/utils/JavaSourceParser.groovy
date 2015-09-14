package toolbox.utils

import com.github.javaparser.JavaParser
import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.comments.Comment
import com.github.javaparser.ast.visitor.VoidVisitorAdapter
import org.springframework.util.StringUtils
import toolbox.utils.model.FieldModel

/**
 * java源代码解释，解释出相应的模型，用于生成相关文件
 */
class JavaSourceParser {
    public static List<FieldModel> parser(def sourceFilePath){
        try {
            // creates an input stream for the file to be parsed
            FileInputStream sourcefile = new FileInputStream(sourceFilePath);

            CompilationUnit cu;
            try {
                // parse the file
                cu = JavaParser.parse(sourcefile);
            } finally {
                sourcefile.close();
            }

            def fields = []

            // visit and print the methods names
            new FieldVisitor().visit(cu, fields);

            return fields
        }catch (e){
            e.printStackTrace()
        }
        return null;
    }

    /**
     * Simple visitor implementation for visiting MethodDeclaration nodes.
     */
    private static class FieldVisitor extends VoidVisitorAdapter {

        @Override
        public void visit(FieldDeclaration n, Object fields) {
            //只有非静态属性可以
            if(n.getModifiers()==25){
                return
            }

            FieldModel fi = new FieldModel();
            fi.fieldName = n.getVariables().get(0).getId().getName()
            fi.fieldType = n.getType().toStringWithoutComments()
            fi.fieldComment = getComment(n.getAllContainedComments())


            fields << fi
        }

        //取得第一行非空注释
        private String getComment(List<Comment> comments){
            for(Comment c : comments){
                if(StringUtils.isEmpty(c.getContent()))  continue;
                return c.getContent();
            }
        }
    }
}
