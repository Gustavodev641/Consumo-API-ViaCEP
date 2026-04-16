package com.DellaVolpe.CEP.exception;

public class CepInvalidoException extends RuntimeException {
    public CepInvalidoException(String cep) {
        super("CEP inválido: " + cep);
    }
}
