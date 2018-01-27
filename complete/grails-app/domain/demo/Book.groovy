package demo

import grails.rest.Resource

@Resource(uri = '/book') //<1>
class Book {

    String author
    String title
    Integer pages

    static constraints = {
    }
}
