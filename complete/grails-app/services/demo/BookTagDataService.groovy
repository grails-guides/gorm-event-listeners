package demo

import grails.gorm.services.Service
import grails.gorm.services.Where
import grails.gorm.transactions.Transactional
import groovy.transform.CompileStatic

@CompileStatic
@Service(BookTag)
abstract class BookTagDataService {
    abstract void delete(Serializable id)

    abstract BookTag save(Book book, Tag tag)

    @Where({ book == book })
    abstract void deleteByBook(Book book)

    @Where({ book.id == bookId && tag == tag })
    abstract BookTag findByBookIdAndTag(Long bookId, Tag tag)

    @Where({ book.id == bookId })
    abstract List<BookTag> findAllByBookId(Long bookId)

    abstract  Number count()

    abstract Number countByBook(Book book)

    @Transactional
    void deleteByBookId(Long bookId) {
        deleteByBook(Book.load(bookId))
    }

}