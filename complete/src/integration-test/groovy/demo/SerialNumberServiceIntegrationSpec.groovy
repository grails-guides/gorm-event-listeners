package demo

import grails.testing.mixin.integration.Integration
import spock.lang.Specification

@Integration
class SerialNumberServiceIntegrationSpec extends Specification {

    BookDataService bookDataService
    BookTagDataService bookTagDataService
    AuditDataService auditDataService
    TagDataService tagDataService

    def "saving a book, generates automatically a serial number"() {
        when:
        Book book = bookDataService.save('Grails 3', 'Author', 100)

        then:
        book
        !book.hasErrors()
        book.serialNumber

        cleanup:
        auditDataService.deleteByBookId(book.id)
        bookTagDataService.deleteByBook(book)
        bookDataService.delete(book.id)
        tagDataService.delete(tagDataService.find('Grails 3')?.id)

    }
}
