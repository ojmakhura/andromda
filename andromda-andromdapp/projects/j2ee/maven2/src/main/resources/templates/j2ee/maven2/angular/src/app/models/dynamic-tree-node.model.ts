import { signal } from "@angular/core";
import { TreeOption } from "./tree-option.model";
import { BehaviorSubject } from "rxjs";

export class DynamicTreeNode {
  children = new BehaviorSubject<DynamicTreeNode[]>([]);
  expanded = signal(true);
  loading = signal(false);
  options = signal<Set<TreeOption>>(new Set<TreeOption>());
  selected = signal(false);

  constructor(public level: number, public data: any, public parent?: DynamicTreeNode) {}
}
