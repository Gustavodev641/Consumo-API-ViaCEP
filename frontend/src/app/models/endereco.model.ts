export interface Endereco {
  id?: number;
  cep: string;
  rua?: string;
  numero: number | null;
  bairro?: string;
  cidade?: string;
  estado?: string;
}