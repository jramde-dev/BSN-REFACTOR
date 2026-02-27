import {Component, OnInit} from '@angular/core';
import {PageResponseBorredBookResponse} from "../../../../services/models/page-response-borred-book-response";
import {BorrowedBookResponse} from "../../../../services/models/borrowed-book-response";
import {BookService} from "../../../../services/services/book.service";
import {FeedbackRequest} from "../../../../services/models/feedback-request";
import {FeedbackService} from "../../../../services/services/feedback.service";
import {BookServiceRefactored} from "../../../../services/services/book.service.refactored";
import {PaginationResponse} from "../../../../services/models/api-response";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-borrowed-books',
  templateUrl: './borrowed-books.component.html',
  styleUrls: ['./borrowed-books.component.scss']
})
export class BorrowedBooksComponent implements OnInit {
  borrowedBooks: PaginationResponse<BorrowedBookResponse> = {};
  feedbackRequest: FeedbackRequest = {bookId: 0, comment: "", note: 0}
  page: number = 0;
  size: number = 5;
  selectedBook: BorrowedBookResponse | undefined = undefined;

  constructor(
    // private bookService: BookService,
    private bookService: BookServiceRefactored,
    private toastrService: ToastrService,
    private feedbackService: FeedbackService) {
  }

  ngOnInit() {
    this.findAllBorrowedBooks();
  }

  findAllBorrowedBooks() {
    // this.bookService.findAllBorrowedBooks({page: this.page, size: this.size}).subscribe({
    this.bookService.getBorrowedBooks({page: this.page, size: this.size}).subscribe({
      next: (borrowedBooks: PageResponseBorredBookResponse) => {
        this.borrowedBooks = borrowedBooks;
      }
    })
  }

  onReturnBorrowedBook(book: BorrowedBookResponse) {
    this.selectedBook = book;
    this.feedbackRequest.bookId = book.id as number;
    console.log("Borrowed book id is : ", this.feedbackRequest.bookId)
  }

  onReturnBook(withFeedback: boolean) {
   // this.bookService.returnBorrowBook({
    this.bookService.returnBorrowedBook(this.selectedBook?.id as number).subscribe({
      next: () => {
        if (withFeedback) {
          this.giveFeedback();
        }
        this.selectedBook = undefined;
        this.findAllBorrowedBooks();
      }
    });
  }

  private giveFeedback() {
    this.feedbackService.saveFeedback({
      body: this.feedbackRequest
    }).subscribe({
      next: () => {
        this.toastrService.success("Your feedback has bee submitted!", "Success")
      }
    });
  }

  onGoToTheFirstPage() {
    this.page = 0;
    this.findAllBorrowedBooks();
  }

  onGoToPreviousPage() {
    this.page--;
    this.findAllBorrowedBooks();
  }

  onGoToPage(page: number) {
    this.page = page;
    this.findAllBorrowedBooks();
  }

  onGoToNextPage() {
    this.page++;
    this.findAllBorrowedBooks();
  }

  onGoToLastPage() {
    this.page = this.borrowedBooks.totalPages as number - 1;
    this.findAllBorrowedBooks();
  }

  get isLastPage(): boolean {
    return this.page == this.borrowedBooks.totalPages as number - 1;
  }
}
