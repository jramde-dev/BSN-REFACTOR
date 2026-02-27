import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {BookRequest} from "../../../../services/models/book-request";
import {ActivatedRoute, Router} from "@angular/router";
import {BookServiceRefactored} from "../../../../services/services/book.service.refactored";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-manage-book',
  templateUrl: './manage-book.component.html',
  styleUrls: ['./manage-book.component.scss']
})
export class ManageBookComponent implements OnInit {
  defaultImage: string = "https://images.unsplash.com/photo-1768124362942-3e4113f6f73e?q=80&w=784&auto=format";
  selectedPicture: string | undefined;
  selectedBookCover: any;
  errorMsg: Array<string> = [];
  bookRequest: BookRequest = {author: "", isbn: "", synopsis: "", title: ""};

  constructor(
    //private bookService: BookService,
    private bookService: BookServiceRefactored,
    private toastrService: ToastrService,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private cdr: ChangeDetectorRef) {
  }

  ngOnInit() {
    const bookId = Number(this.activatedRoute.snapshot.paramMap.get('bookId'));
    if (bookId) {
      this.bookService.getBookById(bookId).subscribe({
        next: (bookResponse) => {
          this.bookRequest = {
            id: bookResponse.id as number,
            title: bookResponse.title as string,
            author: bookResponse.author as string,
            isbn: bookResponse.isbn as string,
            synopsis: bookResponse.synopsis as string,
            shareable: bookResponse.shareable
          }

          if (bookResponse.cover) {
            this.selectedPicture = 'data:image/jpg;base64,' + bookResponse.cover;
          }
        },
        error: (error) => {
          console.error('Failed to load book:', error);
          this.errorMsg = error.error?.businessValidationErrors || ['Failed to load book data'];
        }
      })
    }
  }

  onSelectedFile(event: any) {
    if (event.target.files && event.target.files[0]) {
      this.selectedBookCover = event.target.files[0];
      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.selectedPicture = e.target.result as string;
      }
      reader.readAsDataURL(this.selectedBookCover);
    }
  }

  onSaveBook() {
    this.bookService.createBook(this.bookRequest).subscribe({
      // book id returned by the backend
      next: (bookId) => {
        if (this.selectedBookCover) {
          this.bookService.uploadCoverPicture(bookId as number, this.selectedBookCover).subscribe({
            next: () => {
              this.toastrService.success("Book saved successfully", "success")
              this.router.navigate(['/books/my-books']);
            },
            error: (error) => {
              console.log('Cover upload error:', error);
              this.toastrService.error(error.error?.businessValidationErrors, "Error")
            }
          })
        }
      },
      error: (error) => {
        this.toastrService.error(error.error.businessValidationErrors, "Error")
      }
    })
  }
}
