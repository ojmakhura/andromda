import { CommonModule } from '@angular/common';
import {
  AfterViewInit,
  Component,
  effect,
  EventEmitter,
  Input,
  OnDestroy,
  OnInit,
  Output,
  Signal,
  ViewChild,
} from '@angular/core';
import { MatCheckboxChange } from '@angular/material/checkbox';
import { MatIconModule } from '@angular/material/icon';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatRadioChange } from '@angular/material/radio';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatTable, MatTableDataSource, MatTableModule } from '@angular/material/table';
import { SharedModule } from '@app/@shared/shared.module';
import { MaterialModule } from '@app/material.module';
import { ActionTemplate } from '@app/model/action-template';
import { ColumnModel } from '@app/model/column.model';
import { IdLabel } from '@app/model/id-label.model';
import { Page } from '@app/model/page.model';
import { SelectionType } from '@app/model/selection-type.model';
import { DeepSignal } from '@ngrx/signals';
import { TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'app-table',
  templateUrl: './table.component.html',
  styleUrl: './table.component.scss',
  standalone: true,
  imports: [CommonModule, TranslateModule, SharedModule, MaterialModule],
})
export class TableComponent<T> implements OnInit, OnDestroy, AfterViewInit {
  @Input() paged: boolean = false;
  @Input() showActions: boolean = true;
  @Input() actions: ActionTemplate[];
  @Input({ required: true }) dataColumns: ColumnModel[];
  @Input() dataSignal: Signal<T[]> | DeepSignal<Page<T>> | Signal<undefined>;
  @Input() selectionType = SelectionType.NONE;

  @Output() actionClicked: EventEmitter<any> = new EventEmitter<any>();
  @Output() tableLoadEmitter: EventEmitter<any> = new EventEmitter<any>();
  @Input() selectionFilter: any;

  dataSource = new MatTableDataSource<T>([]);
  @ViewChild('tablePaginator', { static: true }) tablePaginator: MatPaginator;
  @ViewChild('tableSort', { static: true }) tableSort: MatSort;
  @ViewChild('dataTable') dataTable?: MatTable<T>;
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
        this.dataSource = new MatTableDataSource(data);
        this.dataSource.sort = this.tableSort;
        this.totalElements = data.length;
      } else {
        const page = <Page<T>>this.dataSignal();
        this.dataSource = new MatTableDataSource(page.content);
        this.dataSource.sort = this.tableSort;
        this.totalElements = page.totalElements;
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

  ngOnDestroy(): void {}

  ngAfterViewInit() {
    this.tablePaginator.page.subscribe({
      next: (paginator: MatPaginator) => {
        if (this.tableLoadEmitter) {
          this.tableLoadEmitter.emit({
            pageNumber: paginator.pageIndex,
            pageSize: paginator.pageSize,
          });
        }
      },
    });
  }

  onActionClicked(action: string, id: string) {
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
