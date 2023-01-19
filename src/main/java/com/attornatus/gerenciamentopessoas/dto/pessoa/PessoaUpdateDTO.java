package com.attornatus.gerenciamentopessoas.dto.pessoa;

import jakarta.validation.constraints.Pattern;

public class PessoaUpdateDTO {
    private String nome;

    @Pattern(regexp = "([0-2][0-9]|3[0-1])/(0[1-9]|1[0-2])/[0-9]{4}", message = "Formato da data de nascimento inválida")
    private String dataNascimento;

    public PessoaUpdateDTO() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
}
