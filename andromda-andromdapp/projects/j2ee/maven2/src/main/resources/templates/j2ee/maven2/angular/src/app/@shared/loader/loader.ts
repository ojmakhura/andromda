import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { TranslateModule } from '@ngx-translate/core';

export type LoaderType = 'spinner' | 'dots' | 'pulse' | 'bars';
export type LoaderColor = 'primary' | 'accent' | 'warn' | 'white';

@Component({
  selector: 'app-loader',
  templateUrl: './loader.html',
  styleUrls: ['./loader.scss'],
  imports: [TranslateModule, CommonModule, MatProgressSpinnerModule, MatCardModule],
  standalone: true,
})
export class Loader {
  @Input() isLoading = false;
  @Input() type: LoaderType = 'spinner';
  @Input() size: 'small' | 'medium' | 'large' = 'medium';
  @Input() color: LoaderColor = 'primary';
  @Input() message?: string;
  @Input() overlay = true;
  @Input() fullScreen = true;

  get diameter(): number {
    switch (this.size) {
      case 'small': return 32;
      case 'medium': return 48;
      case 'large': return 64;
      default: return 64;
    }
  }

  get spinnerColor(): 'primary' | 'accent' | 'warn' {
    return this.color === 'white' ? 'primary' : this.color as 'primary' | 'accent' | 'warn';
  }
}
