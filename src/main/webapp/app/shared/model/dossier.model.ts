import { IClient } from 'app/shared/model/client.model';

export interface IDossier {
  id?: number;
  nom?: string;
  clients?: IClient[];
}

export class Dossier implements IDossier {
  constructor(public id?: number, public nom?: string, public clients?: IClient[]) {}
}
