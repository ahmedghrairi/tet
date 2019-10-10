import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { TetTestModule } from '../../../test.module';
import { DossierUpdateComponent } from 'app/entities/dossier/dossier-update.component';
import { DossierService } from 'app/entities/dossier/dossier.service';
import { Dossier } from 'app/shared/model/dossier.model';

describe('Component Tests', () => {
  describe('Dossier Management Update Component', () => {
    let comp: DossierUpdateComponent;
    let fixture: ComponentFixture<DossierUpdateComponent>;
    let service: DossierService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TetTestModule],
        declarations: [DossierUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(DossierUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DossierUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(DossierService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Dossier(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Dossier();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
