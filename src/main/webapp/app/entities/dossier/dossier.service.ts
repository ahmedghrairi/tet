import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IDossier } from 'app/shared/model/dossier.model';

type EntityResponseType = HttpResponse<IDossier>;
type EntityArrayResponseType = HttpResponse<IDossier[]>;

@Injectable({ providedIn: 'root' })
export class DossierService {
  public resourceUrl = SERVER_API_URL + 'api/dossiers';

  constructor(protected http: HttpClient) {}

  create(dossier: IDossier): Observable<EntityResponseType> {
    return this.http.post<IDossier>(this.resourceUrl, dossier, { observe: 'response' });
  }

  update(dossier: IDossier): Observable<EntityResponseType> {
    return this.http.put<IDossier>(this.resourceUrl, dossier, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDossier>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDossier[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
