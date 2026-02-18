import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {MainComponent} from "./pages/main/main.component";
import {BookListComponent} from "./pages/book-list/book-list.component";
import {MyBooksComponent} from "./pages/my-books/my-books.component";
import {ManageBookComponent} from "./pages/manage-book/manage-book.component";
import {BorrowedBooksComponent} from "./pages/borrowed-books/borrowed-books.component";
import {ReturnedBooksComponent} from "./pages/returned-books/returned-books.component";
import {authGuard} from "../../core/guard/auth.guard";

const routes: Routes = [
  {
    path: "", component: MainComponent,

    // Because we want to display everything inside the main component
    children: [
      {path: "", title: "Books", component: BookListComponent, canActivate: [authGuard]},
      {path: "my-books", title: "My Books", component: MyBooksComponent, canActivate: [authGuard]},
      {path: "manage", title: "Book Management", component: ManageBookComponent, canActivate: [authGuard]},
      {path: "manage/:bookId", title: "Manage Book", component: ManageBookComponent, canActivate: [authGuard]},
      {path: "borrowed-books", title: "Borrowed Books", component: BorrowedBooksComponent, canActivate: [authGuard]},
      {path: "returned-books", title: "Returned Books", component: ReturnedBooksComponent, canActivate: [authGuard]}
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class BookRoutingModule {
}
