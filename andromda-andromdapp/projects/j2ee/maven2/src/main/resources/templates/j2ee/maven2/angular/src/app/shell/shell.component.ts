import { Title } from '@angular/platform-browser';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Breakpoints, BreakpointObserver } from '@angular/cdk/layout';

import { AuthenticationService, CredentialsService } from '@app/auth';
import * as nav from './navigation';
import { Observable } from 'rxjs';
import { Menu } from '@app/model/menu/menu';
import { Store, select } from '@ngrx/store';
import { AuthState } from '@app/store/auth/auth.state';
import * as AuthSelectors from '@app/store/auth/auth.selectors';
import * as AuthActions from '@app/store/auth/auth.actions';
import * as MenuSelectors from '@app/store/menu/menu.selectors';
import * as MenuActions from '@app/store/menu/menu.actions';

@Component({
  selector: 'app-shell',
  templateUrl: './shell.component.html',
  styleUrls: ['./shell.component.scss']
})
export class ShellComponent implements OnInit {

  menus: any[] = [];
  menus$: Observable<Menu[]>;
  username$: Observable<string>;
  constructor(private router: Router,
              private titleService: Title,
              private authenticationService: AuthenticationService,
              private credentialsService: CredentialsService,
              private store: Store<AuthState>,
              private breakpoint: BreakpointObserver) 
  {
    this.menus$ = this.store.pipe(select (MenuSelectors.selectMenus));
    this.username$ = this.store.pipe(select (AuthSelectors.selectUsername)); 
  }

  ngOnInit() {
    this.menus = nav.menuItems;
  }

  logout() {
    this.authenticationService.logout().subscribe(() => {
      this.store.dispatch(AuthActions.authReset());
      this.store.dispatch(MenuActions.menuReset());
      this.router.navigate(['/login'], { replaceUrl: true })
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
