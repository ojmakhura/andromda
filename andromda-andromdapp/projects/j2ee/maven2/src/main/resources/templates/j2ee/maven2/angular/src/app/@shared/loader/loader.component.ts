import { CommonModule } from '@angular/common';
import { Component, OnInit, Input } from '@angular/core';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MaterialModule } from '@app/material.module';
import { TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'app-loader',
  templateUrl: './loader.component.html',
  styleUrls: ['./loader.component.scss'],
  imports: [MaterialModule, TranslateModule, CommonModule, MatProgressSpinnerModule],
  standalone: true,
})
export class LoaderComponent implements OnInit {
  @Input() isLoading = false;
  @Input() size = 1;
  @Input() message: string | undefined;

  constructor() {}

  ngOnInit() {}
}
