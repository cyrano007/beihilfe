import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRechnung } from 'app/shared/model/rechnung.model';

@Component({
    selector: 'jhi-rechnung-detail',
    templateUrl: './rechnung-detail.component.html'
})
export class RechnungDetailComponent implements OnInit {
    rechnung: IRechnung;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ rechnung }) => {
            this.rechnung = rechnung;
        });
    }

    previousState() {
        window.history.back();
    }
}
