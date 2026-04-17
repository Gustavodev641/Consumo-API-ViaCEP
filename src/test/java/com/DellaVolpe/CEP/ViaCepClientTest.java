package com.DellaVolpe.CEP;

import com.DellaVolpe.CEP.client.ViaCepClient;
import com.DellaVolpe.CEP.dto.CepResponseDTO;
import com.DellaVolpe.CEP.exception.CepInvalidoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ViaCepClientTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ViaCepClient viaCepClient;

    private static final String CEP_VALIDO = "01001000";
    private static final String URL = "https://viacep.com.br/ws/" + CEP_VALIDO + "/json/";

    @Test
    void deveBuscarCepValido() {
        CepResponseDTO mockResponse = new CepResponseDTO();
        mockResponse.setLogradouro("Praça da Sé");
        mockResponse.setBairro("Sé");
        mockResponse.setLocalidade("São Paulo");
        mockResponse.setUf("SP");
        mockResponse.setErro(false);

        when(restTemplate.getForObject(URL, CepResponseDTO.class)).thenReturn(mockResponse);

        CepResponseDTO result = viaCepClient.buscarCep(CEP_VALIDO);

        assertNotNull(result);
        assertEquals("Praça da Sé", result.getLogradouro());
        assertEquals("SP", result.getUf());
    }

    @Test
    void deveLancarExcecaoQuandoCepInvalido() {
        CepResponseDTO mockResponse = new CepResponseDTO();
        mockResponse.setErro(true);

        when(restTemplate.getForObject(URL, CepResponseDTO.class)).thenReturn(mockResponse);

        assertThrows(CepInvalidoException.class, () -> viaCepClient.buscarCep(CEP_VALIDO));
    }

    @Test
    void deveLancarExcecaoQuandoRespostaNull() {
        when(restTemplate.getForObject(URL, CepResponseDTO.class)).thenReturn(null);

        assertThrows(CepInvalidoException.class, () -> viaCepClient.buscarCep(CEP_VALIDO));
    }

    @Test
    void deveRemoverHifenDoCepAntesDeConsultar() {
        String cepComHifen = "01001-000";
        String urlEsperada = "https://viacep.com.br/ws/01001000/json/";

        CepResponseDTO mockResponse = new CepResponseDTO();
        mockResponse.setErro(false);
        mockResponse.setLogradouro("Praça da Sé");

        when(restTemplate.getForObject(urlEsperada, CepResponseDTO.class)).thenReturn(mockResponse);

        CepResponseDTO result = viaCepClient.buscarCep(cepComHifen);

        assertNotNull(result);
        verify(restTemplate).getForObject(urlEsperada, CepResponseDTO.class);
    }
}
