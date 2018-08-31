/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BeihilfeTestModule } from '../../../test.module';
import { RechnungDetailComponent } from 'app/entities/rechnung/rechnung-detail.component';
import { Rechnung } from 'app/shared/model/rechnung.model';

describe('Component Tests', () => {
    describe('Rechnung Management Detail Component', () => {
        let comp: RechnungDetailComponent;
        let fixture: ComponentFixture<RechnungDetailComponent>;
        const route = ({ data: of({ rechnung: new Rechnung(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [BeihilfeTestModule],
                declarations: [RechnungDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(RechnungDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(RechnungDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.rechnung).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
