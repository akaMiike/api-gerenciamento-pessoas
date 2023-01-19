package com.attornatus.gerenciamentopessoas.services;

import com.attornatus.gerenciamentopessoas.entities.Endereco;
import com.attornatus.gerenciamentopessoas.entities.Pessoa;
import com.attornatus.gerenciamentopessoas.repositories.EnderecoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa não encontrada");
        }
        return enderecoRepository.buscarEnderecoPrincipalPorPessoa(idPessoa)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Endereço principal não encontrado")
        );
    }

    @Transactional
    public void salvarEnderecoPrincipalPessoa(Integer idEndereco, Integer idPessoa){
        if(!pessoaService.existePessoa(idPessoa)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa não encontrada");
        }

        enderecoRepository.removerEnderecoPrincipalAtualPessoa(idPessoa);

        Endereco enderecoPrincipalAtual = buscarPorId(idEndereco);
        enderecoPrincipalAtual.setPrincipal(true);
        enderecoRepository.save(enderecoPrincipalAtual);
    }

    public Endereco buscarPorId(Integer id){
        return enderecoRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Endereço não encontrado")
        );
    }
    


}
