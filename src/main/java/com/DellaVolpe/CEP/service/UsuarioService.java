package com.DellaVolpe.CEP.service;

import com.DellaVolpe.CEP.client.ViaCepClient;
import com.DellaVolpe.CEP.dto.CepResponseDTO;
import com.DellaVolpe.CEP.entitys.Endereco;
import com.DellaVolpe.CEP.entitys.Usuario;
import com.DellaVolpe.CEP.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        // Endereços são opcionais — só valida CEP se houver endereços
        if (usuario.getEnderecos() != null) {
            for (Endereco e : usuario.getEnderecos()) {
                preencherEndereco(e, usuario);
            }
        }
        return usuarioRepository.save(usuario);
    }

    public Usuario atualizar(Long id, Usuario dados) {
        Usuario usuario = buscarPorId(id);

        usuario.setNome(dados.getNome());
        usuario.setEmail(dados.getEmail());
        usuario.setTelefone(dados.getTelefone());
        usuario.setCpf(dados.getCpf());

        usuario.getEnderecos().clear();

        if (dados.getEnderecos() != null) {
            for (Endereco e : dados.getEnderecos()) {
                preencherEndereco(e, usuario);
                usuario.getEnderecos().add(e);
            }
        }

        return usuarioRepository.save(usuario);
    }

    public void deletar(Long id) {
        buscarPorId(id);
        usuarioRepository.deleteById(id);
    }

    // Endereço não pode existir sem usuário — vínculo feito aqui
    private void preencherEndereco(Endereco e, Usuario usuario) {
        CepResponseDTO cepData = viaCepClient.buscarCep(e.getCep());
        e.setRua(cepData.getLogradouro());
        e.setBairro(cepData.getBairro());
        e.setCidade(cepData.getLocalidade());
        e.setEstado(cepData.getUf());
        e.setUsuario(usuario); // endereço SEMPRE vinculado ao usuário
    }
}