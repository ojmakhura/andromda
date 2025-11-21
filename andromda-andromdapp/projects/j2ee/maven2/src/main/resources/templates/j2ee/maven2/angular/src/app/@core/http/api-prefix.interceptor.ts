import { HttpInterceptorFn } from '@angular/common/http';

import { environment } from '@env/environment';

export const apiPrefixInterceptor: HttpInterceptorFn = (req, next) => {

  // Skip translation files and other public assets
  if (req.url.includes('/i18n/') || /^(http|https):/i.test(req.url)) {
    return next(req);
  }

  // Add API prefix to relative URLs
  req = req.clone({ url: environment.apiUrl + req.url }); 

  // Pass the cloned request with the updated header to the next handler
  return next(req);
};