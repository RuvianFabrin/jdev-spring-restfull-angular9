import {Telefone} from './telefone';
import {Profissao} from './Profissao';

export class User {
  id: number;
  login: string;
  nome: string;
  cpf: string;
  senha: string;
  dataNascimento: string;
  salario: DoubleRange;
  telefones: Array<Telefone>;
  profissao: Profissao = new Profissao();
}
