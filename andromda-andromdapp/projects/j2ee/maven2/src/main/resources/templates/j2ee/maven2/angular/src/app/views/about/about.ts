import { Component } from '@angular/core';
import { TranslateModule } from '@ngx-translate/core';
import { MaterialModule } from '@app/material.module';

@Component({
  selector: 'app-about',
  standalone: true,
  imports: [TranslateModule, MaterialModule],
  templateUrl: './about.html',
  styleUrl: './about.scss'
})
export class About {
  teamMembers = [
    {
      name: 'John Doe',
      role: 'CEO & Founder',
      avatar: 'account_circle',
      description: 'Leading the vision with 15+ years of industry experience'
    },
    {
      name: 'Jane Smith',
      role: 'CTO',
      avatar: 'account_circle',
      description: 'Driving technical innovation and architecture excellence'
    },
    {
      name: 'Mike Johnson',
      role: 'Lead Developer',
      avatar: 'account_circle',
      description: 'Building scalable solutions with modern technologies'
    },
    {
      name: 'Sarah Williams',
      role: 'UX Designer',
      avatar: 'account_circle',
      description: 'Crafting intuitive and beautiful user experiences'
    }
  ];

  technologies = [
    { name: 'Angular', icon: 'code', color: 'primary' },
    { name: 'Material Design', icon: 'palette', color: 'accent' },
    { name: 'TypeScript', icon: 'integration_instructions', color: 'primary' },
    { name: 'RxJS', icon: 'stream', color: 'accent' },
    { name: 'SCSS', icon: 'style', color: 'primary' },
    { name: 'RESTful API', icon: 'api', color: 'accent' }
  ];

  milestones = [
    { year: '2020', title: 'Company Founded', description: 'Started with a vision to revolutionize business management' },
    { year: '2021', title: 'First Major Release', description: 'Launched version 1.0 with core features' },
    { year: '2022', title: 'Global Expansion', description: 'Expanded to serve customers in 50+ countries' },
    { year: '2023', title: 'Innovation Award', description: 'Recognized for technical excellence and innovation' },
    { year: '2024', title: '100K+ Users', description: 'Reached milestone of 100,000 active users' },
    { year: '2025', title: 'Next Generation', description: 'Released version 2.0 with AI-powered features' }
  ];
}
