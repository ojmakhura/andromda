import { Component, inject, Input } from "@angular/core";
import { FormBuilder, FormGroup } from "@angular/forms";

@Component({
    selector: "app-generic",
    template: "",
    styleUrls: [],
    standalone: true,
    imports: []
})
export abstract class GenericComponent {

    formBuilder = inject(FormBuilder);
    @Input() formGroupControl: FormGroup;

    constructor() {
    }

}