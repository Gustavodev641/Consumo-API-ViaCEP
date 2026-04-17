import { Component, OnInit, ChangeDetectorRef } from '@angular/core'; 
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDividerModule } from '@angular/material/divider';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { UsuarioService } from '../../../core/services/usuario.service';
import { Usuario } from '../../../models/usuario.model';

@Component({
  selector: 'app-detalhes',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatDividerModule,
    MatProgressSpinnerModule,
    MatSnackBarModule
  ],
  templateUrl: './detalhes.component.html',
  styleUrls: ['./detalhes.component.css']
})
export class DetalhesComponent implements OnInit {
  usuario?: Usuario;
  carregando = false;

  constructor(
    private usuarioService: UsuarioService,
    private route: ActivatedRoute,
    private router: Router,
    private snackBar: MatSnackBar,
    private cdRef: ChangeDetectorRef 
  ) { }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.carregarUsuario(+id);
      }
    });
  }

  carregarUsuario(id: number): void {
    this.carregando = true;
    this.cdRef.detectChanges(); 

    this.usuarioService.buscarPorId(id).subscribe({
      next: (data) => {
        console.log('Dados recebidos:', data);
        this.usuario = data;
        this.carregando = false;
        this.cdRef.detectChanges(); 
      },
      error: (err) => {
        console.error('Erro na busca:', err);
        this.carregando = false;
        this.snackBar.open('Erro ao carregar usuário!', 'Fechar', { duration: 3000 });
        this.cdRef.detectChanges();
      }
    });
  }

  voltar(): void {
    this.router.navigate(['/usuarios']);
  }
}