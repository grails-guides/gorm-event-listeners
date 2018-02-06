package demo

import grails.rest.Resource

@Resource(uri = '/audit', readOnly = true) //<1>
class Audit {

    String event
    Long bookId

    static constraints = {
        event nullable: false, blank: false
        bookId nullable: false
    }
}
