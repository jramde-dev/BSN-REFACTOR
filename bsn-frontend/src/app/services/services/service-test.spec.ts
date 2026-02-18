import {TestBed} from '@angular/core/testing';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';

import {BookService} from './book.service.refactored';
import {AuthenticationService} from './authentication.service.refactored';
import {FeedbackService} from './feedback.service.refactored';
import {BookRequest, BookResponse} from '../models/book-request';
import {AuthenticationRequest, AuthenticationResponse} from '../models/authentication-request';
import {FeedbackRequest, FeedbackResponse} from '../models/feedback-request';
import {ApiResponse, PaginationResponse} from '../models/api-response';

describe('Refactored Services', () => {
  let bookService: BookService;
  let authService: AuthenticationService;
  let feedbackService: FeedbackService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        BookService,
        AuthenticationService,
        FeedbackService
      ]
    });

    bookService = TestBed.inject(BookService);
    authService = TestBed.inject(AuthenticationService);
    feedbackService = TestBed.inject(FeedbackService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  describe('BookService', () => {
    it('should get all books', () => {
      const mockResponse: PaginationResponse<BookResponse> = {
        content: [{id: 1, title: 'Test Book'}],
        totalElements: 1,
        totalPages: 1,
        first: true,
        last: true
      };

      bookService.getAllBooks().subscribe(response => {
        expect(response.content).toHaveLength(1);
        expect(response.content?.[0].title).toBe('Test Book');
      });

      const req = httpMock.expectOne('/api/books');
      expect(req.request.method).toBe('GET');
      req.flush(mockResponse);
    });

    it('should create a book', () => {
      const bookRequest: BookRequest = {
        title: 'New Book',
        author: 'Test Author',
        isbn: '123456789',
        synopsis: 'Test synopsis'
      };

      const mockResponse: ApiResponse<number> = {
        data: 1,
        success: true
      };

      bookService.createBook(bookRequest).subscribe(response => {
        expect(response.data).toBe(1);
        expect(response.success).toBe(true);
      });

      const req = httpMock.expectOne('/api/books');
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual(bookRequest);
      req.flush(mockResponse);
    });

    it('should get book by id', () => {
      const mockResponse: ApiResponse<BookResponse> = {
        data: {id: 1, title: 'Test Book'},
        success: true
      };

      bookService.getBookById(1).subscribe(response => {
        expect(response.data?.id).toBe(1);
        expect(response.data?.title).toBe('Test Book');
      });

      const req = httpMock.expectOne('/api/books/1');
      expect(req.request.method).toBe('GET');
      req.flush(mockResponse);
    });
  });

  describe('AuthenticationService', () => {
    it('should authenticate user', () => {
      const authRequest: AuthenticationRequest = {
        email: 'test@example.com',
        password: 'password'
      };

      const mockResponse: ApiResponse<AuthenticationResponse> = {
        data: {token: 'jwt-token', refreshToken: 'refresh-token'},
        success: true
      };

      authService.login(authRequest).subscribe(response => {
        expect(response.data?.token).toBe('jwt-token');
        expect(response.success).toBe(true);
      });

      const req = httpMock.expectOne('/api/auth/authenticate');
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual(authRequest);
      req.flush(mockResponse);
    });

    it('should register user', () => {
      const registerRequest = {
        email: 'test@example.com',
        firstName: 'John',
        lastName: 'Doe',
        password: 'password'
      };

      const mockResponse: ApiResponse<void> = {
        success: true
      };

      authService.register(registerRequest).subscribe(response => {
        expect(response.success).toBe(true);
      });

      const req = httpMock.expectOne('/api/auth/register');
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual(registerRequest);
      req.flush(mockResponse);
    });
  });

  describe('FeedbackService', () => {
    it('should save feedback', () => {
      const feedbackRequest: FeedbackRequest = {
        bookId: 1,
        comment: 'Great book!',
        note: 5
      };

      const mockResponse: ApiResponse<number> = {
        data: 1,
        success: true
      };

      feedbackService.saveFeedback(feedbackRequest).subscribe(response => {
        expect(response.data).toBe(1);
        expect(response.success).toBe(true);
      });

      const req = httpMock.expectOne('/api/feedbacks');
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual(feedbackRequest);
      req.flush(mockResponse);
    });

    it('should get feedbacks by book id', () => {
      const mockResponse: PaginationResponse<FeedbackResponse> = {
        content: [{comment: 'Great book!', note: 5}],
        totalElements: 1,
        totalPages: 1,
        first: true,
        last: true
      };

      feedbackService.getFeedbacksByBookId(1).subscribe(response => {
        expect(response.content).toHaveLength(1);
        expect(response.content?.[0].comment).toBe('Great book!');
      });

      const req = httpMock.expectOne('/api/feedbacks/book/1');
      expect(req.request.method).toBe('GET');
      req.flush(mockResponse);
    });
  });
});
