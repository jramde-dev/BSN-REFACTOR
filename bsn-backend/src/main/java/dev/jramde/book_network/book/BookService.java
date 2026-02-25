package dev.jramde.book_network.book;

import dev.jramde.book_network.common.PageResponse;
import dev.jramde.book_network.exception.OperationNotPermittedException;
import dev.jramde.book_network.file.FileStorageService;
import dev.jramde.book_network.history.BookTransactionHistory;
import dev.jramde.book_network.history.BookTransactionHistoryRepository;
import dev.jramde.book_network.mapper.ModelMapper;
import dev.jramde.book_network.user.AppUser;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookTransactionHistoryRepository bookTransactionHistoryRepository;
    private final BookRepository bookRepository;
    private final ModelMapper mapper;
    private final FileStorageService fileStorageService;

    /**
     * Create a new book.
     *
     * @param bookRequest   : the book request
     * @param connectedUser : the connected user (currentUser)
     * @return the id of the created book
     */
    public Integer create(final BookRequest bookRequest, final Authentication connectedUser) {
        // AppUser currentUser = (AppUser) connectedUser.getPrincipal();
        Book book = mapper.maps(bookRequest);
        // book.setOwner(currentUser);
        return bookRepository.save(book).getId();
    }

    public BookResponse findById(Integer bookId) {
        return bookRepository.findById(bookId)
                .map(mapper::maps)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));
    }

    /**
     * Best practice : use pagination because the list can be very long.
     *
     * @param page          : the page number
     * @param size          : the page size
     * @param connectedUser : the connected user (currentUser)
     * @return the page of books
     */
    public PageResponse<BookResponse> findAll(int page, int size, Authentication connectedUser) {
        AppUser currentUser = (AppUser) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Book> books = bookRepository.findAllSharedBooksExceptThoseBelongToTheCurrentUser(
                pageable, currentUser.getId());
        List<BookResponse> bookResponses = books.stream().map(mapper::maps).toList();

        return new PageResponse<>(
                bookResponses,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );
    }

    /**
     * Return all the books of the current currentUser.
     *
     * @param page          : page
     * @param size          : size
     * @param connectedUser : current connected user and currentUser
     * @return listing books
     */
    public PageResponse<BookResponse> findAllByOwner(int page, int size, Authentication connectedUser) {
        AppUser currentUser = (AppUser) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Book> books = bookRepository.findAll(BookSpecification.withOwner(currentUser.getId()), pageable);
        List<BookResponse> bookResponses = books.stream().map(mapper::maps).toList();

        return new PageResponse<>(
                bookResponses,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );
    }

    /**
     * Return the current connected user borrowed book.
     *
     * @param page          : page to filter
     * @param size          : page size
     * @param connectedUser : connected user
     * @return all books
     */
    public PageResponse<BorredBookResponse> findAllBorrowedBooks(int page, int size, Authentication connectedUser) {
        // AppUser currentUser = (AppUser) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<BookTransactionHistory> borrowedBooks = bookTransactionHistoryRepository
                .findAllBorrowedBooks(pageable, connectedUser.getName());
        List<BorredBookResponse> borredBookResponses = borrowedBooks.stream().map(mapper::maps).toList();

        return new PageResponse<>(
                borredBookResponses,
                borrowedBooks.getNumber(),
                borrowedBooks.getSize(),
                borrowedBooks.getTotalElements(),
                borrowedBooks.getTotalPages(),
                borrowedBooks.isFirst(),
                borrowedBooks.isLast()
        );
    }

    /**
     * Return all books which were borrowed and borrowed have returned them.
     *
     * @param page          : the number of pages we want to sort
     * @param size          : the size
     * @param connectedUser : the currentUser
     * @return all books returned
     */
    public PageResponse<BorredBookResponse> findAllReturnedBooks(int page, int size, Authentication connectedUser) {
        // AppUser currentUser = (AppUser) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<BookTransactionHistory> borrowedBooks = bookTransactionHistoryRepository.findAllReturnedBooks(
                pageable, connectedUser.getName());
        List<BorredBookResponse> borredBookResponses = borrowedBooks.stream().map(mapper::maps).toList();

        return new PageResponse<>(
                borredBookResponses,
                borrowedBooks.getNumber(),
                borrowedBooks.getSize(),
                borrowedBooks.getTotalElements(),
                borrowedBooks.getTotalPages(),
                borrowedBooks.isFirst(),
                borrowedBooks.isLast()
        );
    }

    /**
     * Change book status making it shareable or not.
     * connectedUser.getName() will now return the keycloak user id.
     *
     * @param bookId        : book to change status
     * @param connectedUser : connected user
     * @return book id.
     */
    public Integer changeShareableStatus(Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new EntityNotFoundException("Book not found"));
        // AppUser currentUser = (AppUser) connectedUser.getPrincipal();
        if (!Objects.equals(book.getCreatedBy(), connectedUser.getName())) {
            throw new OperationNotPermittedException("You are not the currentUser of this book");
        }
        book.setShareable(!book.isShareable());
        bookRepository.save(book);
        return bookId;
    }

    /**
     * Change book status archiving it or not.
     *
     * @param bookId        : book to change status
     * @param connectedUser : connected user
     * @return book id.
     */
    public Integer changeArchiveStatus(Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new EntityNotFoundException("Book not found"));
        // AppUser currentUser = (AppUser) connectedUser.getPrincipal();
        if (!Objects.equals(book.getCreatedBy(), connectedUser.getName())) {
            throw new OperationNotPermittedException("You are not the currentUser of this book");
        }
        book.setArchived(!book.isArchived());
        bookRepository.save(book);
        return bookId;
    }

    /**
     * Borrow a book.
     *
     * @param bookId        : the book to borrow
     * @param connectedUser : connected user borrows the book.
     * @return the borrow book id
     */
    public Integer borrowBook(Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new EntityNotFoundException("Book not found"));
        if (book.isArchived() || !book.isShareable()) {
            throw new OperationNotPermittedException("You cannot borrow this book because it is archived or unshareable.");
        }

        // AppUser currentUser = (AppUser) connectedUser.getPrincipal();
        if (Objects.equals(book.getCreatedBy(), connectedUser.getName())) {
            throw new OperationNotPermittedException("You cannot borrow your own book.");
        }

        final boolean isAlreadyBorrowed = bookTransactionHistoryRepository
                .isAlreadyBorrowedByUser(bookId, connectedUser.getName());
        if (isAlreadyBorrowed) {
            throw new OperationNotPermittedException("You have already borrowed the requested book.");
        }

        BookTransactionHistory bookTransactionHistory = BookTransactionHistory.builder()
                .borrowerId(connectedUser.getName())
                .book(book)
                .returned(false)
                .returnApproved(false)
                .build();
        return bookTransactionHistoryRepository.save(bookTransactionHistory).getId();
    }

    /**
     * Return a borrowed book.
     *
     * @param bookId        : the book id
     * @param connectedUser : current user
     * @return return book id
     */
    public Integer returnBorrowBook(Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new EntityNotFoundException("Book not found"));
        if (book.isArchived() || !book.isShareable()) {
            throw new OperationNotPermittedException("You cannot borrow this book because it is archived or unshareable.");
        }

        // AppUser currentUser = (AppUser) connectedUser.getPrincipal();
        if (Objects.equals(book.getCreatedBy(), connectedUser.getName())) {
            throw new OperationNotPermittedException("You cannot borrow or return your own book.");
        }

        BookTransactionHistory bookTransactionHistory = bookTransactionHistoryRepository
                .findByBookIdAndUserId(bookId, connectedUser.getName())
                .orElseThrow(() -> new OperationNotPermittedException("You did not borrow this book."));
        bookTransactionHistory.setReturned(true);
        return bookTransactionHistoryRepository.save(bookTransactionHistory).getId();
    }

    /**
     * Approve return a borrowed book.
     *
     * @param bookId        : the book id
     * @param connectedUser : current user
     * @return return book id
     */
    public Integer approveReturnBorrowBook(Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new EntityNotFoundException("Book not found"));
        if (book.isArchived() || !book.isShareable()) {
            throw new OperationNotPermittedException(
                    "You cannot approve this book because it is archived or unshareable.");
        }

        // AppUser currentUser = (AppUser) connectedUser.getPrincipal();
        if (!Objects.equals(book.getCreatedBy(), connectedUser.getName())) {
            throw new OperationNotPermittedException("You cannot return a book that you do not own.");
        }

        BookTransactionHistory bookTransactionHistory = bookTransactionHistoryRepository
                .findByBookIdAndOwnerId(bookId, connectedUser.getName())
                .orElseThrow(() -> new OperationNotPermittedException("This book is not returned."));
        bookTransactionHistory.setReturnApproved(true);
        return bookTransactionHistoryRepository.save(bookTransactionHistory).getId();
    }


    /**
     * Upload book cover picture.
     *
     * @param connectedUser
     * @param bookId
     * @param file
     */
    public void uploadCoverPicture(Authentication connectedUser, Integer bookId, MultipartFile file) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new EntityNotFoundException("Book not found"));
        AppUser currentUser = (AppUser) connectedUser.getPrincipal();

        // Will also store
        String bookCoverFilePath = fileStorageService.getCoverPath(file, currentUser.getId());
        book.setBookCover(bookCoverFilePath);
        bookRepository.save(book);
    }
}
