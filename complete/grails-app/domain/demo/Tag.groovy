package demo

import grails.rest.Resource

@Resource(uri = '/author', readOnly = true) //<1>
class Tag {

    String name
}
