import { Injectable } from '@angular/core';
import { SortOrder } from './sort-order';


@Injectable()
export class PropertySearchOrder {
    propertyName?: string | any = null;

    order?: SortOrder | any = null;

    
    constructor() {
    }
}
