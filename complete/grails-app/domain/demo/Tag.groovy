package demo

import grails.rest.Resource

@Resource(uri = '/author', readOnly = true)
class Tag {

    String name
}
