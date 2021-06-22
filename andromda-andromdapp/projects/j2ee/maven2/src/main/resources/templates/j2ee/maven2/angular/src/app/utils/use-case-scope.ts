import { Injectable } from '@angular/core';

@Injectable()
export class UseCaseScope {
    pageVariables: any = {};
    useCaseParameters: any = {};
    useCaseReturnValues: any = {};
    useCaseInDialog: boolean = false;
}