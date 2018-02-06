package demo

import grails.rest.Resource
import grails.compiler.GrailsCompileStatic

@Resource(uri = '/bookTag', readOnly = true)
@GrailsCompileStatic
class BookTag {

    Book book
    Tag tag

    static BookTag create(Book book, Tag tag, boolean flush = false) { //<1>
        new BookTag(book: book, tag: tag).save(flush: flush)
    }

    static constraints = {
        book nullable: false
        tag nullable: false
    }
}
