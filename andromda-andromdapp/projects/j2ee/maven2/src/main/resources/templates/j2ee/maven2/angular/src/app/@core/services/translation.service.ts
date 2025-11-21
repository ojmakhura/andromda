import { Injectable } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

@Injectable({
  providedIn: 'root'
})
export class TranslationService {
  constructor(private translate: TranslateService) {
    this.initLanguage();
  }

  initLanguage() {
    // Set default language
    this.translate.setDefaultLang('en');
    
    // Get browser language or use stored preference
    const browserLang = localStorage.getItem('language') || this.translate.getBrowserLang() || 'en';
    const lang = ['en', 'es', 'fr'].includes(browserLang) ? browserLang : 'en';
    
    this.translate.use(lang);
  }

  setLanguage(lang: string) {
    this.translate.use(lang);
    localStorage.setItem('language', lang);
  }

  getCurrentLanguage(): string {
    return this.translate.currentLang || 'en';
  }

  getAvailableLanguages() {
    return [
      { code: 'en', name: 'English' },
      { code: 'es', name: 'Español' },
      { code: 'fr', name: 'Français' }
    ];
  }
}
