package com.attornatus.gerenciamentopessoas.services;

import com.attornatus.gerenciamentopessoas.entities.Pessoa;
import com.attornatus.gerenciamentopessoas.exceptions.pessoa.PessoaNaoEncontradaException;
import com.attornatus.gerenciamentopessoas.repositories.PessoaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PessoaServiceTests {

    @Autowired
    private PessoaService pessoaService;
    @MockBean
    private PessoaRepository pessoaRepository;

    @Test
    @DisplayName("QUANDO criar uma pessoa com dados válidos DEVE criar a nova pessoa")
    public void criarPessoaComDadosValidos(){
        Pessoa novaPessoa = new Pessoa();

        novaPessoa.setNome("Michael");
        novaPessoa.setDataNascimento("28/09/2001");

        pessoaService.criar(novaPessoa);
        verify(pessoaRepository,times(1)).save(novaPessoa);
    }

    @Test
    @DisplayName("QUANDO atualizar uma pessoa com id válido DEVE atualizar os dados da pessoa")
    public void atualizarPessoaComIdValido(){
        Pessoa pessoa = new Pessoa();
        pessoa.setId(1);
        pessoa.setNome("Michael");
        pessoa.setDataNascimento("28/09/2001");

        when(pessoaRepository.existsById(anyInt())).thenReturn(true);
        when(pessoaRepository.findById(anyInt())).thenReturn(Optional.of(pessoa));

        pessoaService.atualizar(pessoa, 1);
        verify(pessoaRepository,times(1)).save(pessoa);
    }

    @Test
    @DisplayName("QUANDO atualizar uma pessoa com id inválido DEVE retornar exceção")
    public void atualizarPessoaComIdInvalido(){
        Pessoa pessoa = new Pessoa();

        pessoa.setNome("Michael");
        pessoa.setDataNascimento("28/09/2001");

        when(pessoaRepository.existsById(anyInt())).thenReturn(false);
        Assertions.assertThrows(PessoaNaoEncontradaException.class,
                () -> pessoaService.atualizar(pessoa, anyInt())
        );
    }

    @Test
    @DisplayName("QUANDO buscar pessoa por id por uma pessoa existente DEVE retornar a pessoa")
    public void buscarPessoaPorIdComPessoaExistente(){
        Pessoa pessoa = new Pessoa();

        pessoa.setId(1);
        pessoa.setNome("Michael");
        pessoa.setDataNascimento("28/09/2001");

        when(pessoaRepository.findById(anyInt())).thenReturn(Optional.of(pessoa));

        Pessoa pessoaObtida = pessoaService.buscarPorId(1);
        assertThat(pessoaObtida).isEqualTo(pessoa);
    }

    @Test
    @DisplayName("QUANDO buscar pessoa por id por uma pessoa não existente DEVE retornar uma exceção")
    public void buscarPessoaPorIdComPessoaNaoExistente(){
        when(pessoaRepository.findById(anyInt())).thenReturn(Optional.empty());

        Assertions.assertThrows(ResponseStatusException.class,
                () -> pessoaService.buscarPorId(1)
        );
    }

    @Test
    @DisplayName("QUANDO buscar por todas as pessoas DEVE retornar todas as pessoas existentes")
    public void buscarTodasPessoas(){
        when(pessoaRepository.findAll()).thenReturn(new ArrayList<>());

        List<Pessoa> pessoas = pessoaService.buscarTodas();
        assertThat(pessoas).isEqualTo(new ArrayList<>());
    }

}
