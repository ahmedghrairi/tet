import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDossier } from 'app/shared/model/dossier.model';

@Component({
  selector: 'jhi-dossier-detail',
  templateUrl: './dossier-detail.component.html'
})
export class DossierDetailComponent implements OnInit {
  dossier: IDossier;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ dossier }) => {
      this.dossier = dossier;
    });
  }

  previousState() {
    window.history.back();
  }
}
