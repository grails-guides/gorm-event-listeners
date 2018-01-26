package demo

import grails.rest.Resource

@Resource(uri = '/bookTag', readOnly = true)
class BookTag {

    Book book
    Tag tag

    static BookTag create(Book book, Tag tag, boolean flush = false) {
        new BookTag(book: book, tag: tag).save(flush: flush)
    }

    static constraints = {
    }
}
