import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';

import { MaterialModule } from '@app/material.module';
import { LoaderComponent } from './loader/loader.component';

@NgModule({
  imports: [
    MaterialModule,
    TranslateModule,
    CommonModule,
    LoaderComponent,
  ],
  declarations: [
    
  ],
  exports: [
  ]
})
export class SharedModule { }
