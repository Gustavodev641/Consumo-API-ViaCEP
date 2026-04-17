import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatCardModule } from '@angular/material/card';
import { MatDividerModule } from '@angular/material/divider';
import { UsuarioService } from '../../../core/services/usuario.service';
import { Usuario } from '../../../models/usuario.model';
import { Endereco } from '../../../models/endereco.model';

@Component({
  selector: 'app-formulario',
  standalone: true,
  imports: [
    CommonModule, FormsModule, MatInputModule, MatButtonModule,
    MatIconModule, MatSnackBarModule, MatProgressSpinnerModule,
    MatCardModule, MatDividerModule
  ],
  templateUrl: './formulario.component.html',
  styleUrls: ['./formulario.component.css']
})
export class FormularioComponent implements OnInit {

  usuario: Usuario = { nome: '', email: '', telefone: '', cpf: '', enderecos: [] };
  enderecos: Endereco[] = [];
  buscandoCeps: boolean[] = [];

  modoEdicao = false;
  usuarioId?: number;
  carregando = false;

  erros: { [key: string]: string } = {};

  constructor(
    private usuarioService: UsuarioService,
    private route: ActivatedRoute,
    private router: Router,
    private snackBar: MatSnackBar,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.modoEdicao = true;
      this.usuarioId = +id;
      this.carregarUsuario(this.usuarioId);
    }
  }

  carregarUsuario(id: number): void {
    this.carregando = true;
    this.usuarioService.buscarPorId(id).subscribe({
      next: (data) => {
        this.usuario = data;
        // Garante uma nova referência de array para o Angular detectar
        this.enderecos = data.enderecos ? [...data.enderecos] : [];
        this.buscandoCeps = this.enderecos.map(() => false);
        this.carregando = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.snackBar.open('Erro ao carregar usuário!', 'Fechar', { duration: 3000 });
        this.carregando = false;
        this.cdr.detectChanges();
      }
    });
  }

  novoEndereco(): Endereco {
    return { cep: '', rua: '', numero: null, bairro: '', cidade: '', estado: '' };
  }

  adicionarEndereco(): void {
    this.enderecos.push(this.novoEndereco());
    this.buscandoCeps.push(false);
    this.cdr.detectChanges();
  }

  removerEndereco(index: number): void {
    this.enderecos.splice(index, 1);
    this.buscandoCeps.splice(index, 1);
    this.cdr.detectChanges();
  }

  buscarCep(index: number): void {
    const endereco = this.enderecos[index];
    const cep = endereco.cep.replace(/\D/g, '');

    if (cep.length !== 8) {
      this.snackBar.open('CEP inválido! Digite 8 números.', 'Fechar', { duration: 3000 });
      return;
    }

    this.buscandoCeps[index] = true;
    this.cdr.detectChanges(); // Mostra o spinner imediatamente

    this.usuarioService.buscarCep(cep).subscribe({
      next: (data: any) => {
        if (data.erro) {
          this.snackBar.open('CEP não encontrado!', 'Fechar', { duration: 3000 });
        } else {
          // Atualiza as propriedades diretamente na referência do objeto na lista
          this.enderecos[index].rua = data.logradouro;
          this.enderecos[index].bairro = data.bairro;
          this.enderecos[index].cidade = data.localidade;
          this.enderecos[index].estado = data.uf;
          
          // Limpa erro visual se existir
          delete this.erros[`cep_${index}`];
        }

        this.buscandoCeps[index] = false;
        this.cdr.markForCheck(); // Avisa o Angular que os dados mudaram
        this.cdr.detectChanges(); // Força a renderização
      },
      error: (err) => {
        this.snackBar.open('Erro ao buscar CEP!', 'Fechar', { duration: 3000 });
        this.buscandoCeps[index] = false;
        this.cdr.detectChanges();
      }
    });
  }

  trackByIndex(index: number): number {
    return index;
  }

  validarCpf(cpf: string): boolean {
    cpf = cpf.replace(/\D/g, '');
    if (cpf.length !== 11 || /^(\d)\1+$/.test(cpf)) return false;
    let soma = 0;
    for (let i = 0; i < 9; i++) soma += parseInt(cpf[i]) * (10 - i);
    let resto = (soma * 10) % 11;
    if (resto === 10 || resto === 11) resto = 0;
    if (resto !== parseInt(cpf[9])) return false;
    soma = 0;
    for (let i = 0; i < 10; i++) soma += parseInt(cpf[i]) * (11 - i);
    resto = (soma * 10) % 11;
    if (resto === 10 || resto === 11) resto = 0;
    return resto === parseInt(cpf[10]);
  }

  validarEmail(email: string): boolean {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
  }

  validarFormulario(): boolean {
    this.erros = {};

    if (!this.usuario.nome?.trim()) this.erros['nome'] = 'Nome é obrigatório';
    if (!this.usuario.email?.trim()) this.erros['email'] = 'E-mail é obrigatório';
    else if (!this.validarEmail(this.usuario.email)) this.erros['email'] = 'E-mail inválido';

    if (!this.usuario.telefone?.trim()) this.erros['telefone'] = 'Telefone é obrigatório';
    if (!this.usuario.cpf?.trim()) this.erros['cpf'] = 'CPF é obrigatório';
    else if (!this.validarCpf(this.usuario.cpf)) this.erros['cpf'] = 'CPF inválido';

    this.enderecos.forEach((e, i) => {
      if (!e.cep?.trim()) this.erros[`cep_${i}`] = 'CEP é obrigatório';
      if (e.numero === null || e.numero === undefined) this.erros[`numero_${i}`] = 'Número é obrigatório';
    });

    return Object.keys(this.erros).length === 0;
  }

  salvar(): void {
  // 1. Validação local (campos vazios, formatos inválidos)
  if (!this.validarFormulario()) {
    this.snackBar.open('Existem campos obrigatórios vazios ou inválidos!', 'Fechar', { 
      duration: 4000,
      panelClass: ['error-snackbar'] 
    });
    return;
  }

  this.usuario.enderecos = this.enderecos;
  this.carregando = true;

  const operacao = this.modoEdicao
    ? this.usuarioService.atualizar(this.usuarioId!, this.usuario)
    : this.usuarioService.criar(this.usuario);

  operacao.subscribe({
    next: () => {
      this.carregando = false;
      this.snackBar.open(this.modoEdicao ? 'Usuário atualizado com sucesso!' : 'Usuário criado com sucesso!', 'Sucesso', { duration: 3000 });
      this.router.navigate(['/usuarios']);
    },
    error: (err) => {
      this.carregando = false;
      
      // Captura a mensagem de erro do Java (ex: "CPF já cadastrado")
      // Normalmente o Spring envia o erro dentro de err.error.message ou err.error
      let msgErro = 'Erro ao salvar: Verifique os dados.';

      if (err.error && typeof err.error === 'string') {
        msgErro = err.error;
      } else if (err.error && err.error.message) {
        msgErro = err.error.message;
      } else if (err.status === 400 || err.status === 409) {
        msgErro = 'Dados duplicados ou inválidos no sistema.';
      }

      this.snackBar.open(msgErro, 'Fechar', { 
        duration: 5000,
        panelClass: ['error-snackbar']
      });
      
      this.cdr.detectChanges();
    }
  });
}

  validarCampo(campo: string, index?: number) {
    switch (campo) {
      case 'nome':
        this.erros['nome'] = this.usuario.nome?.trim() ? '' : 'Nome é obrigatório';
        break;
      case 'email':
        if (!this.usuario.email?.trim()) this.erros['email'] = 'E-mail é obrigatório';
        else if (!this.validarEmail(this.usuario.email)) this.erros['email'] = 'E-mail inválido';
        else delete this.erros['email'];
        break;
      case 'telefone':
        this.erros['telefone'] = this.usuario.telefone?.trim() ? '' : 'Telefone é obrigatório';
        break;
      case 'cpf':
        if (!this.usuario.cpf?.trim()) this.erros['cpf'] = 'CPF é obrigatório';
        else if (!this.validarCpf(this.usuario.cpf)) this.erros['cpf'] = 'CPF inválido';
        else this.erros['cpf'] = '';
        break;
      case 'cep':
        if (index !== undefined) {
          const cep = this.enderecos[index].cep;
          this.erros[`cep_${index}`] = cep?.trim() ? '' : 'CEP é obrigatório';
        }
        break;
      case 'numero':
        if (index !== undefined) {
          const num = this.enderecos[index].numero;
          this.erros[`numero_${index}`] = num ? '' : 'Número é obrigatório';
        }
        break;
    }
    this.cdr.detectChanges();
  }

  voltar(): void {
    this.router.navigate(['/usuarios']);
  }
}