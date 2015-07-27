package toolbox.task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 *
 */
class GenProjectTask extends DefaultTask{

    @TaskAction
    def gen(){
        println("gen projects....")
    }
}
