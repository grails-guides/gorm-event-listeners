package demo

import grails.rest.Resource
import grails.compiler.GrailsCompileStatic

@Resource(uri = '/book') //<1>
@GrailsCompileStatic
class Book {

    String author
    String title
    Integer pages
    String serialNumber

    static constraints = {
        serialNumber nullable: true
        title nullable: false
        pages min: 0
        serialNumber nullable: true
    }
}
