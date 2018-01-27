package demo

import grails.events.annotation.gorm.Listener
import grails.gorm.transactions.Transactional
import groovy.transform.CompileStatic
import org.apache.commons.lang.RandomStringUtils
import org.grails.datastore.mapping.engine.event.PreInsertEvent

@CompileStatic
@Transactional
class SerialNumberService {

    @Listener(Book)
    def setSerialNumber(PreInsertEvent event) {
        String randomString = RandomStringUtils.random(8, true, false)
        String title = "${event.entityAccess.getProperty('title')}".take(4) //<1>

        String value = "${title}-${randomString}".toUpperCase()
        event.entityAccess.setProperty('serialNumber', value) //<2>
    }
}
