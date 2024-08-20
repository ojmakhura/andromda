import { Routes, RouterModule } from '@angular/router';
// import { marker } from '@biesbjerg/ngx-translate-extract-marker';

import { AboutComponent } from './about.component';

export const routes: Routes = [
  { 
    path: '', 
    component: AboutComponent, 
    data: { title: 'About' } 
  }
];
