import { Endereco } from './endereco.model';

export interface Usuario {
  id?: number;
  nome: string;
  email: string;
  telefone: string;
  cpf: string;
  enderecos?: Endereco[];
}