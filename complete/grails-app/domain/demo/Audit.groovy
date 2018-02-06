package demo

import grails.rest.Resource
import grails.compiler.GrailsCompileStatic

@Resource(uri = '/audit', readOnly = true) //<1>
@GrailsCompileStatic
class Audit {

    String event
    Long bookId

    static constraints = {
        event nullable: false, blank: false
        bookId nullable: false
    }
}
