package demo

import grails.rest.Resource

@Resource(uri = '/book') //<1>
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
