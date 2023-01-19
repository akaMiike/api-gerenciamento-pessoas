package com.attornatus.gerenciamentopessoas.services;

import com.attornatus.gerenciamentopessoas.entities.Endereco;
import com.attornatus.gerenciamentopessoas.entities.Pessoa;
import com.attornatus.gerenciamentopessoas.exceptions.endereco.EnderecoNaoEncontradoException;
import com.attornatus.gerenciamentopessoas.exceptions.pessoa.PessoaNaoEncontradaException;
import com.attornatus.gerenciamentopessoas.repositories.EnderecoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnderecoService {

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private PessoaService pessoaService;

    public void salvar(Endereco novoEndereco, Integer idPessoa){
        Pessoa pessoa = pessoaService.buscarPorId(idPessoa);
        novoEndereco.setPessoa(pessoa);

        enderecoRepository.save(novoEndereco);
    }

    public List<Endereco> buscarEnderecosPessoaPorId(Integer idPessoa){
        Pessoa pessoa = pessoaService.buscarPorId(idPessoa);
        return pessoa.getEnderecos();
    }

    public Endereco buscarEnderecoPrincipalPessoaPorId(Integer idPessoa){
        if(!pessoaService.existePessoa(idPessoa)){
            throw new PessoaNaoEncontradaException();
        }
        return enderecoRepository.buscarEnderecoPrincipalPorPessoa(idPessoa)
                .orElseThrow(() -> new EnderecoNaoEncontradoException("Endereço principal não encontrado.")
        );
    }

    @Transactional
    public void salvarEnderecoPrincipalPessoa(Integer idEndereco, Integer idPessoa){
        if(!pessoaService.existePessoa(idPessoa)){
            throw new PessoaNaoEncontradaException();
        }

        enderecoRepository.removerEnderecoPrincipalAtualPessoa(idPessoa);

        Endereco enderecoPrincipalAtual = buscarPorId(idEndereco);
        enderecoPrincipalAtual.setPrincipal(true);
        enderecoRepository.save(enderecoPrincipalAtual);
    }

    public Endereco buscarPorId(Integer id){
        return enderecoRepository.findById(id).orElseThrow(
                () -> new EnderecoNaoEncontradoException()
        );
    }
    


}
