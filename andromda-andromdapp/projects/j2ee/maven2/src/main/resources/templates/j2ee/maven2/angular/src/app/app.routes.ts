import { Routes } from '@angular/router';
import { Shell } from './shell/shell.service';

export const routes: Routes = [
  
  {
    path: 'login',
    loadChildren: async () => (await import('./auth/auth.module')).AuthModule,
  },
  Shell.childRoutes([
    {
      path: '',
      loadChildren: () => import('./home/home.routes').then((m) => m.routes),
    },
    {
      path: 'about',
      data: { title: 'About' },
      loadChildren: () => import('./about/about.routes').then((m) => m.routes),
    },
  ]),
  // Fallback when no prior route is matched
  {
    path: '**',
    redirectTo: '',
    pathMatch: 'full',
  },
];
