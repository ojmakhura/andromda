import { Pipe, PipeTransform } from '@angular/core';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';

@Pipe({
    name: 'sanitizeHtml'
})
export class SanitizeHtml implements PipeTransform {

    constructor(private sanitizer: DomSanitizer){}

    transform(_html: string): SafeHtml {
        return this.sanitizer.bypassSecurityTrustHtml(_html);
    }
}
