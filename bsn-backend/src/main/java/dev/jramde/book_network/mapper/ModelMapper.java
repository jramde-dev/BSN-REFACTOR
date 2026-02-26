package dev.jramde.book_network.mapper;

import dev.jramde.book_network.book.Book;
import dev.jramde.book_network.book.BookRequest;
import dev.jramde.book_network.book.BookResponse;
import dev.jramde.book_network.book.BorredBookResponse;
import dev.jramde.book_network.feedback.Feedback;
import dev.jramde.book_network.feedback.FeedbackRequest;
import dev.jramde.book_network.feedback.FeedbackResponse;
import dev.jramde.book_network.file.AppFileUtils;
import dev.jramde.book_network.history.BookTransactionHistory;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Mapper class to map request to entity and entity to response.
 */

@Service
public class ModelMapper {

    public Book maps(BookRequest bookRequest) {
        return Book.builder()
                .id(bookRequest.id())
                .title(bookRequest.title())
                .author(bookRequest.author())
                .isbn(bookRequest.isbn())
                .synopsis(bookRequest.synopsis())
                .shareable(bookRequest.shareable())
                .archived(false)
                .build();
    }

    public BookResponse maps(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .isbn(book.getIsbn())
                .synopsis(book.getSynopsis())
                //.owner(book.getOwner().getFullName())
                .owner(book.getCreatedBy())
                .rate(book.getRate())
                .shareable(book.isShareable())
                .archived(book.isArchived())
                .cover(AppFileUtils.readFileFromLocation(book.getBookCover()))
                .build();
    }

    public BorredBookResponse maps(BookTransactionHistory history) {
        return BorredBookResponse.builder()
                .id(history.getBook().getId())
                .title(history.getBook().getTitle())
                .author(history.getBook().getAuthor())
                .isbn(history.getBook().getIsbn())
                .rate(history.getBook().getRate())
                .returned(history.isReturned())
                .returnApproved(history.isReturnApproved())
                .build();
    }

    public Feedback maps(FeedbackRequest request) {
        return Feedback.builder()
                .note(request.note())
                .comment(request.comment())
                .book(Book.builder()
                        .id(request.bookId())
                        .build())
                .build();
    }

    public FeedbackResponse maps(Feedback feedback, String userId) {
        return FeedbackResponse.builder()
                .note(feedback.getNote())
                .comment(feedback.getComment())
                .ownFeedback(Objects.equals(feedback.getCreatedBy(), userId))
                .build();
    }
}
