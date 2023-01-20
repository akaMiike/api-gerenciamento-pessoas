package com.attornatus.gerenciamentopessoas.controllers;

import com.attornatus.gerenciamentopessoas.dto.endereco.EnderecoCreateDTO;
import com.attornatus.gerenciamentopessoas.dto.endereco.EnderecoReturnDTO;
import com.attornatus.gerenciamentopessoas.dto.pessoa.PessoaCreateDTO;
import com.attornatus.gerenciamentopessoas.dto.pessoa.PessoaReturnDTO;
import com.attornatus.gerenciamentopessoas.dto.pessoa.PessoaUpdateDTO;
import com.attornatus.gerenciamentopessoas.entities.Endereco;
import com.attornatus.gerenciamentopessoas.entities.Pessoa;
import com.attornatus.gerenciamentopessoas.services.EnderecoService;
import com.attornatus.gerenciamentopessoas.services.PessoaService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/pessoas")
public class PessoaController {

    @Autowired
    private PessoaService pessoaService;
    @Autowired
    private EnderecoService enderecoService;
    @Autowired
    private ModelMapper mapper;

    @GetMapping
    public ResponseEntity<List<PessoaReturnDTO>> listarPessoas(){
        List<PessoaReturnDTO> todasPessoas = pessoaService.buscarTodas()
                .stream()
                .map(pessoa -> mapper.map(pessoa, PessoaReturnDTO.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(todasPessoas);

    }

    @GetMapping("/{idPessoa}/endereco")
    public ResponseEntity<List<EnderecoReturnDTO>> listarEnderecosPessoa(@PathVariable("idPessoa") Integer idPessoa){
        List<EnderecoReturnDTO> enderecos = enderecoService.buscarEnderecosPessoaPorId(idPessoa).stream()
                .map(endereco -> mapper.map(endereco, EnderecoReturnDTO.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(enderecos);
    }

    @GetMapping("/{idPessoa}")
    public ResponseEntity<PessoaReturnDTO> buscarPessoaPorId(@PathVariable("idPessoa") Integer idPessoa){
        Pessoa pessoa = pessoaService.buscarPorId(idPessoa);
        return ResponseEntity.ok(mapper.map(pessoa, PessoaReturnDTO.class));
    }

    @PutMapping("/{idPessoa}")
    public ResponseEntity<Void> atualizarDadosPessoa(@RequestBody @Valid PessoaUpdateDTO pessoa,
                                                       @PathVariable("idPessoa") Integer idPessoa){

        pessoaService.atualizar(mapper.map(pessoa, Pessoa.class), idPessoa);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<Void> criarNovaPessoa(@RequestBody @Valid PessoaCreateDTO novaPessoa){
        pessoaService.criar(mapper.map(novaPessoa, Pessoa.class));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/{idPessoa}/endereco")
    public ResponseEntity<Void> criarNovoEndereco(@RequestBody @Valid EnderecoCreateDTO endereco,
                                                    @PathVariable("idPessoa") Integer idPessoa){
        enderecoService.salvar(mapper.map(endereco, Endereco.class), idPessoa);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{idPessoa}/endereco/{idEndereco}")
    public ResponseEntity<Void> definirEnderecoPrincipal(@PathVariable("idPessoa") Integer idPessoa,
                                                               @PathVariable("idEndereco") Integer idEndereco){

        enderecoService.salvarEnderecoPrincipalPessoa(idEndereco, idPessoa);
        return ResponseEntity.noContent().build();
    }
}

