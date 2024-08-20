import { Injectable } from '@angular/core';
import { SortOrder } from './sort-order';


@Injectable()
export class SearchObject<T> {
    criteria?: T | any = null;

    paged?: Boolean | any = false;

    pageNumber?: number | any = null;

    pageSize?: number | any = null;

    sortings?: SortOrder[] | any[][];

    
    constructor() {
    }
}
