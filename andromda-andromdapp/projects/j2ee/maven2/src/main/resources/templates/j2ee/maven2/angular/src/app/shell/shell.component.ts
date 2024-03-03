import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { Router, RouterModule } from '@angular/router';

import { CommonModule } from '@angular/common';
import { AuthModule, AuthenticationService, CredentialsService } from '@app/auth';
import { I18nModule } from '@app/i18n';
import { MaterialModule } from '@app/material.module';
import { Menu } from '@app/model/menu/menu';
import * as AuthActions from '@app/store/auth/auth.actions';
import * as AuthSelectors from '@app/store/auth/auth.selectors';
import { AuthState } from '@app/store/auth/auth.state';
import * as MenuActions from '@app/store/menu/menu.actions';
import * as MenuSelectors from '@app/store/menu/menu.selectors';
import { Store, select } from '@ngrx/store';
import { TranslateModule } from '@ngx-translate/core';
import { Observable } from 'rxjs';
import * as nav from './navigation';

@Component({
  selector: 'app-shell',
  templateUrl: './shell.component.html',
  styleUrls: ['./shell.component.scss'],
  standalone: true,
  imports: [
    CommonModule,
    TranslateModule,
    MaterialModule,
    AuthModule,
    I18nModule,
    RouterModule,
    ShellComponent,
  ],
})
export class ShellComponent implements OnInit {
  menus: any[] = [];
  menus$: Observable<Menu[]>;
  username$: Observable<string>;
  constructor(
    private router: Router,
    private titleService: Title,
    private authenticationService: AuthenticationService,
    private credentialsService: CredentialsService,
    private store: Store<AuthState>,
    private breakpoint: BreakpointObserver,
  ) {
    this.menus$ = this.store.pipe(select(MenuSelectors.selectMenus));
    this.username$ = this.store.pipe(select(AuthSelectors.selectUsername));
  }

  ngOnInit() {
    this.menus = nav.menuItems;
  }

  logout() {
    this.authenticationService.logout().subscribe(() => {
      this.store.dispatch(AuthActions.authReset());
      this.store.dispatch(MenuActions.menuReset());
      this.router.navigate(['/login'], { replaceUrl: true });
    });
  }

  get username(): string | null {
    const credentials = this.credentialsService.credentials;
    return credentials ? credentials.username : null;
  }

  get isMobile(): boolean {
    return this.breakpoint.isMatched(Breakpoints.Small) || this.breakpoint.isMatched(Breakpoints.XSmall);
  }

  get title(): string {
    return this.titleService.getTitle();
  }
}
