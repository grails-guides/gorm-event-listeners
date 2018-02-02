package demo

import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import org.grails.datastore.mapping.engine.event.PreInsertEvent
import org.springframework.test.annotation.Rollback
import spock.lang.Specification

class SerialNumberServiceSpec extends Specification implements ServiceUnitTest<SerialNumberService>, DataTest {

    def setupSpec() {
        mockDomain Book
    }

    def cleanup() {
    }

    @Rollback
    void "test serial number generated"() {
        given:
        Book book = new Book(title: 'abc', author: 'abc', pages: 100)

        when:
        service.setSerialNumber(new PreInsertEvent(dataStore, book))

        then:
        book.serialNumber
    }
}
