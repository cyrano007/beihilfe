/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { BeihilfeTestModule } from '../../../test.module';
import { RechnungDeleteDialogComponent } from 'app/entities/rechnung/rechnung-delete-dialog.component';
import { RechnungService } from 'app/entities/rechnung/rechnung.service';

describe('Component Tests', () => {
    describe('Rechnung Management Delete Component', () => {
        let comp: RechnungDeleteDialogComponent;
        let fixture: ComponentFixture<RechnungDeleteDialogComponent>;
        let service: RechnungService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [BeihilfeTestModule],
                declarations: [RechnungDeleteDialogComponent]
            })
                .overrideTemplate(RechnungDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(RechnungDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RechnungService);
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
