package demo

import grails.rest.Resource

@Resource(uri = '/book')
class Book {

    String author
    String title
    Integer pages

    static constraints = {
    }
}
