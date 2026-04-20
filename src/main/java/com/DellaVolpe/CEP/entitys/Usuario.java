package com.DellaVolpe.CEP.entitys;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "USUARIO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Column(name = "NOME", nullable = false)
    private String nome;

    @NotBlank(message = "E-mail é obrigatório")
    @Email(message = "E-mail inválido")
    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Telefone é obrigatório")
    @Column(name = "TELEFONE", nullable = false)
    private String telefone;

    @CPF(message = "CPF inválido")
    @NotBlank(message = "CPF é obrigatório")
    @Column(name = "CPF", nullable = false, unique = true)
    private String cpf;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Endereco> enderecos = new ArrayList<>();
}