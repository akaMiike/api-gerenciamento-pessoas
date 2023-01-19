package com.attornatus.gerenciamentopessoas.services;

import com.attornatus.gerenciamentopessoas.entities.Pessoa;
import com.attornatus.gerenciamentopessoas.exceptions.ParametrosInvalidosException;
import com.attornatus.gerenciamentopessoas.exceptions.pessoa.PessoaNaoEncontradaException;
import com.attornatus.gerenciamentopessoas.repositories.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PessoaService {

    @Autowired
    private PessoaRepository pessoaRepository;

    public void criar(Pessoa pessoa){
        pessoaRepository.save(pessoa);
    }

    public void atualizar(Pessoa pessoaAtualizada, Integer id){
        if(pessoaAtualizada.getNome() == null && pessoaAtualizada.getDataNascimento() == null){
            throw new ParametrosInvalidosException("Dados da pessoa devem conter ao menos um campo não vazio.");
        }

        Pessoa pessoaAntiga = buscarPorId(id);

        Optional.ofNullable(pessoaAtualizada.getNome()).ifPresent(pessoaAntiga::setNome);
        Optional.ofNullable(pessoaAtualizada.getDataNascimento()).ifPresent(pessoaAntiga::setDataNascimento);

        pessoaRepository.save(pessoaAntiga);
    }

    public Pessoa buscarPorId(Integer id){
        return pessoaRepository.findById(id).orElseThrow(
                () -> new PessoaNaoEncontradaException()
        );
    }

    public List<Pessoa> buscarTodas(){
        return pessoaRepository.findAll();
    }

    public boolean existePessoa(Integer idPessoa){
        return pessoaRepository.existsById(idPessoa);
    }
}
