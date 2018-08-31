import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IRechnung } from 'app/shared/model/rechnung.model';
import { RechnungService } from './rechnung.service';
import { IUser, UserService } from 'app/core';

@Component({
    selector: 'jhi-rechnung-update',
    templateUrl: './rechnung-update.component.html'
})
export class RechnungUpdateComponent implements OnInit {
    private _rechnung: IRechnung;
    isSaving: boolean;

    users: IUser[];
    austellungsDateDp: any;

    constructor(
        private jhiAlertService: JhiAlertService,
        private rechnungService: RechnungService,
        private userService: UserService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ rechnung }) => {
            this.rechnung = rechnung;
        });
        this.userService.query().subscribe(
            (res: HttpResponse<IUser[]>) => {
                this.users = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.rechnung.id !== undefined) {
            this.subscribeToSaveResponse(this.rechnungService.update(this.rechnung));
        } else {
            this.subscribeToSaveResponse(this.rechnungService.create(this.rechnung));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IRechnung>>) {
        result.subscribe((res: HttpResponse<IRechnung>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackUserById(index: number, item: IUser) {
        return item.id;
    }
    get rechnung() {
        return this._rechnung;
    }

    set rechnung(rechnung: IRechnung) {
        this._rechnung = rechnung;
    }
}
