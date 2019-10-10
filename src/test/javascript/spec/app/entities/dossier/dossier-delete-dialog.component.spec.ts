import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { TetTestModule } from '../../../test.module';
import { DossierDeleteDialogComponent } from 'app/entities/dossier/dossier-delete-dialog.component';
import { DossierService } from 'app/entities/dossier/dossier.service';

describe('Component Tests', () => {
  describe('Dossier Management Delete Component', () => {
    let comp: DossierDeleteDialogComponent;
    let fixture: ComponentFixture<DossierDeleteDialogComponent>;
    let service: DossierService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TetTestModule],
        declarations: [DossierDeleteDialogComponent]
      })
        .overrideTemplate(DossierDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DossierDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(DossierService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
    });
  });
});
