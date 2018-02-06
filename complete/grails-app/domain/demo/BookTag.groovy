package demo

import grails.rest.Resource
import grails.compiler.GrailsCompileStatic

@Resource(uri = '/bookTag', readOnly = true)
@GrailsCompileStatic
class BookTag {

    Book book
    Tag tag

    static constraints = {
        book nullable: false
        tag nullable: false
    }
}
