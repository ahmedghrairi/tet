import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDossier } from 'app/shared/model/dossier.model';
import { DossierService } from './dossier.service';

@Component({
  selector: 'jhi-dossier-delete-dialog',
  templateUrl: './dossier-delete-dialog.component.html'
})
export class DossierDeleteDialogComponent {
  dossier: IDossier;

  constructor(protected dossierService: DossierService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.dossierService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'dossierListModification',
        content: 'Deleted an dossier'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-dossier-delete-popup',
  template: ''
})
export class DossierDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ dossier }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(DossierDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.dossier = dossier;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/dossier', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/dossier', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}
