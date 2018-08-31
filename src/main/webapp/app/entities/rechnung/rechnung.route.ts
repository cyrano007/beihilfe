import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Rechnung } from 'app/shared/model/rechnung.model';
import { RechnungService } from './rechnung.service';
import { RechnungComponent } from './rechnung.component';
import { RechnungDetailComponent } from './rechnung-detail.component';
import { RechnungUpdateComponent } from './rechnung-update.component';
import { RechnungDeletePopupComponent } from './rechnung-delete-dialog.component';
import { IRechnung } from 'app/shared/model/rechnung.model';

@Injectable({ providedIn: 'root' })
export class RechnungResolve implements Resolve<IRechnung> {
    constructor(private service: RechnungService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((rechnung: HttpResponse<Rechnung>) => rechnung.body));
        }
        return of(new Rechnung());
    }
}

export const rechnungRoute: Routes = [
    {
        path: 'rechnung',
        component: RechnungComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'beihilfeApp.rechnung.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'rechnung/:id/view',
        component: RechnungDetailComponent,
        resolve: {
            rechnung: RechnungResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'beihilfeApp.rechnung.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'rechnung/new',
        component: RechnungUpdateComponent,
        resolve: {
            rechnung: RechnungResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'beihilfeApp.rechnung.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'rechnung/:id/edit',
        component: RechnungUpdateComponent,
        resolve: {
            rechnung: RechnungResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'beihilfeApp.rechnung.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const rechnungPopupRoute: Routes = [
    {
        path: 'rechnung/:id/delete',
        component: RechnungDeletePopupComponent,
        resolve: {
            rechnung: RechnungResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'beihilfeApp.rechnung.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
