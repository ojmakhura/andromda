import { ApplicationConfig, importProvidersFrom } from '@angular/core';
import { RouteReuseStrategy, provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { provideAnimations } from '@angular/platform-browser/animations';
import { TranslateModule } from '@ngx-translate/core';
import { StoreModule } from '@ngrx/store';
import { StoreDevtoolsModule } from '@ngrx/store-devtools';
import { ServiceWorkerModule } from '@angular/service-worker';
import { environment } from '@env/environment';
import { EffectsModule } from '@ngrx/effects';
import { UseCaseScope } from './utils/use-case-scope';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { RouteReusableStrategy } from './@shared';
import { apiPrefixInterceptor, errorHandlerInterceptor } from './@core';

export const appConfig: ApplicationConfig = {
  providers: [
    UseCaseScope,
    provideRouter(routes),
    provideAnimations(),
    provideHttpClient(withInterceptors([
      apiPrefixInterceptor,
      errorHandlerInterceptor,
    ])),
    importProvidersFrom(
      TranslateModule.forRoot(),
      StoreModule.forRoot({}),
      StoreDevtoolsModule.instrument({}),
      TranslateModule.forRoot(),
      EffectsModule.forRoot([]),
      ServiceWorkerModule.register('./ngsw-worker.js', { enabled: environment.production }),
    ),
    {
      provide: RouteReuseStrategy,
      useClass: RouteReusableStrategy,
    },
  ],
};
