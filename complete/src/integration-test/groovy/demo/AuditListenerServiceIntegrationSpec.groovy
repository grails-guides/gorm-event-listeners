package demo

import grails.testing.mixin.integration.Integration
import grails.transaction.Rollback
import spock.lang.Specification
import spock.util.concurrent.PollingConditions

@Integration
class AuditListenerServiceIntegrationSpec extends Specification {

    BookTagDataService bookTagDataService
    BookDataService bookDataService
    AuditDataService auditDataService
    TagDataService tagDataService

    void "saving a Book causes an Audit instance to be saved"() {
        when:
        Book book = bookDataService.save('Practical Grails 3', 'Eric Helgeson', 1)

        then:
        book
        book.id
        bookTagDataService.count() == old(bookTagDataService.count()) + 1
        auditDataService.count() == old(auditDataService.count()) + 1

        when:
        Audit lastAudit = this.lastAudit()

        then:
        lastAudit.event == "Book saved"
        lastAudit.bookId == book.id

        when: 'A books is updated'
        book = bookDataService.update(book.id, 'Grails 3')

        then: 'a new audit instance is created'
        auditDataService.count() == old(auditDataService.count()) + 1

        and: 'old bookTags were deleted and a new booktag replaced it'
        //bookTagDataService.count() == old(bookTagDataService.count()) // TODO

        when:
        lastAudit = this.lastAudit()

        then:
        book.title == 'Grails 3'
        lastAudit.event == 'Book updated'
        lastAudit.bookId == book.id

        when: 'A book is deleted'
        bookTagDataService.deleteByBookId(book.id) // TODO
        bookDataService.delete(book.id)

        then: 'a new audit instance is created'
        auditDataService.count() == old(auditDataService.count()) + 1

        cleanup:
        auditDataService.deleteByBookId(book.id)
        tagDataService.delete(tagDataService.find('abc')?.id)
        tagDataService.delete(tagDataService.find('Changed Title')?.id)
    }

    Audit lastAudit() {
        int offset = Math.max(((auditDataService.count() as int) - 1), 0)
        auditDataService.findAll([max: 1, offset: offset]).first()
    }


}
