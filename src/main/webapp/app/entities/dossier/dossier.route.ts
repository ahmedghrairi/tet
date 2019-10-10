import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Dossier } from 'app/shared/model/dossier.model';
import { DossierService } from './dossier.service';
import { DossierComponent } from './dossier.component';
import { DossierDetailComponent } from './dossier-detail.component';
import { DossierUpdateComponent } from './dossier-update.component';
import { DossierDeletePopupComponent } from './dossier-delete-dialog.component';
import { IDossier } from 'app/shared/model/dossier.model';

@Injectable({ providedIn: 'root' })
export class DossierResolve implements Resolve<IDossier> {
  constructor(private service: DossierService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IDossier> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Dossier>) => response.ok),
        map((dossier: HttpResponse<Dossier>) => dossier.body)
      );
    }
    return of(new Dossier());
  }
}

export const dossierRoute: Routes = [
  {
    path: '',
    component: DossierComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'tetApp.dossier.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: DossierDetailComponent,
    resolve: {
      dossier: DossierResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'tetApp.dossier.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: DossierUpdateComponent,
    resolve: {
      dossier: DossierResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'tetApp.dossier.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: DossierUpdateComponent,
    resolve: {
      dossier: DossierResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'tetApp.dossier.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const dossierPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: DossierDeletePopupComponent,
    resolve: {
      dossier: DossierResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'tetApp.dossier.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
