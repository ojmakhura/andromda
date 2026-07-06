import { Injectable, WritableSignal } from "@angular/core";

@Injectable({
  providedIn: 'root',
})
export class ErrorHandler {
    static handleError(error: any, errorSignal: WritableSignal<string | null>): void {
        
        const message = error?.error?.message || error?.message || 'An unexpected error occurred. Please try again.';
        errorSignal.set(message);
    }
}