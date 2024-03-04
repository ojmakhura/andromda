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
      loadChildren: async () => (await import('./about/about.module')).AboutModule,
    },
  ]),
  // Fallback when no prior route is matched
  {
    path: '**',
    redirectTo: '',
    pathMatch: 'full',
  },
];
