import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {BookResponse} from "../../../../services/models/book-response";
import {BookServiceRefactored} from "../../../../services/services/book.service.refactored";
import {PaginationResponse} from "../../../../services/models/api-response";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-book-list',
  templateUrl: './book-list.component.html',
  styleUrls: ['./book-list.component.scss']
})
export class BookListComponent implements OnInit {
  bookResponse: PaginationResponse<BookResponse> = {};
  page: number = 0;
  size: number = 2;
  message!: string;
  level!: string;

  constructor(
    // private bookService: BookService,
    private bookService: BookServiceRefactored,
    private toastrService: ToastrService,
    private router: Router) {
  }

  ngOnInit(): void {
    this.findAllBooks();
  }

  /**
   * Retrieve all the books available in the database.
   */
  findAllBooks() {
    this.bookService.getAllBooks({page: this.page, size: this.size}).subscribe({
      next: (books) => {
        this.bookResponse = books;
      }
    })
  }

  onGoToTheFirstPage() {
    this.page = 0;
    this.findAllBooks();
  }

  onGoToPreviousPage() {
    this.page--;
    this.findAllBooks();
  }

  onGoToPage(page: number) {
    this.page = page;
    this.findAllBooks();
  }

  onGoToNextPage() {
    this.page++;
    this.findAllBooks();
  }

  onGoToLastPage() {
    this.page = this.bookResponse.totalPages as number - 1;
    this.findAllBooks();
  }

  get isLastPage(): boolean {
    return this.page == this.bookResponse.totalPages as number - 1;
  }

  onBorrowBook(book: BookResponse) {
    this.message = "";
    // this.bookService.borrowBook({"book-id": book.id as number}).subscribe({
    this.bookService.borrowBook(book.id as number).subscribe({
      next: () => {
        this.level = "success";
        this.toastrService.success("Book successfully added to your list.", "Success")
      },
      error: (err) => {
        console.log(err);
        this.level = "error";
        this.toastrService.error(err.error.businessErrorMessage, "Error");
      }
    })
  }
}
