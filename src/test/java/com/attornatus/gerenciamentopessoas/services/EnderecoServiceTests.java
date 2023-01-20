package com.attornatus.gerenciamentopessoas.services;

import com.attornatus.gerenciamentopessoas.entities.Endereco;
import com.attornatus.gerenciamentopessoas.entities.Pessoa;
import com.attornatus.gerenciamentopessoas.exceptions.endereco.EnderecoNaoEncontradoException;
import com.attornatus.gerenciamentopessoas.exceptions.pessoa.PessoaNaoEncontradaException;
import com.attornatus.gerenciamentopessoas.repositories.EnderecoRepository;
import com.attornatus.gerenciamentopessoas.repositories.PessoaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@SpringBootTest
public class EnderecoServiceTests {

    @Autowired
    private EnderecoService enderecoService;
    @MockBean
    private EnderecoRepository enderecoRepository;
    @MockBean
    private PessoaRepository pessoaRepository;

    @Test
    @DisplayName("QUANDO salvar um novo endereco associada a uma pessoa existente DEVE salvar novo endereço")
    public void criarEnderecoComEnderecoValidoEPessoaExistente(){
        Pessoa pessoa = new Pessoa();

        pessoa.setId(1);
        pessoa.setNome("Michael");
        pessoa.setDataNascimento("28/09/2001");

        Endereco endereco = new Endereco();
        endereco.setCep("12345-190");
        endereco.setCidade("Fortaleza");
        endereco.setLogradouro("Rua 123");
        endereco.setNumero("00");

        when(pessoaRepository.findById(anyInt())).thenReturn(Optional.of(pessoa));

        enderecoService.salvar(endereco, 1);
        verify(enderecoRepository, times(1)).save(endereco);
    }

    @Test
    @DisplayName("QUANDO salvar um novo endereco associada a uma pessoa inexistente DEVE retornar exceção")
    public void criarEnderecoComEnderecoValidoEPessoaInexistente(){

        Endereco endereco = new Endereco();
        endereco.setCep("12345-190");
        endereco.setCidade("Fortaleza");
        endereco.setLogradouro("Rua 123");
        endereco.setNumero("00");

        when(pessoaRepository.findById(anyInt())).thenReturn(Optional.empty());

        Assertions.assertThrows(PessoaNaoEncontradaException.class,
                () -> enderecoService.salvar(endereco, 99)
        );
    }

    @Test
    @DisplayName("QUANDO buscar por endereços de uma pessoa existente DEVE retornar todos os endereços da pessoa")
    public void buscarEnderecosPessoaComPessoaExistente(){
        Pessoa pessoa = new Pessoa();
        pessoa.setId(1);

        Endereco endereco = new Endereco();
        endereco.setId(2);
        endereco.setCep("12345-190");
        endereco.setCidade("Fortaleza");
        endereco.setLogradouro("Rua 123");
        endereco.setNumero("00");

        pessoa.setEnderecos(List.of(endereco));
        endereco.setPessoa(pessoa);

       when(pessoaRepository.findById(anyInt())).thenReturn(Optional.of(pessoa));
       List<Endereco> enderecosPessoa = enderecoService.buscarEnderecosPessoaPorId(pessoa.getId());

       Assertions.assertEquals(enderecosPessoa.size(), 1);
       Assertions.assertEquals(enderecosPessoa.get(0).getId(), endereco.getId());
       Assertions.assertEquals(enderecosPessoa.get(0).getCep(), endereco.getCep());
       Assertions.assertEquals(enderecosPessoa.get(0).getCidade(), endereco.getCidade());
       Assertions.assertEquals(enderecosPessoa.get(0).getLogradouro(), endereco.getLogradouro());
       Assertions.assertEquals(enderecosPessoa.get(0).getNumero(), endereco.getNumero());
    }

    @Test
    @DisplayName("QUANDO buscar por endereços de uma pessoa inexistente DEVE retornar uma exceção")
    public void buscarEnderecosPessoaComPessoaInexistente(){

       when(pessoaRepository.findById(anyInt())).thenReturn(Optional.empty());

       Assertions.assertThrows(PessoaNaoEncontradaException.class,
               () -> enderecoService.buscarEnderecosPessoaPorId(99)
       );
    }

