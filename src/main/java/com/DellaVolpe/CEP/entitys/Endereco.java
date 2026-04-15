package com.DellaVolpe.CEP.entitys;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class Endereco {

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "CEP")
    private String cep;

    @Column(name = "Rua")
    private String rua;

    @Column(name = "Numero")
    private int numero;

    @Column(name = "Bairro")
    private String bairro;

    @Column(name = "Estado")
    private String estado;

    @Column(name = "Cidade")
    private String cidade;



}
