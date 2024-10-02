import { Pipe, PipeTransform } from "@angular/core";
import { DynamicTreeNode } from "@app/model/dynamic-tree-node.model";

@Pipe({
  name: 'asTreeNode',
  standalone: true
})
export class AsTreeNodePipe implements PipeTransform {
  transform(value: any, ...args: any[]): any {
    return value as DynamicTreeNode;
  }
}