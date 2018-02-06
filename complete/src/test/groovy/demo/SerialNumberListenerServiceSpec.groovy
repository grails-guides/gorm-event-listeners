package demo

import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import org.grails.datastore.mapping.engine.event.PreInsertEvent
import org.springframework.test.annotation.Rollback
import spock.lang.Specification

class SerialNumberListenerServiceSpec extends Specification implements ServiceUnitTest<SerialNumberListenerService>, DataTest {

    def setupSpec() {
        mockDomain Book
    }

    @Rollback
    void "test serial number generated"() {
        given:
        Book book = new Book(title: 'abc', author: 'abc', pages: 100)

        when:
        service.serialNumberGeneratorService = Stub(SerialNumberGeneratorService) {
            generate(_ as String) >> 'XXXX-5125'
        }
        service.onBookPreInsert(new PreInsertEvent(dataStore, book))

        then:
        book.serialNumber == 'XXXX-5125'
    }
}
