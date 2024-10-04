import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { HomeComponent } from './home.component';
import { Shell } from '@app/shell/shell.service';

export const routes: Routes = [
  { path: '', component: HomeComponent, data: { title: 'Home' } }
];
