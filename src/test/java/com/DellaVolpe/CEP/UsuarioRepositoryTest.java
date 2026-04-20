package com.DellaVolpe.CEP;


import com.DellaVolpe.CEP.entitys.Endereco;
import com.DellaVolpe.CEP.entitys.Usuario;
import com.DellaVolpe.CEP.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("dev") // usa o H2 do application-dev.properties
class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario criarUsuario() {
        Usuario u = new Usuario();
        u.setNome("João Silva");
        u.setEmail("joao@email.com");
        u.setTelefone("11999999999");

        Endereco e = new Endereco();
        e.setCep("01001000");
        e.setRua("Praça da Sé");
        e.setNumero(1);
        e.setBairro("Sé");
        e.setCidade("São Paulo");
        e.setEstado("SP");
        e.setUsuario(u);

        u.getEnderecos().add(e);
        return u;
    }

    @Test
    void deveSalvarEBuscarUsuario() {
        Usuario salvo = usuarioRepository.save(criarUsuario());

        Optional<Usuario> result = usuarioRepository.findById(salvo.getId());

        assertTrue(result.isPresent());
        assertEquals("João Silva", result.get().getNome());
        assertEquals(1, result.get().getEnderecos().size());
    }

    @Test
    void deveListarTodosUsuarios() {
        usuarioRepository.save(criarUsuario());
        usuarioRepository.save(criarUsuario());

        List<Usuario> lista = usuarioRepository.findAll();

        assertEquals(2, lista.size());
    }

    @Test
    void deveDeletarUsuario() {
        Usuario salvo = usuarioRepository.save(criarUsuario());
        Long id = salvo.getId();

        usuarioRepository.deleteById(id);

        assertFalse(usuarioRepository.findById(id).isPresent());
    }

    @Test
    void deveRetornarVazioParaIdInexistente() {
        Optional<Usuario> result = usuarioRepository.findById(999L);

        assertFalse(result.isPresent());
    }

    @Test
    void deveAtualizarUsuario() {
        Usuario salvo = usuarioRepository.save(criarUsuario());
        salvo.setNome("João Atualizado");

        Usuario atualizado = usuarioRepository.save(salvo);

        assertEquals("João Atualizado", atualizado.getNome());
    }
}
