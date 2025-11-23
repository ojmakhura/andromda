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
  signal,
  Signal,
  ViewChild,
} from '@angular/core';
import { MatCheckboxChange, MatCheckboxModule } from '@angular/material/checkbox';
import { MatIconModule } from '@angular/material/icon';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatRadioChange, MatRadioModule } from '@angular/material/radio';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatTable, MatTableDataSource, MatTableModule } from '@angular/material/table';
import { ActionTemplate } from '@app/models/action-template';
import { ColumnModel } from '@app/models/column.model';
import { Page } from '@app/models/page.model';
import { SelectionType } from '@app/models/selection-type.model';
import { TranslateModule } from '@ngx-translate/core';
import { MaterialModule } from "@app/material.module";
import { sign } from 'crypto';

@Component({
  selector: 'app-table',
  templateUrl: './table.html',
  styleUrl: './table.scss',
  standalone: true,
  imports: [
    CommonModule,
    TranslateModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatIconModule,
    MatCheckboxModule,
    MatRadioModule,
    MaterialModule
],
})
export class TableComponent<T> implements OnInit, OnDestroy, AfterViewInit {
  @Input() paged: boolean = false;
  @Input() showActions: boolean = true;
  @Input() actions: ActionTemplate[] = [];
  @Input({ required: true }) dataColumns: ColumnModel[] = [];
  @Input() dataSignal: Signal<T[] | Page<T> | undefined> = signal<undefined>(undefined);
  @Input() selectionType = SelectionType.NONE;

  @Output() actionClicked: EventEmitter<any> = new EventEmitter<any>();
  @Output() tableLoadEmitter: EventEmitter<any> = new EventEmitter<any>();
  @Input() selectionFilter: any;

  dataSource = new MatTableDataSource<T>([]);
  @ViewChild('tablePaginator', { static: true }) tablePaginator: MatPaginator = new MatPaginator(null!, null!);
  @ViewChild('tableSort', { static: true }) tableSort: MatSort = new MatSort();
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
        this.dataSource = new MatTableDataSource(data || []);
        this.dataSource.sort = this.tableSort;
        this.totalElements = (data?.length || 0);
      } else {
        const page = <Page<T>>this.dataSignal();
        this.dataSource = new MatTableDataSource((page?.content || []));
        this.dataSource.sort = this.tableSort;
        this.totalElements = (page?.totalElements || 0);
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
