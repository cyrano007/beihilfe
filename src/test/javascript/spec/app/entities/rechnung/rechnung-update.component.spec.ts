/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { BeihilfeTestModule } from '../../../test.module';
import { RechnungUpdateComponent } from 'app/entities/rechnung/rechnung-update.component';
import { RechnungService } from 'app/entities/rechnung/rechnung.service';
import { Rechnung } from 'app/shared/model/rechnung.model';

describe('Component Tests', () => {
    describe('Rechnung Management Update Component', () => {
        let comp: RechnungUpdateComponent;
        let fixture: ComponentFixture<RechnungUpdateComponent>;
        let service: RechnungService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [BeihilfeTestModule],
                declarations: [RechnungUpdateComponent]
            })
                .overrideTemplate(RechnungUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(RechnungUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RechnungService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Rechnung(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.rechnung = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Rechnung();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.rechnung = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
