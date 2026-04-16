package com.DellaVolpe.CEP.repository;

import com.DellaVolpe.CEP.entitys.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
