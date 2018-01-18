package demo

import grails.rest.Resource

@Resource(uri = '/book')
class Book {

    String title
    Integer pages

    static belongsTo = [author: Author]

    static constraints = {
    }
}
