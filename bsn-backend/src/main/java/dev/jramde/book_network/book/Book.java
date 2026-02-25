package dev.jramde.book_network.book;

import dev.jramde.book_network.common.AbstractBaseEntity;
import dev.jramde.book_network.feedback.Feedback;
import dev.jramde.book_network.history.BookTransactionHistory;
import dev.jramde.book_network.user.AppUser;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Book extends AbstractBaseEntity {
    private String title;
    private String author;
    private String isbn; // International Standard Book Number
    private String synopsis;
    private String bookCover;
    private boolean archived;
    private boolean shareable;

    // @ManyToOne
    // @JoinColumn(name = "owner_id")
    // private AppUser owner;

    @OneToMany(mappedBy = "book")
    private List<Feedback> feedbacks;

    @OneToMany(mappedBy = "book")
    private List<BookTransactionHistory> transactionHistories;

    @Transient
    public double getRate() {
        if (feedbacks == null || feedbacks.isEmpty()) {
            return 0.0;
        }
        double rate = feedbacks
                .stream()
                .mapToDouble(Feedback::getNote)
                .average()
                .orElse(0.0);

        // if 3.23 --> 3.0 || 3.65 --> 4
        return Math.round(rate * 10.0) / 10.0;
    }
}
