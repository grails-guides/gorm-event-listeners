package demo

import grails.rest.Resource
import grails.compiler.GrailsCompileStatic

@Resource(uri = '/author', readOnly = true) //<1>
@GrailsCompileStatic
class Tag {

    String name

    static constraints = {
        name nullable: false
    }
}
