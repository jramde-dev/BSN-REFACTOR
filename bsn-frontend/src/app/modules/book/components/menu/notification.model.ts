export interface INotification {
  status?: 'BORROWED' | 'RETURNED' | 'RETURN_APPROVED';
  message?: string;
  bookTitle?: string;
}