    @Test
    @DisplayName("QUANDO buscar por endereço principal de uma pessoa existente DEVE retornar endereço principal")
    public void buscarEnderecoPrincipalExistenteComPessoaExistente(){
        Pessoa pessoa = new Pessoa();
        pessoa.setId(1);

        Endereco endereco = new Endereco();
        endereco.setId(2);
        endereco.setCep("12345-190");
        endereco.setCidade("Fortaleza");
        endereco.setLogradouro("Rua 123");
        endereco.setNumero("00");
        endereco.setPrincipal(true);
        endereco.setPessoa(pessoa);

        when(pessoaRepository.existsById(anyInt())).thenReturn(true);
        when(enderecoRepository.buscarEnderecoPrincipalPorPessoa(anyInt())).thenReturn(Optional.of(endereco));
        Endereco enderecoPrincipal = enderecoService.buscarEnderecoPrincipalPessoaPorId(pessoa.getId());

        Assertions.assertEquals(enderecoPrincipal.getId(), endereco.getId());
        Assertions.assertEquals(enderecoPrincipal.getCep(), endereco.getCep());
        Assertions.assertEquals(enderecoPrincipal.getCidade(), endereco.getCidade());
        Assertions.assertEquals(enderecoPrincipal.getLogradouro(), endereco.getLogradouro());
        Assertions.assertEquals(enderecoPrincipal.getNumero(), endereco.getNumero());
        Assertions.assertEquals(enderecoPrincipal.getPrincipal(), endereco.getPrincipal());
        Assertions.assertEquals(enderecoPrincipal.getPessoa(), endereco.getPessoa());
    }

    @Test
    @DisplayName("QUANDO buscar por endereço principal existente de uma pessoa inexistente DEVE retornar exceção")
    public void buscarEnderecoPrincipalExistenteComPessoaInexistente(){

        when(pessoaRepository.findById(anyInt())).thenReturn(Optional.empty());

        Assertions.assertThrows(PessoaNaoEncontradaException.class,
                () -> enderecoService.buscarEnderecoPrincipalPessoaPorId(anyInt()));
    }

    @Test
    @DisplayName("QUANDO buscar por endereço principal não existente de uma pessoa inexistente DEVE retornar exceção")
    public void buscarEnderecoPrincipalInexistenteComPessoaExistente(){

        when(pessoaRepository.existsById(anyInt())).thenReturn(true);
        when(enderecoRepository.buscarEnderecoPrincipalPorPessoa(anyInt())).thenReturn(Optional.empty());

        Assertions.assertThrows(EnderecoNaoEncontradoException.class,
                () -> enderecoService.buscarEnderecoPrincipalPessoaPorId(anyInt()));
    }

    @Test
    @DisplayName("QUANDO salvar novo endereço principal de uma pessoa existente DEVE salvar endereço como principal")
    public void salvarNovoEnderecoPrincipalComPessoaExistente(){
        Pessoa pessoa = new Pessoa();
        pessoa.setId(1);

        Endereco novoEnderecoPrincipal = mock(Endereco.class);
        novoEnderecoPrincipal.setId(2);

        pessoa.setEnderecos(List.of(novoEnderecoPrincipal));
        novoEnderecoPrincipal.setPessoa(pessoa);

        when(pessoaRepository.existsById(anyInt())).thenReturn(true);
        when(enderecoRepository.findById(anyInt())).thenReturn(Optional.of(novoEnderecoPrincipal));
        when(enderecoRepository.buscarEnderecoPrincipalPorPessoa(anyInt())).thenReturn(Optional.empty());

        enderecoService.salvarEnderecoPrincipalPessoa(novoEnderecoPrincipal.getId(), pessoa.getId());

        verify(novoEnderecoPrincipal, times(1)).setPrincipal(true);
        verify(enderecoRepository, times(1)).save(novoEnderecoPrincipal);
    }

