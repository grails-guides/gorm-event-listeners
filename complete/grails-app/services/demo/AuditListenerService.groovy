package demo

import grails.events.annotation.Subscriber
import grails.events.annotation.gorm.Listener
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.grails.datastore.mapping.engine.event.AbstractPersistenceEvent
import org.grails.datastore.mapping.engine.event.PostDeleteEvent
import org.grails.datastore.mapping.engine.event.PostInsertEvent
import org.grails.datastore.mapping.engine.event.PostUpdateEvent
import org.grails.datastore.mapping.engine.event.PreDeleteEvent
import org.grails.datastore.mapping.engine.event.PreInsertEvent
import org.grails.datastore.mapping.engine.event.PreUpdateEvent

@Slf4j
@CompileStatic
class AuditListenerService {

    AuditDataService auditDataService

    Long bookId(AbstractPersistenceEvent event) {
        if ( event.entityObject instanceof Book ) {
            return ((Book) event.entityObject).id // <3>
        }
        null
    }

    @Listener(Book) // <1>
    void afterInsert(PostInsertEvent event) {
        Long bookId = bookId(event)
        if ( bookId ) {
            log.info 'After book save...'
            auditDataService.save('Book saved', bookId)
        }
    }

    @Listener(Book) // <1>
    void afterUpdate(PostUpdateEvent event) {
        Long bookId = bookId(event)
        if ( bookId ) {
            log.info "After book update..."
            auditDataService.save('Book updated', bookId)
        }
    }

    @Listener(Book) // <1>
    void afterDelete(PostDeleteEvent event) {
        log.info 'beforeInsert...'
        Long bookId = bookId(event)
        if ( bookId ) {
            log.info "Before book delete..."
            auditDataService.save('Book deleted', bookId)
        }
    }
}
