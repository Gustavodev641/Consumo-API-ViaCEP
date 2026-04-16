package com.DellaVolpe.CEP.client;

import com.DellaVolpe.CEP.dto.CepResponseDTO;
import com.DellaVolpe.CEP.exception.CepInvalidoException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ViaCepClient {
    private static final RestTemplate restTemplate = new RestTemplate();


    public CepResponseDTO buscarCep(String cep){
        String cepLimpo = cep.replace("-", "").replace(" ", "");

        String url = "https://viacep.com.br/ws/" + cepLimpo + "/json/";
        CepResponseDTO response = restTemplate.getForObject(url, CepResponseDTO.class);


        if (response == null || response.isErro()) {
            throw new CepInvalidoException(cep);
        }

        return response;
    }
}
