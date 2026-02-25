package dev.jramde.book_network.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, Integer>, JpaSpecificationExecutor<Book> {

    /**
     * Return all public book not belong to the current user.
     * The createdBy will be set only when the connected user will perform the creation of a book.
     * @param pageable : the pagination
     * @param userId : current user
     * @return Books
     */
    @Query("""
            SELECT book FROM Book book
            WHERE book.archived = false
            AND book.shareable = true
            AND book.createdBy != :userId
            """)
    Page<Book> findAllSharedBooksExceptThoseBelongToTheCurrentUser(Pageable pageable, String userId);
}
