/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { BeihilfeTestModule } from '../../../test.module';
import { RechnungComponent } from 'app/entities/rechnung/rechnung.component';
import { RechnungService } from 'app/entities/rechnung/rechnung.service';
import { Rechnung } from 'app/shared/model/rechnung.model';

describe('Component Tests', () => {
    describe('Rechnung Management Component', () => {
        let comp: RechnungComponent;
        let fixture: ComponentFixture<RechnungComponent>;
        let service: RechnungService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [BeihilfeTestModule],
                declarations: [RechnungComponent],
                providers: []
            })
                .overrideTemplate(RechnungComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(RechnungComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RechnungService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new Rechnung(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.rechnungs[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
