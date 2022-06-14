import { Action, createFeature, createReducer, on } from '@ngrx/store';
import * as MenuActions from './menu.actions';
import { menuKey, initialState } from './menu.state';

export const menuReducer = createReducer(
  initialState,
  
  on(MenuActions.addMenu, (state, action) => ({
    ...state,
    menus: state.menus.find((menu) => menu.routerLink === action.menu.routerLink) ? [...state.menus, action.menu] : [...state.menus],
    errors: [],
  })),
  on(MenuActions.getMenusSuccess, (state, action) => ({
    ...state,
    menus: action.menus,
    errors: [],
  })),
  on(MenuActions.addMenu, (state, action) => ({
    ...state,
    menus: [...state.menus, action.menu],
    errors: [],
  })),
  on(MenuActions.menuReset, (state) => ({
    ...state,
    menus: [],
    errors: [],
  })),
  on(MenuActions.menuFailure, (state, action) => ({
    ...state,
    errors: action.errors,
  }))
);

export const menuFeature = createFeature({
  name: menuKey,
  reducer: menuReducer,
});
