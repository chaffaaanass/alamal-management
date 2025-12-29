import { Routes } from '@angular/router';

export const routes: Routes = [
    {path: '', loadComponent: () => import('./components/auth/login/login.component').then(m => m.LoginComponent)},
    {path: 'engineers', loadComponent: () => import('./components/engineer/engineer.component').then(m => m.EngineerComponent)},
    {path: 'engineers/types', loadComponent: () => import('./components/engineer-type/engineer-type.component').then(m => m.EngineerTypeComponent)},
    {path: 'batch-types', loadComponent: () => import('./components/batch-type/batch-type.component').then(m => m.BatchTypeComponent)},
    {path: 'societies', loadComponent: () => import('./components/society/society.component').then(m => m.SocietyComponent)},
    {path: 'users', loadComponent: () => import('./components/user/user.component').then(m => m.UserComponent)},
];