    @Test
    @DisplayName("QUANDO atualizar endereço principal de uma pessoa existente DEVE atualizar endereço principal")
    public void atualizarEnderecoPrincipalComPessoaExistente(){
        Pessoa pessoa = new Pessoa();
        pessoa.setId(1);

        Endereco novoEnderecoPrincipal = mock(Endereco.class);
        novoEnderecoPrincipal.setId(2);

        Endereco enderecoPrincipalAntigo = new Endereco();
        enderecoPrincipalAntigo.setId(3);
        enderecoPrincipalAntigo.setPrincipal(true);

        pessoa.setEnderecos(List.of(novoEnderecoPrincipal, enderecoPrincipalAntigo));
        novoEnderecoPrincipal.setPessoa(pessoa);

        when(pessoaRepository.existsById(anyInt())).thenReturn(true);
        when(enderecoRepository.findById(anyInt())).thenReturn(Optional.of(novoEnderecoPrincipal));
        when(enderecoRepository.buscarEnderecoPrincipalPorPessoa(anyInt())).thenReturn(Optional.of(enderecoPrincipalAntigo));

        enderecoService.salvarEnderecoPrincipalPessoa(novoEnderecoPrincipal.getId(), pessoa.getId());

        verify(novoEnderecoPrincipal, times(1)).setPrincipal(true);
        verify(enderecoRepository, times(1)).save(novoEnderecoPrincipal);

        verify(enderecoRepository, times(1)).removerEnderecoPrincipalAtualPessoa(pessoa.getId());
    }

    @Test
    @DisplayName("QUANDO salvar endereço principal de uma pessoa inexistente DEVE retornar exceção")
    public void atualizarEnderecoPrincipalComPessoaInexistente(){
        Endereco endereco = new Endereco();
        endereco.setId(2);

        when(pessoaRepository.findById(anyInt())).thenReturn(Optional.empty());
        when(enderecoRepository.findById(anyInt())).thenReturn(Optional.of(endereco));
        when(enderecoRepository.buscarEnderecoPrincipalPorPessoa(anyInt())).thenReturn(Optional.empty());

        Assertions.assertThrows(PessoaNaoEncontradaException.class,
                () -> enderecoService.salvarEnderecoPrincipalPessoa(endereco.getId(), anyInt())
        );
    }

    @Test
    @DisplayName("QUANDO salvar endereço principal inexistente de uma pessoa existente DEVE retornar exceção")
    public void atualizarEnderecoPrincipalInexistenteComPessoaExistente(){
        Pessoa pessoa = new Pessoa();
        pessoa.setId(1);

        when(pessoaRepository.findById(anyInt())).thenReturn(Optional.of(pessoa));
        when(enderecoRepository.findById(anyInt())).thenReturn(Optional.empty());
        when(enderecoRepository.buscarEnderecoPrincipalPorPessoa(anyInt())).thenReturn(Optional.empty());

        Assertions.assertThrows(PessoaNaoEncontradaException.class,
                () -> enderecoService.salvarEnderecoPrincipalPessoa(anyInt(), pessoa.getId())
        );
    }

    @Test
    @DisplayName("QUANDO buscar por endereço existente por ip DEVE retornar endereço")
    public void buscarEnderecoExistentePorId(){
        Pessoa pessoa = new Pessoa();
        pessoa.setId(1);

        Endereco endereco = new Endereco();
        endereco.setId(2);
        endereco.setCep("12345-190");
        endereco.setCidade("Fortaleza");
        endereco.setLogradouro("Rua 123");
        endereco.setNumero("00");
        endereco.setPessoa(pessoa);

        when(enderecoRepository.findById(anyInt())).thenReturn(Optional.of(endereco));
        Endereco enderecoEncontrado = enderecoService.buscarPorId(endereco.getId());

        Assertions.assertEquals(enderecoEncontrado.getId(), endereco.getId());
        Assertions.assertEquals(enderecoEncontrado.getCep(), endereco.getCep());
        Assertions.assertEquals(enderecoEncontrado.getCidade(), endereco.getCidade());
        Assertions.assertEquals(enderecoEncontrado.getLogradouro(), endereco.getLogradouro());
        Assertions.assertEquals(enderecoEncontrado.getNumero(), endereco.getNumero());
        Assertions.assertEquals(enderecoEncontrado.getPessoa(), endereco.getPessoa());
    }

    @Test
    @DisplayName("QUANDO buscar por endereço inexistente por ip DEVE retornar exceção")
    public void buscarEnderecoInexistentePorId(){
        when(enderecoRepository.findById(anyInt())).thenReturn(Optional.empty());

        Assertions.assertThrows(EnderecoNaoEncontradoException.class,
                () -> enderecoService.buscarPorId(anyInt())
        );
    }
}
