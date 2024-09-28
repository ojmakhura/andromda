import { ApplicationConfig, isDevMode, importProvidersFrom } from '@angular/core';
import { RouteReuseStrategy, provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { provideAnimations } from '@angular/platform-browser/animations';
import { TranslateModule } from '@ngx-translate/core';
import { StoreDevtoolsModule, provideStoreDevtools } from '@ngrx/store-devtools';
import { ServiceWorkerModule } from '@angular/service-worker';
import { environment } from '@env/environment';
import { UseCaseScope } from './utils/use-case-scope';
import { withInterceptors, provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
import { RouteReusableStrategy } from './@shared';
import { MAT_FORM_FIELD_DEFAULT_OPTIONS } from '@angular/material/form-field';
import { apiPrefixInterceptor } from './@core/http/api-prefix.interceptor';
import { errorHandlerInterceptor } from './@core/http/error-handler.interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    UseCaseScope,
    provideRouter(routes),
    provideAnimations(),
    provideHttpClient(
      withInterceptorsFromDi(),
      withInterceptors([
        apiPrefixInterceptor,
        errorHandlerInterceptor
      ])
    ),
    provideStoreDevtools({ maxAge: 25, logOnly: !isDevMode() }),
    importProvidersFrom(
      StoreDevtoolsModule.instrument({}),
      TranslateModule.forRoot(),
      ServiceWorkerModule.register('./ngsw-worker.js', { enabled: environment.production }),
    ),
    { provide: MAT_FORM_FIELD_DEFAULT_OPTIONS, useValue: { appearance: 'outline' } },
    {
      provide: RouteReuseStrategy,
      useClass: RouteReusableStrategy,
    },
  ],
};
