import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
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
import { ChangeDetectorRef } from '@angular/core';

@Component({
  selector: 'app-formulario',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatSnackBarModule,
    MatProgressSpinnerModule,
    MatCardModule,
    MatDividerModule
  ],
  templateUrl: './formulario.component.html',
  styleUrls: ['./formulario.component.css']
})
export class FormularioComponent implements OnInit {

  usuario: Usuario = { nome: '', email: '', telefone: '', enderecos: [] };
  endereco: Endereco = { cep: '', rua: '', numero: '', bairro: '', cidade: '', estado: '' };

  modoEdicao = false;
  usuarioId?: number;
  carregando = false;
  buscandoCep = false;

  constructor(
    private usuarioService: UsuarioService,
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient,
    private snackBar: MatSnackBar,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    console.log('ID da rota:', id);
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
        console.log('Usuário carregado:', data);
        this.usuario = data;
        if (data.enderecos && data.enderecos.length > 0) {
          this.endereco = { ...data.enderecos[0] };
        }
        this.carregando = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.log('Erro ao carregar:', err);
        this.snackBar.open('Erro ao carregar usuário!', 'Fechar', { duration: 3000 });
        this.carregando = false;
        this.cdr.detectChanges();
      }
    });
  }

  buscarCep(): void {
    const cep = this.endereco.cep.replace(/\D/g, '');
    console.log('CEP digitado:', cep);
    console.log('Tamanho:', cep.length);

    if (cep.length !== 8) {
      this.snackBar.open('CEP inválido! Digite 8 números.', 'Fechar', { duration: 3000 });
      return;
    }

    this.buscandoCep = true;
    console.log('Chamando backend...');
    this.usuarioService.buscarCep(cep).subscribe({
      next: (data) => {
        console.log('Retorno do CEP:', data);
        this.endereco.rua = data.logradouro;
        this.endereco.bairro = data.bairro;
        this.endereco.cidade = data.localidade;
        this.endereco.estado = data.uf;
        this.buscandoCep = false;
        this.cdr.detectChanges(); 
      },
      error: (err) => {
        console.log('Erro:', err);
        this.snackBar.open('CEP inválido!', 'Fechar', { duration: 3000 });
        this.buscandoCep = false;
      }
    });
  }

  salvar(): void {
    this.usuario.enderecos = [this.endereco];
    this.carregando = true;

    const operacao = this.modoEdicao
      ? this.usuarioService.atualizar(this.usuarioId!, this.usuario)
      : this.usuarioService.criar(this.usuario);

    operacao.subscribe({
      next: () => {
        this.snackBar.open(
          this.modoEdicao ? 'Usuário atualizado!' : 'Usuário criado!',
          'Fechar', { duration: 3000 }
        );
        this.router.navigate(['/usuarios']);
      },
      error: () => {
        this.snackBar.open('Erro ao salvar usuário!', 'Fechar', { duration: 3000 });
        this.carregando = false;
      }
    });
  }

  voltar(): void {
    this.router.navigate(['/usuarios']);
  }
}