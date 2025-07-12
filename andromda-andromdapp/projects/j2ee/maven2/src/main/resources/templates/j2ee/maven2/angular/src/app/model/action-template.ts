export class ActionTemplate {
    id: string;
    label: string;
    icon?: string;
    tooltip?: string;

    constructor(id: string, label: string, icon?: string, tooltip?: string) {
        this.id = id;
        this.label = label;
        this.icon = icon;
        this.tooltip = tooltip;
    }
}