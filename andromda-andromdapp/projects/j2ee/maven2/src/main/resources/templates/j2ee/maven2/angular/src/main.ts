import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { App } from './app/app';

// bootstrapApplication(App, appConfig)
fetch('env.json')
  .then((response) => response.json())
  .then((env) => {
    console.log(env);
    bootstrapApplication(App, appConfig(env)).catch((err) => console.error(err));
  })
  .catch((error) => {
    console.error('Error loading env.json:', error);
    bootstrapApplication(App, appConfig({})).catch((err) => console.error(err));
  });
//   .catch((err) => console.error(err));
