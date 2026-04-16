import { Env } from '@app/models/env.model';
import { patchState, signalStore, withMethods, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { of, switchMap } from 'rxjs';


export type AppEnvState = {
  env: Env;
  loading: boolean;
  loadingMenus: boolean;
  error?: any;
  authorisedPaths: string[];
  authorisedPathsLoaded: boolean;
  isLoggedIn: boolean;
  accountUri: string | null;
  username: string | null;
};

const initialState: AppEnvState = {
  env: {
    apiUrl: '',
  },
  error: null,
  loading: false,
  loadingMenus: false,
  authorisedPaths: [],
  authorisedPathsLoaded: false,
  isLoggedIn: false,
  accountUri: null,
  username: null,
};

export const AppEnvStore = signalStore(
  { providedIn: 'root' },
  withState(initialState),
  withMethods((store) => {
    return {
      reset: () => {
        patchState(store, initialState);
      },
      getEnv: rxMethod<void>(
        switchMap(() => {
          patchState(store, { loading: true });
          return fetch('/env.json')
            .then((response) => response.json())
            .then((env) => {
              patchState(store, { env, loading: false, error: false });
            })
            .catch((error) => {
              patchState(store, { error, loading: false });
            });
        }),
      ),
      setEnv: rxMethod<Env>(
        switchMap((env) => {
          patchState(store, { env });
          return of(store.env());
        }),
      ),
      setIsLoggedIn: rxMethod<boolean>(
        switchMap((isLoggedIn) => {
          patchState(store, { isLoggedIn });
          return of(store.isLoggedIn);
        }),
      ),
      setAccountUri: rxMethod<string | null>(
        switchMap((accountUri) => {
          patchState(store, { accountUri: accountUri ? accountUri : undefined });
          return of(store.accountUri);
        }),
      ),
      setUsername: rxMethod<string | null>(
        switchMap((username) => {
          patchState(store, { username });
          return of(store.username);
        }),
      ),
    };
  }),
);
