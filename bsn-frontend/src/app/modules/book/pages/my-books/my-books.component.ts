import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {BookResponse} from "../../../../services/models/book-response";
import {PaginationResponse} from "../../../../services/models/api-response";
import {BookServiceRefactored} from "../../../../services/services/book.service.refactored";

@Component({
  selector: 'app-my-books',
  templateUrl: './my-books.component.html',
  styleUrls: ['./my-books.component.scss']
})
export class MyBooksComponent implements OnInit {
  bookResponse: PaginationResponse<BookResponse> = {};
  page: number = 0;
  size: number = 5;

  constructor(
    // private bookService: BookService,
    private bookService: BookServiceRefactored,
    private router: Router) {
  }

  ngOnInit(): void {
    this.findAllBooksByOwner();
  }

  /**
   * Retrieve all the books available in the database.
   */
  findAllBooksByOwner() {
    this.bookService.getBooksByOwner({page: this.page, size: this.size}).subscribe({
      next: (books) => {
        this.bookResponse = books;
      }
    })
  }

  onGoToTheFirstPage() {
    this.page = 0;
    this.findAllBooksByOwner();
  }

  onGoToPreviousPage() {
    this.page--;
    this.findAllBooksByOwner();
  }

  onGoToPage(page: number) {
    this.page = page;
    this.findAllBooksByOwner();
  }

  onGoToNextPage() {
    this.page++;
    this.findAllBooksByOwner();
  }

  onGoToLastPage() {
    this.page = this.bookResponse.totalPages as number - 1;
    this.findAllBooksByOwner();
  }

  get isLastPage(): boolean {
    return this.page == this.bookResponse.totalPages as number - 1;
  }

  /**
   * Archive a book.
   * @param book : book to archive
   */
  archiveBook(book: BookResponse) {
    this.bookService.changeArchiveStatus(book.id as number).subscribe({
      next: () => {
        book.archived = !book.archived;
      }
    });
  }

  /**
   * Share a book.
   * @param book : book to share
   */
  shareBook(book: BookResponse) {
    this.bookService.changeShareableStatus(book.id as number).subscribe({
      next: () => {
        book.shareable = !book.shareable;
      }
    });
  }

  editBook(book: BookResponse) {
    this.router.navigate(['/books', 'manage', book.id]);
  }
}
