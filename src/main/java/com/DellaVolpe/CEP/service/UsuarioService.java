package com.DellaVolpe.CEP.service;

import com.DellaVolpe.CEP.client.ViaCepClient;
import com.DellaVolpe.CEP.dto.CepResponseDTO;
import com.DellaVolpe.CEP.entitys.Endereco;
import com.DellaVolpe.CEP.entitys.Usuario;
import com.DellaVolpe.CEP.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ViaCepClient viaCepClient;

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public Usuario criar(Usuario usuario) {
        for (Endereco e : usuario.getEnderecos()) {
            CepResponseDTO cepData = viaCepClient.buscarCep(e.getCep());
            e.setRua(cepData.getLogradouro());
            e.setBairro(cepData.getBairro());
            e.setCidade(cepData.getLocalidade());
            e.setEstado(cepData.getUf());
            e.setUsuario(usuario);
        }
        return usuarioRepository.save(usuario);
    }

    public Usuario atualizar(Long id, Usuario dados) {
        Usuario usuario = buscarPorId(id);

        usuario.setNome(dados.getNome());
        usuario.setEmail(dados.getEmail());
        usuario.setTelefone(dados.getTelefone());

        usuario.getEnderecos().clear();

        for (Endereco e : dados.getEnderecos()) {
            CepResponseDTO cepData = viaCepClient.buscarCep(e.getCep());
            e.setRua(cepData.getLogradouro());
            e.setBairro(cepData.getBairro());
            e.setCidade(cepData.getLocalidade());
            e.setEstado(cepData.getUf());
            e.setUsuario(usuario);
            usuario.getEnderecos().add(e);
        }

        return usuarioRepository.save(usuario);
    }

    public void deletar(Long id) {
        buscarPorId(id);
        usuarioRepository.deleteById(id);
    }
}
