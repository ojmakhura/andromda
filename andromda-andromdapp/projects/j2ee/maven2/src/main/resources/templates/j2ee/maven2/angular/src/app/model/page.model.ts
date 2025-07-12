export class Page<T> {
    content: T[] = [];
    totalPages: number = 0;
    totalElements: number = 0;
    last: boolean = false;
    first: boolean = false;
    numberOfElements: number = 0;
    size: number = 0;
    number: number = 0;
    empty: boolean = false;
}