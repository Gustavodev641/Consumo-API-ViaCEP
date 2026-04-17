export interface Endereco {
  id?: number;
  cep: string;
  rua: string;
  numero: string;
  bairro: string;
  cidade: string;
  estado: string;
}

export interface Usuario {
  id?: number;
  nome: string;
  email: string;
  telefone: string;
  enderecos: Endereco[];
}