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
import { MatIconModule } from '@angular/material/icon';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatTable, MatTableDataSource, MatTableModule } from '@angular/material/table';
import { SharedModule } from '@app/@shared';
import { MaterialModule } from '@app/material.module';
import { ActionTemplate } from '@app/model/action-template';
import { IdLabel } from '@app/model/id-label.model'; 
import { Page } from '@app/model/page.model';
import { DeepSignal } from '@ngrx/signals';
import { TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'app-table',
  templateUrl: './table.component.html',
  standalone: true,
  imports: [
    CommonModule, 
    TranslateModule, 
    SharedModule, 
    MaterialModule
  ],
})
export class TableComponent<T> implements OnInit, OnDestroy, AfterViewInit {
  @Input() paged: boolean = false;
  @Input() showActions: boolean = true;
  @Input() actions: ActionTemplate[];
  @Input({required: true}) dataColumns: IdLabel[];
  @Input() dataSignal: Signal<T[]> | DeepSignal<Page<T>> | Signal<undefined>;

  @Output() actionClicked: EventEmitter<any> = new EventEmitter<any>();
  @Output() tableLoadEmitter: EventEmitter<any> = new EventEmitter<any>();

  dataSource = new MatTableDataSource<T>([]);
  @ViewChild('tablePaginator', { static: true }) tablePaginator: MatPaginator;
  @ViewChild('tableSort', { static: true }) tableSort: MatSort;
  @ViewChild('dataTable') dataTable: MatTable<T>;
  totalElements = 0;

  allColumns: string[] = [];

  constructor() {

    effect(() => {
        console.log(this.dataSignal())
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
    if (this.actions) {
      this.allColumns.push('actions');
    }
    // console.log(this.dataColumns);
  }

  ngOnDestroy(): void {}

  ngAfterViewInit() {
    this.tablePaginator.page.subscribe((paginator) => {
      if (this.tableLoadEmitter) {
        this.tableLoadEmitter.emit({
          pageNumber: paginator.pageIndex,
          pageSize: paginator.pageSize,
        });
      }
    });
  }

  onActionClicked(action: string, id: number) {
    if (this.actionClicked) {
      this.actionClicked.emit({ action, id });
    }
  }
}
