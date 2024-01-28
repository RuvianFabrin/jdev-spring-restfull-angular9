import { Component, OnInit } from '@angular/core';
import {UsuarioService} from '../../../service/usuario.service';
import {Observable} from 'rxjs';
import {User} from '../../../model/user';

@Component({
  selector: 'app-usuario',
  templateUrl: './usuario.component.html',
  styleUrls: ['./usuario.component.css']
})
export class UsuarioComponent implements OnInit {

  students: Array<User[]>;
  nome: string;
  total: number;
  constructor(private usuarioService: UsuarioService) { }

  ngOnInit() {
    this.usuarioService.getStudentList().subscribe(data => {
      this.students = data.content;
      this.total = data.totalElements;
    });
  }

  deleteUsuario(id: number, index: number) {
    if ( confirm('Deseja mesmo remover?')) {
      this.usuarioService.deletarUsuario(id).subscribe(data => {
        this.students.splice(index, 1); /* Remove da tela */
        // console.log('Retorno delete:' + data);
        // this.usuarioService.getStudentList().subscribe(dataU => {
        //   this.students = dataU;
        // });
      });

    }
  }

  consultarUser() {
    if ( this.nome === '' ) {
      this.usuarioService.getStudentList().subscribe(data => {
        this.students = data.content;
        this.total = data.totalElements;
      });
    } else {
      // 05:51
      this.usuarioService.consultarUser(this.nome).subscribe(data => {
        this.students = data.content;
        this.total = data.totalElements;
      });
    }

  }

  carregarPagina(pagina) {
    if (this.nome !== '') {
      this.usuarioService.consultarUserPorPage(this.nome, (pagina - 1) ).subscribe(data => {
        this.students = data.content;
        this.total = data.totalElements;
      });
    } else {

      this.usuarioService.getStudentListPage(pagina - 1).subscribe(data => {
        this.students = data.content;
        this.total = data.totalElements;
      });
    }
  }

  imprimeRelatorio(){
    return this.usuarioService.downloadPdfRelatorio();
  }

}
