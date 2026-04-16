package com.DellaVolpe.CEP;

import com.DellaVolpe.CEP.client.ViaCepClient;
import com.DellaVolpe.CEP.dto.CepResponseDTO;
import com.DellaVolpe.CEP.entitys.Endereco;
import com.DellaVolpe.CEP.entitys.Usuario;
import com.DellaVolpe.CEP.repository.UsuarioRepository;
import com.DellaVolpe.CEP.service.UsuarioService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ViaCepClient viaCepClient;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    @DisplayName("Deve criar um usuário com sucesso quando o CEP for válido")
    void deveCriarUsuarioComSucesso() {
        Usuario usuario = new Usuario();
        usuario.setNome("Gabriel");

        Endereco endereco = new Endereco();
        endereco.setCep("01001000");

        List<Endereco> listaEnderecos = new ArrayList<>();
        listaEnderecos.add(endereco);
        usuario.setEnderecos(listaEnderecos);

        CepResponseDTO mockCep = new CepResponseDTO();
        mockCep.setLogradouro("Praça da Sé");
        mockCep.setBairro("Sé");
        mockCep.setLocalidade("São Paulo");
        mockCep.setUf("SP");

        when(viaCepClient.buscarCep(anyString())).thenReturn(mockCep);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario resultado = usuarioService.criar(usuario);

        assertNotNull(resultado);
        assertEquals("Gabriel", resultado.getNome());
        assertEquals("Praça da Sé", resultado.getEnderecos().get(0).getRua());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o CEP for inválido")
    void deveLancarExcecaoQuandoCepInvalido() {
        Usuario usuario = new Usuario();
        Endereco endereco = new Endereco();
        endereco.setCep("00000000");
        usuario.setEnderecos(List.of(endereco));

        when(viaCepClient.buscarCep(anyString()))
                .thenThrow(new RuntimeException("CEP inválido: 00000000"));

        assertThrows(RuntimeException.class, () -> usuarioService.criar(usuario));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve retornar um usuário ao buscar por ID existente")
    void deveBuscarUsuarioPorId() {
        // GIVEN
        Long id = 1L;
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setNome("Gabriel");

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));

        // WHEN
        Usuario resultado = usuarioService.buscarPorId(id);

        // THEN
        assertNotNull(resultado);
        assertEquals("Gabriel", resultado.getNome());
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar ID inexistente")
    void deveLancarErroAoBuscarIdInexistente() {
   
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.empty());


        assertThrows(RuntimeException.class, () -> usuarioService.buscarPorId(1L));
    }

    @Test
    @DisplayName("Deve deletar um usuário com sucesso")
    void deveDeletarUsuario() {
        Long id = 1L;
        Usuario usuario = new Usuario();
        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));

        usuarioService.deletar(id);

        verify(usuarioRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Deve atualizar dados do usuário e endereços com sucesso")
    void deveAtualizarUsuario() {
        // GIVEN
        Long id = 1L;

        Usuario usuarioAntigo = new Usuario();
        usuarioAntigo.setId(id);
        usuarioAntigo.setNome("Gabriel Antigo");
        usuarioAntigo.setEnderecos(new ArrayList<>());

        Usuario novosDados = new Usuario();
        novosDados.setNome("Gabriel Atualizado");
        novosDados.setEmail("gabriel@novo.com");

        Endereco novoEndereco = new Endereco();
        novoEndereco.setCep("01001000");
        List<Endereco> novosEnderecos = new ArrayList<>();
        novosEnderecos.add(novoEndereco);
        novosDados.setEnderecos(novosEnderecos);

        CepResponseDTO mockCep = new CepResponseDTO();
        mockCep.setLogradouro("Rua Nova");

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioAntigo));
        when(viaCepClient.buscarCep(anyString())).thenReturn(mockCep);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioAntigo);

        Usuario resultado = usuarioService.atualizar(id, novosDados);

        assertEquals("Gabriel Atualizado", resultado.getNome());
        assertEquals("Rua Nova", resultado.getEnderecos().get(0).getRua());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }
}