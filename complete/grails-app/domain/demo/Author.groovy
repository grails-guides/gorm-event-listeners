package demo

import grails.rest.Resource

@Resource(uri = '/author')
class Author {


    String name
    Date birthday

    static hasMany = [books: Book]

    static constraints = {
    }
}
