import {
  ApplicationConfig,
  importProvidersFrom,
  inject,
  provideAppInitializer,
} from '@angular/core';
import {
  provideRouter,
  RouteReuseStrategy,
  withComponentInputBinding,
  withHashLocation,
} from '@angular/router';

import { AppEnvStore } from './store/app-env.state';

import { routes } from './app.routes';
import { provideAnimations } from '@angular/platform-browser/animations';
import {
  provideHttpClient,
  withFetch,
  withInterceptors,
  withInterceptorsFromDi,
  HttpClient,
} from '@angular/common/http';
import {
  MAT_DATE_LOCALE,
  MatDateFormats,
  provideNativeDateAdapter,
} from '@angular/material/core';
import { RouteReusableStrategy } from './@core/route-reusable-strategy';
import { MAT_FORM_FIELD_DEFAULT_OPTIONS } from '@angular/material/form-field';
import { apiPrefixInterceptor } from './@core/http/api-prefix.interceptor';
import { errorHandlerInterceptor } from './@core/http/error-handler.interceptor';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { catchError, firstValueFrom, Observable, of, tap } from 'rxjs';
import { bootstrapApplication } from '@angular/platform-browser';
import { App } from './app';

export class CustomTranslateLoader implements TranslateLoader {
  constructor(private http: HttpClient) {}

  getTranslation(lang: string): Observable<any> {
    return this.http.get(`/i18n/${lang}.json`).pipe(catchError(() => of({})));
  }
}

export function HttpLoaderFactory(http: HttpClient) {
  return new CustomTranslateLoader(http);
}

function initialiseEnv(env: any) {
  return () => {
    const appEnvStore = inject(AppEnvStore);

    console.log(window.location.origin);
    appEnvStore.setEnv(env);

    return firstValueFrom(of(env));
  };
}

export const MY_DATE_FORMATS: MatDateFormats = {
  parse: {
    dateInput: 'DD/MM/YYYY', // how the input string is parsed
  },
  display: {
    dateInput: 'DD/MM/YYYY', // how it appears in the input
    monthYearLabel: 'MMM YYYY', // month-year label in calendar
    dateA11yLabel: 'LL', // accessibility label
    monthYearA11yLabel: 'MMMM YYYY', // accessibility label for month/year
  },
};

export const initialiseApp = async () => {
  const env = await fetch('/env.json').then((res) => res.json());
  const appConfig = () => {
    return {
      providers: [
        provideAppInitializer(initialiseEnv(env)),
        provideRouter(routes, withComponentInputBinding(), withHashLocation()),
        provideAnimations(),
        provideHttpClient(
          withFetch(),
          withInterceptorsFromDi(),
          withInterceptors([apiPrefixInterceptor, errorHandlerInterceptor]),
        ),
        importProvidersFrom(
          TranslateModule.forRoot({
            defaultLanguage: 'en',
            loader: {
              provide: TranslateLoader,
              useFactory: HttpLoaderFactory,
              deps: [HttpClient],
            },
          }),
        ),
        {
          provide: MAT_FORM_FIELD_DEFAULT_OPTIONS,
          useValue: { appearance: 'outline' },
        },
        {
          provide: RouteReuseStrategy,
          useClass: RouteReusableStrategy,
        },
        provideNativeDateAdapter(MY_DATE_FORMATS),
        { provide: MAT_DATE_LOCALE, useValue: 'en-GB' },
      ],
    } as ApplicationConfig;
  };

  bootstrapApplication(App, appConfig).catch((err) => console.error(err));
};
