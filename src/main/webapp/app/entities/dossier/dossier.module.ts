import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TetSharedModule } from 'app/shared/shared.module';
import { DossierComponent } from './dossier.component';
import { DossierDetailComponent } from './dossier-detail.component';
import { DossierUpdateComponent } from './dossier-update.component';
import { DossierDeletePopupComponent, DossierDeleteDialogComponent } from './dossier-delete-dialog.component';
import { dossierRoute, dossierPopupRoute } from './dossier.route';

const ENTITY_STATES = [...dossierRoute, ...dossierPopupRoute];

@NgModule({
  imports: [TetSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    DossierComponent,
    DossierDetailComponent,
    DossierUpdateComponent,
    DossierDeleteDialogComponent,
    DossierDeletePopupComponent
  ],
  entryComponents: [DossierDeleteDialogComponent]
})
export class TetDossierModule {}
