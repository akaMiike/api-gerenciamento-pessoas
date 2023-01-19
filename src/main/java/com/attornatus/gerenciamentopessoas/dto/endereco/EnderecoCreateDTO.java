package com.attornatus.gerenciamentopessoas.dto.endereco;

import jakarta.validation.constraints.NotEmpty;

public class EnderecoCreateDTO {
    @NotEmpty(message = "Logradouro não pode ser vazio.")
    private String logradouro;

    @NotEmpty(message = "CEP não pode ser vazio.")
    private String cep;

    @NotEmpty(message = "Número não pode ser vazio.")
    private String numero;

    @NotEmpty(message = "Cidade não pode ser vazia.")
    private String cidade;

    public EnderecoCreateDTO() {
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }
}
