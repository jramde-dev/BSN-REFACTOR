import {HttpClient, HttpParams} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';

import {BookRequest} from '../models/book-request';
import {BookResponse} from '../models/book-response';
import {BorrowedBookResponse} from '../models/borrowed-book-response';
import {ApiResponse, PaginationResponse} from '../models/api-response';

@Injectable({
  providedIn: 'root'
})
export class BookServiceRefactored {
  private readonly baseUrl = 'http://localhost:8080/api/v1';

  constructor(private http: HttpClient) {
  }

  // Book CRUD operations
  getAllBooks(params?: { page?: number; size?: number; sort?: string }): Observable<PaginationResponse<BookResponse>> {
    const httpParams = this.buildHttpParams(params);
    return this.http.get<PaginationResponse<BookResponse>>(`${this.baseUrl}/books`, {params: httpParams});
  }

  getBookById(bookId: number): Observable<BookResponse> {
    return this.http.get<BookResponse>(`${this.baseUrl}/books/${bookId}`);
  }

  createBook(bookRequest: BookRequest): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.baseUrl}/books`, bookRequest);
  }

  uploadCoverPicture(bookId: number, file: File): Observable<ApiResponse<void>> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<ApiResponse<void>>(`${this.baseUrl}/books/cover/${bookId}`, formData);
  }

  // Book management operations
  borrowBook(bookId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.baseUrl}/books/borrow/${bookId}`, {});
  }

  returnBorrowedBook(bookId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.baseUrl}/books/borrow/return/${bookId}`, {});
  }

  approveReturnedBook(bookId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.baseUrl}/books/borrow/return/approve/${bookId}`, {});
  }

  changeShareableStatus(bookId: number): Observable<ApiResponse<number>> {
    return this.http.patch<ApiResponse<number>>(`${this.baseUrl}/books/shareable/${bookId}`, {});
  }

  changeArchiveStatus(bookId: number): Observable<ApiResponse<number>> {
    return this.http.patch<ApiResponse<number>>(`${this.baseUrl}/books/archived/${bookId}`, {});
  }

  // Book listing operations
  getBooksByOwner(params?: {
    page?: number;
    size?: number;
    sort?: string
  }): Observable<PaginationResponse<BookResponse>> {
    const httpParams = this.buildHttpParams(params);
    return this.http.get<PaginationResponse<BookResponse>>(`${this.baseUrl}/books/owner`, {params: httpParams});
  }

  getBorrowedBooks(params?: {
    page?: number;
    size?: number;
    sort?: string
  }): Observable<PaginationResponse<BorrowedBookResponse>> {
    const httpParams = this.buildHttpParams(params);
    return this.http.get<PaginationResponse<BorrowedBookResponse>>(`${this.baseUrl}/books/borrowed`, {params: httpParams});
  }

  getReturnedBooks(params?: {
    page?: number;
    size?: number;
    sort?: string
  }): Observable<PaginationResponse<BorrowedBookResponse>> {
    const httpParams = this.buildHttpParams(params);
    return this.http.get<PaginationResponse<BorrowedBookResponse>>(`${this.baseUrl}/books/returned`, {params: httpParams});
  }

  // Helper method to build HTTP parameters
  private buildHttpParams(params?: { [key: string]: any }): HttpParams {
    let httpParams = new HttpParams();
    if (params) {
      Object.keys(params).forEach(key => {
        if (params[key] !== undefined && params[key] !== null) {
          httpParams = httpParams.set(key, params[key].toString());
        }
      });
    }
    return httpParams;
  }


  // Backend not implemented
  updateBook(bookId: number, bookRequest: BookRequest): Observable<ApiResponse<void>> {
    return this.http.put<ApiResponse<void>>(`${this.baseUrl}/books/${bookId}`, bookRequest);
  }
}
