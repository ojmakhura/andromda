import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';

import { MaterialModule } from '@app/material.module';
import { LanguageSelectorComponent } from './language-selector.component';

@NgModule({
  imports: [
    CommonModule,
    TranslateModule,
    MaterialModule,
  ],
  declarations: [
    LanguageSelectorComponent,
  ],
  exports: [
    LanguageSelectorComponent,
  ]
})
export class I18nModule { }
