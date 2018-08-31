import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BeihilfeSharedModule } from 'app/shared';
import { BeihilfeAdminModule } from 'app/admin/admin.module';
import {
    RechnungComponent,
    RechnungDetailComponent,
    RechnungUpdateComponent,
    RechnungDeletePopupComponent,
    RechnungDeleteDialogComponent,
    rechnungRoute,
    rechnungPopupRoute
} from './';

const ENTITY_STATES = [...rechnungRoute, ...rechnungPopupRoute];

@NgModule({
    imports: [BeihilfeSharedModule, BeihilfeAdminModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        RechnungComponent,
        RechnungDetailComponent,
        RechnungUpdateComponent,
        RechnungDeleteDialogComponent,
        RechnungDeletePopupComponent
    ],
    entryComponents: [RechnungComponent, RechnungUpdateComponent, RechnungDeleteDialogComponent, RechnungDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BeihilfeRechnungModule {}
