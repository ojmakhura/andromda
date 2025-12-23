import { Component, Input, signal } from "@angular/core";

import { FieldTree, form, PathKind, SchemaPathTree } from '@angular/forms/signals';

@Component({
  selector: "app-generic",
  template: "",
  styleUrls: [],
  standalone: true,
  imports: []
})
export abstract class GenericComponent<D> {
  @Input() formData!: D;
  formSignal = signal<D>(this.formData ?? this.createNewFormObject());
  formObject: FieldTree<D> = form(this.formSignal, path => this.createValidators(path));

  constructor() {
  }

  abstract createValidators(path: SchemaPathTree<D, PathKind.Root>): void;

  abstract createNewFormObject(): D;
}
