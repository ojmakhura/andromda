import {
  AfterViewChecked,
  AfterViewInit,
  Component,
  effect,
  EventEmitter,
  Input,
  OnDestroy,
  OnInit,
  Output,
  signal,
  Signal,
  ViewChild,
} from '@angular/core';
import { MatCheckboxChange, MatCheckboxModule } from '@angular/material/checkbox';
import { MatIconModule } from '@angular/material/icon';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatRadioChange, MatRadioModule } from '@angular/material/radio';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatButtonModule } from '@angular/material/button';
import { CommonModule } from '@angular/common';
import { MaterialModule } from '@app/material.module';
import { TranslateModule } from '@ngx-translate/core';
import { ActionTemplate } from '@app/models/action-template';
import { ColumnModel } from '@app/models/column.model';
import { DeepSignal } from '@ngrx/signals';
import { Page } from '@app/models/page.model';
import { SelectionType } from '@app/models/selection-type.model';

interface HasId {
  id?: string | number;
  [key: string]: any;
}

@Component({
  selector: 'app-fancy-table',
  templateUrl: './fancy-table.component.html',
  styleUrls: ['./fancy-table.component.scss'],
  standalone: true,
  imports: [
    CommonModule,
    TranslateModule,
    MaterialModule,
    MatIconModule,
    MatCheckboxModule,
    MatRadioModule,
    MatPaginatorModule,
    MatTooltipModule,
    MatButtonModule,
  ],
})
export class FancyTableComponent<T extends HasId> implements OnInit, OnDestroy, AfterViewInit, AfterViewChecked {
  @Input() paged: boolean = false;
  @Input() showActions: boolean = true;
  @Input() actions: ActionTemplate[] = [];
  @Input({ required: true }) mainColumns: ColumnModel[] = [];
  @Input({ required: true }) dataColumns: ColumnModel[] = [];
  @Input() dataSignal: Signal<T[]> | DeepSignal<Page<T>> | Signal<undefined> = signal<undefined>(undefined);
  dataItems = signal([] as T[]);
  @Input() selectionType = SelectionType.NONE;

  @Output() actionClicked: EventEmitter<any> = new EventEmitter<any>();
  @Output() pageChanged: EventEmitter<any> = new EventEmitter<any>();
  @Input() selectionFilter: any;

  @ViewChild(MatPaginator) set paginator(paginator: MatPaginator | null) {
    if (paginator) {
      paginator.page.subscribe({
        next: (paginator: MatPaginator) => {
          console.log('Paginator event:', paginator);
          this.pageChanged.emit({
            pageNumber: paginator.pageIndex,
            pageSize: paginator.pageSize,
          });
        },
      });
    }
  }

  totalElements = 0;
  allColumns: string[] = [];
  s = SelectionType;
  selectedItems: any[] = [];

  constructor() {
    effect(() => {
      if (!this.dataSignal) {
        return;
      }

      if (!this.paged) {
        const data: T[] = <T[]>this.dataSignal();
        this.dataItems.set(data || []);
        this.totalElements = data?.length || 0;
      } else {
        const page = <Page<T>>this.dataSignal();
        this.dataItems.set(page?.content || []);
        this.totalElements = page?.totalElements || 0;
      }
    });
  }

  ngOnInit(): void {
    this.allColumns = this.dataColumns.map((column) => column.id);
    if (this.showActions) {
      this.allColumns.push('actions');
    }

    if (this.selectionType !== SelectionType.NONE) {
      this.allColumns.unshift('selection');
    }
  }

  ngAfterViewChecked(): void {}

  ngOnDestroy(): void {}

  ngAfterViewInit() {
    console.log('Paginator:', this.paginator);
  }

  onActionClicked(action: string, id: string | number | undefined) {
    if (this.actionClicked) {
      this.actionClicked.emit({ action, id });
    }
  }

  radioSelected($event: MatRadioChange, row: any) {
    if ($event.value) {
      this.selectedItems = [row];
    }
  }

  checkboxSelected($event: MatCheckboxChange, row: any) {
    if ($event.checked) {
      this.selectedItems.push(row);
    } else {
      this.selectedItems = this.selectedItems.filter((item) => {
        let r = this.selectionFilter ? this.selectionFilter(item, row) : item.id !== row.id;
        return !r;
      });
    }
  }
}
