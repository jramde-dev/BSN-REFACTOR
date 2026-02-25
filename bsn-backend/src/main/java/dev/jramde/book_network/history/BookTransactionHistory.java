package dev.jramde.book_network.history;

import dev.jramde.book_network.book.Book;
import dev.jramde.book_network.common.AbstractBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class BookTransactionHistory extends AbstractBaseEntity {

    /**
     * The book which has borrowed.
     */
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    /**
     * The borrower.
     */
    // @ManyToOne
    // @JoinColumn(name = "user_id")
    // private AppUser user;
    @Column(name = "borrower_id")
    private String borrowerId;

    private boolean returned;
    private boolean returnApproved;
}
