import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-formulario',
  standalone: true,  // Adicione esta linha se não estiver presente
  imports: [CommonModule /* outros imports necessários */],
  templateUrl: './formulario.component.html',
  styleUrls: ['./formulario.component.css']
})
export class FormularioComponent {
  // ...existing code...
}