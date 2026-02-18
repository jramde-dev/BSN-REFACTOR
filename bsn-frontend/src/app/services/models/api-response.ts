import {BookResponse} from "./book-response";

export interface ApiResponse<T> {
  data?: T;
  first?: boolean;
  last?: boolean;
  number?: number;
  size?: number;
  totalElements?: number;
  totalPages?: number;
  message?: string;
  success?: boolean;
}

export interface PaginationResponse<T> {
  content?: Array<T>;
  first?: boolean;
  last?: boolean;
  number?: number;
  size?: number;
  totalElements?: number;
  totalPages?: number;
  message?: string;
  success?: boolean;
}
