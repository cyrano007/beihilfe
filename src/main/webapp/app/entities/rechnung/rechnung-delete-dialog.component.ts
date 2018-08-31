import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IRechnung } from 'app/shared/model/rechnung.model';
import { RechnungService } from './rechnung.service';

@Component({
    selector: 'jhi-rechnung-delete-dialog',
    templateUrl: './rechnung-delete-dialog.component.html'
})
export class RechnungDeleteDialogComponent {
    rechnung: IRechnung;

    constructor(private rechnungService: RechnungService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.rechnungService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'rechnungListModification',
                content: 'Deleted an rechnung'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-rechnung-delete-popup',
    template: ''
})
export class RechnungDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ rechnung }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(RechnungDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.rechnung = rechnung;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
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
