import { Action, createFeature, createReducer, on } from '@ngrx/store';
import * as AuthActions from './auth.actions';
import { authKey, initialState } from './auth.state';

export const authReducer = createReducer(
  initialState,
  
  on(AuthActions.setId, (state, action) => ({
    ...state,
    id: action.id,
    error: null,
  })),
  on(AuthActions.setUsername, (state, action) => ({
    ...state,
    username: action.username,
    error: null,
  })),
  on(AuthActions.setEmail, (state, action) => ({
    ...state,
    email: action.email,
    error: null,
  })),
  on(AuthActions.setLastName, (state, action) => ({
    ...state,
    lastName: action.lastName,
    error: null,
  })),
  on(AuthActions.setFirstName, (state, action) => ({
    ...state,
    firstName: action.firstName,
    error: null,
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
    error: null,
  })),
  on(AuthActions.authFailure, (state, action) => ({
    ...state,
    error: action.error,
  }))
);

export const authFeature = createFeature({
  name: authKey,
  reducer: authReducer,
});
