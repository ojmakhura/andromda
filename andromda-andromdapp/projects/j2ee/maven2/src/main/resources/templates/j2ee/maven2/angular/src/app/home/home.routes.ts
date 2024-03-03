import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { marker } from '@biesbjerg/ngx-translate-extract-marker';

import { HomeComponent } from './home.component';
import { Shell } from '@app/shell/shell.service';

export const HOME_ROUTES: Routes = [
  { path: '', component: HomeComponent, data: { title: marker('Home') } }
];
