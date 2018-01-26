package demo

import grails.rest.Resource

@Resource(uri = '/audit', readOnly = true)
class Audit {

    String event
    Long bookId

    static constraints = {
    }
}
