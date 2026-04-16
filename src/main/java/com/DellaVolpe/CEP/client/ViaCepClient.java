package com.DellaVolpe.CEP.client;

import com.DellaVolpe.CEP.dto.CepResponseDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ViaCepClient {
    private static final RestTemplate restTemplate = new RestTemplate();


    public static CepResponseDTO buscarCep(String cep){
        String cepLimpo = cep.replace("-", "").replace(" ", "");

        String url = "https://viacep.com.br/ws/" + cepLimpo + "/json/";
        return restTemplate.getForObject(url, CepResponseDTO.class);
    }
}
