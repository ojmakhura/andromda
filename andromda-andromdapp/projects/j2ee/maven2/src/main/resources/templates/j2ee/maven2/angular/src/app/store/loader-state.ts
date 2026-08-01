import { patchState, signalStore, withMethods, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { of, switchMap } from 'rxjs';

export type LoaderType = 'spinner' | 'dots' | 'pulse' | 'bars';
export type LoaderColor = 'primary' | 'accent' | 'warn' | 'white';
export type LoaderSize = 'small' | 'medium' | 'large';

export type LoaderState = {
  isLoading: boolean;
  loaderMessage: string;
  type: LoaderType;
  size: LoaderSize;
  color: LoaderColor;
  message?: string;
  overlay: boolean;
  fullScreen: boolean;
};

const initialState: LoaderState = {
  isLoading: false,
  loaderMessage: 'Loading ...',
  type: 'spinner',
  size: 'medium',
  color: 'primary',
  message: undefined,
  overlay: true,
  fullScreen: true,
};

export const LoaderStateStore = signalStore(
  { providedIn: 'root' },
  withState(initialState),
  withMethods((store: any) => {
    return {
      reset: () => {
        patchState(store, initialState);
      },
      setIsLoading: rxMethod<boolean>(
        switchMap((isLoading: boolean) => {
          patchState(store, { isLoading });
          return of(store.isLoading());
        }),
      ),
      setLoaderMessage: rxMethod<string>(
        switchMap((loaderMessage: string) => {
          patchState(store, { loaderMessage });
          return of(store.loaderMessage());
        }),
      ),
      setType: rxMethod<LoaderType>(
        switchMap((type: LoaderType) => {
          patchState(store, { type });
          return of(store.type());
        }),
      ),
      setSize: rxMethod<LoaderSize>(
        switchMap((size: LoaderSize) => {
          patchState(store, { size });
          return of(store.size());
        }),
      ),
      setColor: rxMethod<LoaderColor>(
        switchMap((color: LoaderColor) => {
          patchState(store, { color });
          return of(store.color());
        }),
      ),
      setMessage: rxMethod<string | undefined>(
        switchMap((message: string | undefined) => {
          patchState(store, { message });
          return of(store.message());
        }),
      ),
      setOverlay: rxMethod<boolean>(
        switchMap((overlay: boolean) => {
          patchState(store, { overlay });
          return of(store.overlay());
        }),
      ),
      setFullScreen: rxMethod<boolean>(
        switchMap((fullScreen: boolean) => {
          patchState(store, { fullScreen });
          return of(store.fullScreen());
        }),
      ),
    };
  }),
);
