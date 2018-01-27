package demo

import grails.events.annotation.gorm.Listener
import grails.gorm.transactions.Transactional
import org.grails.datastore.mapping.engine.event.AbstractPersistenceEvent
import org.grails.datastore.mapping.engine.event.PostInsertEvent
import org.grails.datastore.mapping.engine.event.PostUpdateEvent
import org.grails.datastore.mapping.engine.event.PreDeleteEvent
import org.grails.datastore.mapping.engine.event.PreUpdateEvent

@Transactional
class TagGenerationService {

    @Listener(Book)
    void afterSave(PostInsertEvent event) {
        saveBookTag(event) //<1>
    }

    @Listener(Book)
    void afterUpdate(PostUpdateEvent event) {
        saveBookTag(event) //<1>
    }

    @Listener(Book)
    void beforeUpdate(PreUpdateEvent event) {
        Book book = Book.load(((Book) event.entityObject).id) //<2>

        if (book.isDirty('title')) { //<3>
            Tag tag = Tag.findOrCreateWhere(name: book.getPersistentValue('title')) //<4>
            BookTag bookTag = BookTag.findByBookAndTag(book, tag)
            if (bookTag) bookTag.delete()
        }
    }

    @Listener(Book)
    void beforeDelete(PreDeleteEvent event) {
        Book book = Book.load(((Book) event.entityObject).id)
        List<BookTag> bookTags = BookTag.findAllByBook(book)
        bookTags*.delete()
    }

    private static saveBookTag(AbstractPersistenceEvent event) { //<1>
        Book book = Book.load(((Book) event.entityObject).id)
        Tag tag = Tag.findOrCreateWhere(name: book.title).save()
        BookTag bookTag = BookTag.findByBookAndTag(book, tag)

        if (!bookTag) {
            BookTag.create(book, tag)
        }
    }
}

