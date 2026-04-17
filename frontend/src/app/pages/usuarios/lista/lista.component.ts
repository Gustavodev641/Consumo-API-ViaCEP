import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { UsuarioService } from '../../../core/services/usuario.service';
import { Usuario } from '../../../models/usuario.model';

@Component({
  selector: 'app-lista',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatTableModule,
    MatButtonModule,
    MatInputModule,
    MatIconModule,
    MatSnackBarModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './lista.component.html',
  styleUrls: ['./lista.component.css']
})
export class ListaComponent implements OnInit {

  usuarios: Usuario[] = [];
  usuariosFiltrados: Usuario[] = [];
  termoBusca: string = '';
  carregando: boolean = false;
  colunas = ['nome', 'email', 'telefone', 'acoes'];

  constructor(
    private usuarioService: UsuarioService,
    private router: Router,
    private snackBar: MatSnackBar,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.carregarUsuarios();
  }

  carregarUsuarios(): void {
    this.carregando = true;
    this.usuarioService.listarTodos().subscribe({
      next: (data) => {
        this.usuarios = data;
        this.usuariosFiltrados = data;
        this.carregando = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        const msg = err.error || 'Erro ao carregar usuários!';
        this.snackBar.open(msg, 'Fechar', { duration: 3000 });
        this.carregando = false;
        this.cdr.detectChanges();
      }
    });
  }

  filtrar(): void {
    const termo = this.termoBusca.toLowerCase();
    this.usuariosFiltrados = this.usuarios.filter(u =>
      u.nome.toLowerCase().includes(termo) ||
      u.email.toLowerCase().includes(termo)
    );
  }

  visualizar(id: number): void {
    this.router.navigateByUrl('/', { skipLocationChange: true }).then(() => {
      this.router.navigate(['/usuarios', id]);
    });
  }

  novo(): void {
    this.router.navigate(['/usuarios/novo']);
  }

  editar(id: number): void {
    this.router.navigate(['/usuarios', id, 'editar']);
  }

  deletar(id: number): void {
    // Confirmação certeira antes de deletar
    if (confirm('Deseja realmente deletar este usuário?')) {
      this.usuarioService.deletar(id).subscribe({
        next: () => {
          this.snackBar.open('Usuário deletado com sucesso!', 'Fechar', { duration: 3000 });
          this.carregarUsuarios();
        },
        error: (err) => {
          // Se o backend enviar uma mensagem de erro (ex: ORA-xxxxx), ela será mostrada aqui
          const msgErro = err.error || 'Erro ao deletar usuário!';
          this.snackBar.open(msgErro, 'Fechar', { duration: 5000 });
        }
      });
    }
  }
}