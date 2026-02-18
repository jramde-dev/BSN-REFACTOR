import {Component, OnInit} from '@angular/core';
import {PageResponseBorredBookResponse} from "../../../../services/models/page-response-borred-book-response";
import {BookService} from "../../../../services/services/book.service";
import {BorrowedBookResponse} from "../../../../services/models/borrowed-book-response";
import {PaginationResponse} from "../../../../services/models/api-response";
import {BookServiceRefactored} from "../../../../services/services/book.service.refactored";

@Component({
  selector: 'app-returned-books',
  templateUrl: './returned-books.component.html',
  styleUrls: ['./returned-books.component.scss']
})
export class ReturnedBooksComponent implements OnInit {
  returnedBooks: PaginationResponse<BorrowedBookResponse> = {};
  page: number = 0;
  size: number = 5;
  message: string = "";
  level: string = "success";

  constructor(
    // private bookService: BookService,
    private bookService: BookServiceRefactored,
  ) {
  }

  ngOnInit() {
    this.findAllReturnedBooks();
  }

  findAllReturnedBooks() {
    this.bookService.getReturnedBooks({page: this.page, size: this.size}).subscribe({
      next: (returnedBooks) => {
        this.returnedBooks = returnedBooks;
      }
    })
  }

  onApproveReturnedBook(book: BorrowedBookResponse) {
    if (!book.returned) {
      this.level = "error";
      this.message = "The book is not yet returned.";
      return;
    }

    this.bookService.approveReturnedBook(book.id as number).subscribe({
      next: () => {
        this.level = "success";
        this.message = "Returned book approved !";
        this.findAllReturnedBooks();
      }
    });
  }

  onGoToTheFirstPage() {
    this.page = 0;
    this.findAllReturnedBooks();
  }

  onGoToPreviousPage() {
    this.page--;
    this.findAllReturnedBooks();
  }

  onGoToPage(page: number) {
    this.page = page;
    this.findAllReturnedBooks();
  }

  onGoToNextPage() {
    this.page++;
    this.findAllReturnedBooks();
  }

  onGoToLastPage() {
    this.page = this.returnedBooks.totalPages as number - 1;
    this.findAllReturnedBooks();
  }

  get isLastPage(): boolean {
    return this.page == this.returnedBooks.totalPages as number - 1;
  }
}
