import { Moment } from 'moment';
import { IUser } from 'app/core/user/user.model';

export interface IRechnung {
    id?: number;
    rechnungID?: string;
    austellungsDate?: Moment;
    bezahlDate?: string;
    betrag?: number;
    user?: IUser;
}

export class Rechnung implements IRechnung {
    constructor(
        public id?: number,
        public rechnungID?: string,
        public austellungsDate?: Moment,
        public bezahlDate?: string,
        public betrag?: number,
        public user?: IUser
    ) {}
}
