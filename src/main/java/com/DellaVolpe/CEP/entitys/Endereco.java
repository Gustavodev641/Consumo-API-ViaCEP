package com.DellaVolpe.CEP.entitys;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "ENDERECO", schema = "APP_USER")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @NotBlank(message = "CEP é obrigatório")
    @Column(name = "CEP")
    private String cep;

    @Column(name = "RUA")
    private String rua;

    @NotNull(message = "Número é obrigatório")
    @Column(name = "NUMERO")
    private Integer numero;

    @Column(name = "BAIRRO")
    private String bairro;

    @Column(name = "ESTADO")
    private String estado;

    @Column(name = "CIDADE")
    private String cidade;

    @ManyToOne
    @JoinColumn(name = "USUARIO_ID")
    @JsonIgnore
    private Usuario usuario;
}