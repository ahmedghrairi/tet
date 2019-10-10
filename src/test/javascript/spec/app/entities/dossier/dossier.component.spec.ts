import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { TetTestModule } from '../../../test.module';
import { DossierComponent } from 'app/entities/dossier/dossier.component';
import { DossierService } from 'app/entities/dossier/dossier.service';
import { Dossier } from 'app/shared/model/dossier.model';

describe('Component Tests', () => {
  describe('Dossier Management Component', () => {
    let comp: DossierComponent;
    let fixture: ComponentFixture<DossierComponent>;
    let service: DossierService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TetTestModule],
        declarations: [DossierComponent],
        providers: []
      })
        .overrideTemplate(DossierComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DossierComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(DossierService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Dossier(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.dossiers[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
