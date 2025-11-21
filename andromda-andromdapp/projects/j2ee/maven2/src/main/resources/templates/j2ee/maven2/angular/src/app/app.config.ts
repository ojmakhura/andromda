import { ApplicationConfig, importProvidersFrom } from '@angular/core';
import { provideRouter, RouteReuseStrategy } from '@angular/router';

import { routes } from './app.routes';
import { provideAnimations } from '@angular/platform-browser/animations';
import { provideHttpClient, withFetch, withInterceptors, withInterceptorsFromDi, HttpClient } from '@angular/common/http';
import { CUSTOM_DATE_FORMATS } from './@shared/custom-date-formats';
import { MAT_DATE_FORMATS } from '@angular/material/core';
import { RouteReusableStrategy } from './@core/route-reusable-strategy';
import { MAT_FORM_FIELD_DEFAULT_OPTIONS } from '@angular/material/form-field';
import { apiPrefixInterceptor } from './@core/http/api-prefix.interceptor';
import { errorHandlerInterceptor } from './@core/http/error-handler.interceptor';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { Observable, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { provideToastr } from 'ngx-toastr';

export class CustomTranslateLoader implements TranslateLoader {
  constructor(private http: HttpClient) {}

  getTranslation(lang: string): Observable<any> {
    return this.http.get(`/i18n/${lang}.json`).pipe(
      catchError(() => of({}))
    );
  }
}

export function HttpLoaderFactory(http: HttpClient) {
  return new CustomTranslateLoader(http);
}

export const appConfig = (env: any) => {
  return {
    providers: [
      provideRouter(routes),
      provideAnimations(),
      provideHttpClient(
        withFetch(),
        withInterceptorsFromDi(),
        withInterceptors([
          apiPrefixInterceptor, 
          errorHandlerInterceptor, 
        ]),
      ),
      provideToastr({
        timeOut: 3000,
        positionClass: 'toast-top-right',
        preventDuplicates: true,
        progressBar: true,
        closeButton: true,
        newestOnTop: true,
        enableHtml: true,
        tapToDismiss: true,
        maxOpened: 5,
        autoDismiss: true
      }),
      importProvidersFrom(
        TranslateModule.forRoot({
          defaultLanguage: 'en',
          loader: {
            provide: TranslateLoader,
            useFactory: HttpLoaderFactory,
            deps: [HttpClient]
          }
        }),
      ),
      { provide: MAT_FORM_FIELD_DEFAULT_OPTIONS, useValue: { appearance: 'outline' } },
      {
        provide: RouteReuseStrategy,
        useClass: RouteReusableStrategy,
      },
      { provide: MAT_DATE_FORMATS, useValue: CUSTOM_DATE_FORMATS },
    ],
  } as ApplicationConfig;
};