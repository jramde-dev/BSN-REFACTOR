package dev.jramde.book_network.book;

import org.springframework.data.jpa.domain.Specification;

public class BookSpecification {

    /**
     * Returns a specification to find books by owner id.
     * owner id is the id of the owner of the book (private AppUser owner)
     * @param ownerId : the owner id
     * @return the specification
     */
    public static Specification<Book> withOwner(String ownerId) {
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.equal(root.get("createdBy"), ownerId);
    }
}
