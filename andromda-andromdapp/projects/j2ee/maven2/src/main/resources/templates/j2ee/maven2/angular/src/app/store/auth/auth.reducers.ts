import { Action, createFeature, createReducer, on } from '@ngrx/store';
import * as AuthActions from './auth.actions';
import { authKey, initialState } from './auth.state';

export const authReducer = createReducer(
  initialState,
  
  on(AuthActions.setId, (state, action) => ({
    ...state,
    id: action.id,
    errors: [],
  })),
  on(AuthActions.setUsername, (state, action) => ({
    ...state,
    username: action.username,
    errors: [],
  })),
  on(AuthActions.setEmail, (state, action) => ({
    ...state,
    email: action.email,
    errors: [],
  })),
  on(AuthActions.setLastName, (state, action) => ({
    ...state,
    lastName: action.lastName,
    errors: [],
  })),
  on(AuthActions.setFirstName, (state, action) => ({
    ...state,
    firstName: action.firstName,
    errors: [],
  })),
  on(AuthActions.authReset, (state) => ({
    ...state,
    id: null,
    menus: [],
    roles: [],
    username: null,
    email: null,
    lastName: null,
    firstName: null,
    loggedIn: false,
    errors: [],
  })),
  on(AuthActions.authFailure, (state, action) => ({
    ...state,
    errors: action.errors,
  }))
);

export const authFeature = createFeature({
  name: authKey,
  reducer: authReducer,
});
