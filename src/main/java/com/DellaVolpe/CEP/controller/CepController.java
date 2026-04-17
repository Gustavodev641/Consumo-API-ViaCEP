package com.DellaVolpe.CEP.controller;

import com.DellaVolpe.CEP.client.ViaCepClient;
import com.DellaVolpe.CEP.dto.CepResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cep")
@CrossOrigin(origins = "*")
public class CepController {

    @Autowired
    private ViaCepClient viaCepClient;

    @GetMapping("/{cep}")
    public ResponseEntity<CepResponseDTO> buscarCep(@PathVariable String cep) {
        return ResponseEntity.ok(viaCepClient.buscarCep(cep));
    }
}