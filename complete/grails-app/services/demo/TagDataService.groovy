package demo

import grails.gorm.services.Service
import grails.gorm.transactions.Transactional
import groovy.transform.CompileStatic

@CompileStatic
@Service(Tag)
abstract class TagDataService {
    abstract Tag find(String name)
    abstract Tag save(String name)
    abstract void delete(Serializable id)
}