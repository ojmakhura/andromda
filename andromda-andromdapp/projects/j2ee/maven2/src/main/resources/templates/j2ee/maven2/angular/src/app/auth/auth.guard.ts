import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class AuthGuard{
  
  async isAccessAllowed(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<boolean | UrlTree> {
    if (!this.authenticated) {

      // Add the rediect to login screen
    }

    return this.authenticated;
  }
}
