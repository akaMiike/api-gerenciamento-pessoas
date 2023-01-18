package com.attornatus.gerenciamentopessoas.services;

import com.attornatus.gerenciamentopessoas.entities.Pessoa;
import com.attornatus.gerenciamentopessoas.repositories.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PessoaService {

    @Autowired
    private PessoaRepository pessoaRepository;

    public void criar(Pessoa pessoa){
        pessoaRepository.save(pessoa);
    }

    public void atualizar(Pessoa pessoaAtualizada, Integer id){
        if(!pessoaRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa não encontrada.");
        }

        pessoaAtualizada.setId(id);
        pessoaRepository.save(pessoaAtualizada);
    }

    public Pessoa buscarPorId(Integer id){
        return pessoaRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa não encontrada.")
        );
    }

    public List<Pessoa> buscarTodas(){
        return pessoaRepository.findAll();
    }

}
