import { HttpInterceptorFn } from '@angular/common/http';

import { environment } from '@env/environment';

export const apiPrefixInterceptor: HttpInterceptorFn = (req, next) => {

  if (!/^(http|https):/i.test(req.url)) {
    req = req.clone({ url: environment.serverUrl + req.url });
  }

  // Pass the cloned request with the updated header to the next handler
  return next(req);
};