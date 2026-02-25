package dev.jramde.book_network.feedback;

import dev.jramde.book_network.book.Book;
import dev.jramde.book_network.book.BookRepository;
import dev.jramde.book_network.common.PageResponse;
import dev.jramde.book_network.exception.OperationNotPermittedException;
import dev.jramde.book_network.mapper.ModelMapper;
import dev.jramde.book_network.user.AppUser;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author : Juldas RAMDE, ramde266@gmail.com.
 * @since : 16/10/2025, 03:29
 */

@Service
@RequiredArgsConstructor
public class FeedbackService {
    private final BookRepository bookRepository;
    private final FeedbackRepository feedbackRepository;
    private final ModelMapper mapper;

    public Integer save(FeedbackRequest feedbackRequest, Authentication connectedUser) {
        Book book = bookRepository.findById(feedbackRequest.bookId())
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));
        if (book.isArchived() || !book.isShareable()) {
            throw new OperationNotPermittedException(
                    "You cannot give a feedback for this book because it is archived or unshareable.");
        }

       // AppUser currentUser = (AppUser) connectedUser.getPrincipal();
        if (Objects.equals(book.getCreatedBy(), connectedUser.getName())) {
            throw new OperationNotPermittedException("You cannot feedback your own book.");
        }

        Feedback feedback = mapper.maps(feedbackRequest);
        return feedbackRepository.save(feedback).getId();
    }

    public PageResponse<FeedbackResponse> findFeedbacksByBookId(
            Integer bookId, int page, int size, Authentication connectedUser) {
        Pageable pageable = PageRequest.of(page, size);
        // AppUser currentUser = (AppUser) connectedUser.getPrincipal();
        Page<Feedback> feedbacks = feedbackRepository.findAllByBookId(bookId, pageable);
        List<FeedbackResponse> feedbackResponses = feedbacks
                .stream()
                .map(feedback -> mapper.maps(feedback, connectedUser.getName()))
                .toList();
        return new PageResponse<>(
                feedbackResponses,
                feedbacks.getNumber(),
                feedbacks.getSize(),
                feedbacks.getTotalElements(),
                feedbacks.getTotalPages(),
                feedbacks.isFirst(),
                feedbacks.isLast()
        );
    }
}
