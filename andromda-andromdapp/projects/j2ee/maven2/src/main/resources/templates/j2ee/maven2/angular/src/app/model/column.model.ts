export class ColumnModel {
  id: string;
  label: string;
  actionId?: string;
  link: boolean = false;

  constructor(id: string, label: string, link: boolean | false, actionId?: string) {
      this.id = id;
      this.label = label;
      this.link = link;
      this.actionId = actionId;
  }
}
