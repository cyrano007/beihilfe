import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IRechnung } from 'app/shared/model/rechnung.model';
import { Principal } from 'app/core';
import { RechnungService } from './rechnung.service';

@Component({
    selector: 'jhi-rechnung',
    templateUrl: './rechnung.component.html'
})
export class RechnungComponent implements OnInit, OnDestroy {
    rechnungs: IRechnung[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private rechnungService: RechnungService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {}

    loadAll() {
        this.rechnungService.query().subscribe(
            (res: HttpResponse<IRechnung[]>) => {
                this.rechnungs = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInRechnungs();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IRechnung) {
        return item.id;
    }

    registerChangeInRechnungs() {
        this.eventSubscriber = this.eventManager.subscribe('rechnungListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
