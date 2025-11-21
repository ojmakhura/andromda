import { Routes } from '@angular/router';
import { Shell } from './shell';

export const routes: Routes = [
    Shell.childRoutes([
        {
            path: '',
            loadComponent: () => import('./views/home/home').then(m => m.Home)
        },
        {
            path: 'about',
            loadComponent: () => import('./views/about/about').then(m => m.About)
        }
    ])
];
