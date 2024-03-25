import { HttpInterceptorFn } from '@angular/common/http';
import { catchError } from 'rxjs/operators';

import { environment } from '@env/environment';
import { Logger } from '../logger.service';

const log = new Logger('ErrorHandlerInterceptor');

export const errorHandlerInterceptor: HttpInterceptorFn = (request, next) => {
  return next(request).pipe(catchError((error) => {
    if (!environment.production) {
      // Do something with the error
      log.error('Request error', error);
    }
    throw error;
  }));
};

