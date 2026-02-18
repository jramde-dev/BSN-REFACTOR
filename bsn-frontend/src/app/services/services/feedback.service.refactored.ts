import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { FeedbackRequest } from '../models/feedback-request';
import { FeedbackResponse } from '../models/feedback-response';
import { ApiResponse, PaginationResponse } from '../models/api-response';

@Injectable({
  providedIn: 'root'
})
export class FeedbackService {
  private readonly baseUrl = '/api/feedbacks';

  constructor(private http: HttpClient) {}

  // Feedback operations
  saveFeedback(feedback: FeedbackRequest): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.baseUrl}`, feedback);
  }

  getFeedbacksByBookId(bookId: number, params?: { page?: number; size?: number; sort?: string }): Observable<PaginationResponse<FeedbackResponse>> {
    const httpParams = this.buildHttpParams(params);
    return this.http.get<PaginationResponse<FeedbackResponse>>(`${this.baseUrl}/book/${bookId}`, { params: httpParams });
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
}
