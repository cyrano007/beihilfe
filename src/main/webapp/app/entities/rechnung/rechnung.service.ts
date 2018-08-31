import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IRechnung } from 'app/shared/model/rechnung.model';

type EntityResponseType = HttpResponse<IRechnung>;
type EntityArrayResponseType = HttpResponse<IRechnung[]>;

@Injectable({ providedIn: 'root' })
export class RechnungService {
    private resourceUrl = SERVER_API_URL + 'api/rechnungs';

    constructor(private http: HttpClient) {}

    create(rechnung: IRechnung): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(rechnung);
        return this.http
            .post<IRechnung>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(rechnung: IRechnung): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(rechnung);
        return this.http
            .put<IRechnung>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IRechnung>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IRechnung[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(rechnung: IRechnung): IRechnung {
        const copy: IRechnung = Object.assign({}, rechnung, {
            austellungsDate:
                rechnung.austellungsDate != null && rechnung.austellungsDate.isValid() ? rechnung.austellungsDate.format(DATE_FORMAT) : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.austellungsDate = res.body.austellungsDate != null ? moment(res.body.austellungsDate) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((rechnung: IRechnung) => {
            rechnung.austellungsDate = rechnung.austellungsDate != null ? moment(rechnung.austellungsDate) : null;
        });
        return res;
    }
}
