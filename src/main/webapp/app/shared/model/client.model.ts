import { IDossier } from 'app/shared/model/dossier.model';

export interface IClient {
  id?: number;
  nom?: string;
  dossiers?: IDossier[];
}

export class Client implements IClient {
  constructor(public id?: number, public nom?: string, public dossiers?: IDossier[]) {}
}
