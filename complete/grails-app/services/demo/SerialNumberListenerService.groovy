package demo

import grails.events.annotation.gorm.Listener
import grails.gorm.transactions.Transactional
import groovy.transform.CompileStatic
import org.apache.commons.lang.RandomStringUtils
import org.grails.datastore.mapping.engine.event.PreInsertEvent

@CompileStatic
class SerialNumberListenerService {

    SerialNumberGeneratorService serialNumberGeneratorService

    @Listener(Book)
    void onBookPreInsert(PreInsertEvent event) {
        String title = event.entityAccess.getProperty('title') as String // <1>
        String serialNumber = serialNumberGeneratorService.generate(title)
        event.entityAccess.setProperty('serialNumber', serialNumber) //<2>
    }
}
