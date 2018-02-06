package demo

import grails.events.annotation.gorm.Listener
import groovy.transform.CompileStatic
import org.grails.datastore.mapping.engine.event.AbstractPersistenceEvent
import org.grails.datastore.mapping.engine.event.PostInsertEvent
import org.grails.datastore.mapping.engine.event.PostUpdateEvent
import org.grails.datastore.mapping.engine.event.PreDeleteEvent
import org.grails.datastore.mapping.engine.event.PreInsertEvent
import org.grails.datastore.mapping.engine.event.PreUpdateEvent

@CompileStatic
class TagGenerationService {

    TagDataService tagDataService
    BookTagDataService bookTagDataService

    @Listener(Book)
    void afterInsert(PostInsertEvent event) {
        saveBookTag(event) //<1>
    }

    @Listener(Book)
    void afterUpdate(PostUpdateEvent event) {
        Long bookId = bookId(event)
        if ( bookId && ((Book) event.entityObject).isDirty('title') ) {
            saveBookTag(event) //<1>
        }
    }

    @Listener(Book)
    void beforeUpdate(PreUpdateEvent event) {
        Long bookId = bookId(event)
        if ( bookId && ((Book) event.entityObject).isDirty('title') ) {
            deleteBookTags(bookId)
        }

    }

    @Listener(Book)
    void beforeDelete(PreDeleteEvent event) {
        Long bookId = bookId(event)
        deleteBookTags(bookId)
    }

    private Long bookId(AbstractPersistenceEvent event) {
        if ( event.entityObject instanceof Book ) {
            return ((Book) event.entityObject).id
        }
        null
    }


    private void deleteBookTags(Long bookId) {
        if ( bookId ) {
            bookTagDataService.findAllByBookId(bookId)?.each { BookTag bookTag ->
                bookTagDataService.delete(bookTag.id)
            }
        }
    }

    private void saveBookTag(AbstractPersistenceEvent event) { //<1>
        Long bookId = bookId(event)
        if ( bookId ) {
            String title = event.entityAccess.getProperty('title') as String
            Tag tag = tagDataService.find(title)
            if ( !tag ) {
                tag = tagDataService.save(title)
            }

            BookTag bookTag = bookTagDataService.findByBookIdAndTag(bookId, tag)
            if ( !bookTag ) {
                bookTag = bookTagDataService.save(Book.load(bookId), tag)
            }
        }
    }
}

