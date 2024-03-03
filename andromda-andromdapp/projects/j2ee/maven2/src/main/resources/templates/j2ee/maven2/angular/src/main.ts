/*
 * Entry point of the application.
 * Only platform bootstrapping code should be here.
 * For app-specific initialization, use `app/app.component.ts`.
 */

import { enableProdMode } from '@angular/core';
import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from '@app/app.component';
import { appConfig } from '@app/app.config';

import { environment } from '@env/environment';

if (environment.production) {
  enableProdMode();
}

bootstrapApplication(AppComponent, appConfig).catch((err) => console.error(err));
