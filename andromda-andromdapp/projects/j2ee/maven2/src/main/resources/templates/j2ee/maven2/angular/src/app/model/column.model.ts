export class ColumnModel {
    id: string;
    label: string;
    link: boolean = false;
    
    constructor(id: string, label: string, link: boolean | false) {
        this.id = id;
        this.label = label;
        this.link = link;
    }
}