import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {AppConstants} from '../app-constants';
import {User} from '../model/user';
import {UserReport} from '../model/UserReport';

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {

  constructor(private http: HttpClient) {
  }

  getStudentList(): Observable<any> {
    return this.http.get<any>(AppConstants.baseUrl);
  }

  getStudentListPage(pagina): Observable<any> {
    return this.http.get<any>(AppConstants.baseUrl + 'page/' + pagina);
  }

  deletarUsuario(id: number): Observable<any> {
    return this.http.delete(AppConstants.baseUrl + id, {responseType: 'text'});
  }

  getStudant(id: number): Observable<any> {
    return this.http.get<any>(AppConstants.baseUrl + id);
  }

  consultarUser(nome: string): Observable<any> {
    return this.http.get(AppConstants.baseUrl + 'usuarioPorNome/' + nome);
  }

  consultarUserPorPage(nome: string, page: number): Observable<any> {
    return this.http.get(AppConstants.baseUrl + 'usuarioPorNome/' + nome + '/page/' + page);
  }

  updateUsuario(user: User): Observable<any> {
    return this.http.put<any>(AppConstants.baseUrl, user);
  }

  salvarUsuario(user: User): Observable<any> {
    return this.http.post<any>(AppConstants.baseUrl, user);
  }

  removerTelefone(id): Observable<any> {
    return this.http.delete(AppConstants.baseUrl + 'removerTelefone/' + id, {responseType: 'text'});
  }

  userAutenticado() {
    if (localStorage.getItem('token') !== null && localStorage.getItem('token').toString().trim() !== null) {
      return true;
    } else {
      return false;
    }
  }

  getProfissaoList(): Observable<any> {
    return this.http.get<any>(AppConstants.getBaseUrlPath + 'profissao/');
  }

  downloadPdfRelatorio() {
    return this.http.get(AppConstants.baseUrl + 'relatorio', {responseType: 'text'}).subscribe(data => {
      document.querySelector('iframe').src = data;
    });
  }

  downloadPdfRelatorioParam(userReport: UserReport) {
    return this.http.post(AppConstants.baseUrl + 'relatorio/', userReport, {responseType: 'text'}).subscribe(data => {
      document.querySelector('iframe').src = data;
    });
  }
  carregarGrafico(): Observable<any> {
    return this.http.get<any>(AppConstants.baseUrl + 'grafico/');
  }
}
