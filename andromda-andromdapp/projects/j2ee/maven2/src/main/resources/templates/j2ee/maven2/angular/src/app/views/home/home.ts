import { AfterViewInit, Component, inject, OnDestroy, OnInit } from '@angular/core';
import { TranslateModule } from '@ngx-translate/core';
import { MaterialModule } from '@app/material.module';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [TranslateModule, MaterialModule],
  templateUrl: './home.html',
  styleUrl: './home.scss'
})
export class Home implements OnInit, AfterViewInit, OnDestroy  {
  isLoading = false;

  statistics = [
    { value: '100K+', label: 'Active Users', icon: 'people' },
    { value: '50+', label: 'Countries', icon: 'public' },
    { value: '99.9%', label: 'Uptime', icon: 'trending_up' },
    { value: '24/7', label: 'Support', icon: 'support_agent' }
  ];

  features = [
    {
      icon: 'dashboard',
      titleKey: 'home.feature1.title',
      descKey: 'home.feature1.description'
    },
    {
      icon: 'speed',
      titleKey: 'home.feature2.title',
      descKey: 'home.feature2.description'
    },
    {
      icon: 'security',
      titleKey: 'home.feature3.title',
      descKey: 'home.feature3.description'
    },
    {
      icon: 'cloud',
      titleKey: 'home.feature4.title',
      descKey: 'home.feature4.description'
    },
    {
      icon: 'integration_instructions',
      titleKey: 'home.feature5.title',
      descKey: 'home.feature5.description'
    },
    {
      icon: 'analytics',
      titleKey: 'home.feature6.title',
      descKey: 'home.feature6.description'
    }
  ];

  benefits = [
    { icon: 'check_circle', textKey: 'home.benefit1' },
    { icon: 'check_circle', textKey: 'home.benefit2' },
    { icon: 'check_circle', textKey: 'home.benefit3' },
    { icon: 'check_circle', textKey: 'home.benefit4' }
  ];
  
  ngOnInit(): void {
  }

  ngAfterViewInit(): void {
  }

  ngOnDestroy(): void {
  }

  toggleSpinner(): void {
    this.isLoading = !this.isLoading;
  }
}
