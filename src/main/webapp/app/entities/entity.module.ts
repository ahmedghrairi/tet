import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'dossier',
        loadChildren: () => import('./dossier/dossier.module').then(m => m.TetDossierModule)
      },
      {
        path: 'client',
        loadChildren: () => import('./client/client.module').then(m => m.TetClientModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class TetEntityModule {}
