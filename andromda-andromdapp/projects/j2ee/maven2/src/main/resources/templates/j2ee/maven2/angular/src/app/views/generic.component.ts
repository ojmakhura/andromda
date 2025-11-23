import { Component, Input, signal } from "@angular/core";

import { FieldTree, form } from '@angular/forms/signals';

@Component({
    selector: "app-generic",
    template: "",
    styleUrls: [],
    standalone: true,
    imports: []
})
export abstract class GenericComponent<D> {

    @Input() formObject: FieldTree<D> = form(signal<D>({} as D));

    constructor() {
    }
}
