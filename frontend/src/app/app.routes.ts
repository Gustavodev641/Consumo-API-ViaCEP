import { Routes } from '@angular/router';
import { ListaComponent } from './pages/usuarios/lista/lista.component';
import { FormularioComponent } from './pages/usuarios/formulario/formulario.component';
import { DetalhesComponent } from './pages/usuarios/detalhes/detalhes.component';

export const routes: Routes = [
  { path: '', redirectTo: 'usuarios', pathMatch: 'full' },
  { path: 'usuarios', component: ListaComponent },
  { path: 'usuarios/novo', component: FormularioComponent },
  { path: 'usuarios/:id/editar', component: FormularioComponent },
  { path: 'usuarios/:id', component: DetalhesComponent },
];