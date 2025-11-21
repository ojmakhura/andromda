export class ColumnModel {
  id: string;
  label: string;
  actionId?: string;
  link: boolean = false;
  pipe?: { transform: (...args: any[]) => any };
  pipeArgs?: any[];

  constructor(
    id: string,
    label: string,
    link: boolean = false,
    actionId?: string,
    pipe?: { transform: (...args: any[]) => any },
    pipeArgs?: any[],
  ) {
    this.id = id;
    this.label = label;
    this.link = link;
    this.actionId = actionId;
    this.pipe = pipe;
    this.pipeArgs = pipeArgs;
  }
}