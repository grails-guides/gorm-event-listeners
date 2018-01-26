package demo

import grails.events.annotation.gorm.Listener
import grails.gorm.transactions.Transactional
import groovy.util.logging.Slf4j
import org.grails.datastore.mapping.engine.event.AbstractPersistenceEvent
import org.grails.datastore.mapping.engine.event.PostInsertEvent
import org.grails.datastore.mapping.engine.event.PostUpdateEvent
import org.grails.datastore.mapping.engine.event.PreDeleteEvent
import org.grails.datastore.mapping.engine.event.PreUpdateEvent
import org.hibernate.BaseSessionEventListener
import org.hibernate.SessionFactory

@Slf4j
@Transactional
class TagGenerationService {

    SessionFactory sessionFactory

    @Listener
    void afterSave(PostInsertEvent event) {
        if (event.entityObject instanceof Book) {
            log.info "After book save..."

            saveBookTag(event)
        }
    }

    @Listener
    void afterUpdate(PostUpdateEvent event) {
        if (event.entityObject instanceof Book) {
            log.info "After book update..."

            saveBookTag(event)
        }
    }

    @Listener
    void beforeUpdate(PreUpdateEvent event) {
        if (event.entityObject instanceof Book) {
            log.info "Before book update..."

            Book book = (Book) event.entityObject
            if (book.isDirty('title')) {
                log.info "book.title dirty..."
                Tag tag = Tag.findOrCreateWhere(name: book.getPersistentValue('title'))
                BookTag bookTag = BookTag.findByBookAndTag(book, tag)
                if (bookTag) bookTag.delete()
            }
        }
    }

    @Listener
    void beforeDelete(PreDeleteEvent event) {
        if (event.entityObject instanceof Book) {
            log.info "Before book delete..."

            Book book = (Book) event.entityObject
            List<BookTag> bookTags = BookTag.findAllByBook(book)
            bookTags*.delete()
        }
    }

    private void saveBookTag(AbstractPersistenceEvent event) {
        log.info "saveBookTag..."
        EventListener tagListener = new EventListener({
            Book book = Book.load(((Book) event.entityObject).id)
            Tag tag = Tag.findOrCreateWhere(name: book.title).save()
            BookTag bookTag = BookTag.findByBookAndTag(book, tag)

            if(!bookTag) {
                log.info "Saving BookTag for $book & $tag"
                BookTag.create(book, tag)
            }
        })
        sessionFactory.currentSession.addEventListeners(tagListener)
    }

    class EventListener extends BaseSessionEventListener {

        Boolean fired = false //<1> Make sure this listener only executes once
        Closure handler

        EventListener(Closure c) {
            handler = c
        }

        @Override
        void transactionCompletion(boolean successful) {
            if (successful && !fired) {
                fired = true

                BookTag.withNewTransaction handler
            }
        }
    }
}
