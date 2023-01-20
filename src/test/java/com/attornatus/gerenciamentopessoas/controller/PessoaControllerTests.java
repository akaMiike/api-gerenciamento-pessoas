package com.attornatus.gerenciamentopessoas.controller;

import com.attornatus.gerenciamentopessoas.dto.endereco.EnderecoCreateDTO;
import com.attornatus.gerenciamentopessoas.dto.pessoa.PessoaCreateDTO;
import com.attornatus.gerenciamentopessoas.dto.pessoa.PessoaUpdateDTO;
import com.attornatus.gerenciamentopessoas.entities.Endereco;
import com.attornatus.gerenciamentopessoas.entities.Pessoa;
import com.attornatus.gerenciamentopessoas.exceptions.ParametrosInvalidosException;
import com.attornatus.gerenciamentopessoas.exceptions.endereco.EnderecoNaoEncontradoException;
import com.attornatus.gerenciamentopessoas.exceptions.pessoa.PessoaNaoEncontradaException;
import com.attornatus.gerenciamentopessoas.services.EnderecoService;
import com.attornatus.gerenciamentopessoas.services.PessoaService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class PessoaControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PessoaService pessoaService;

    @MockBean
    private EnderecoService enderecoService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private String toJson(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }

    @DisplayName("QUANDO obter todos as pessoas DEVE retornar todas as pessoas cadastradas")
    @Test
    public void obterTodasPessoasCadastradas() throws Exception {
        Pessoa pessoa = new Pessoa();
        pessoa.setId(1);
        pessoa.setDataNascimento("28/09/2001");
        pessoa.setNome("Michael");

        when(pessoaService.buscarTodas()).thenReturn(List.of(pessoa));

        mockMvc.perform(get("/pessoas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nome").value("Michael"))
                .andExpect(jsonPath("$[0].dataNascimento").value("28/09/2001"));

    }

    @DisplayName("QUANDO buscar por pessoa cadastrada por id DEVE retornar dados da pessoa cadastrada")
    @Test
    public void buscarPessoaCadastradaPorId() throws Exception {
        Pessoa pessoa = new Pessoa();
        pessoa.setId(1);
        pessoa.setDataNascimento("28/09/2001");
        pessoa.setNome("Michael");

        when(pessoaService.buscarPorId(anyInt())).thenReturn(pessoa);

        mockMvc.perform(get("/pessoas/" + pessoa.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Michael"))
                .andExpect(jsonPath("$.dataNascimento").value("28/09/2001"));

    }

    @DisplayName("QUANDO buscar por pessoa não cadastrada por id DEVE retornar erro com status 404 NOT FOUND")
    @Test
    public void buscarPessoaNaoCadastradaPorId() throws Exception {
        when(pessoaService.buscarPorId(anyInt())).thenThrow(new PessoaNaoEncontradaException());

        String URL = "/pessoas/" + anyInt();

        mockMvc.perform(get(URL))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Pessoa não encontrada."))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.instance").value(URL))
                .andExpect(jsonPath("$.title").value("Not Found"));
    }

    @DisplayName("QUANDO atualizar pessoa cadastrada com dados corretos DEVE retornar status 204 NO CONTENT")
    @Test
    public void atualizarPessoaCadastradaComDadosCorretos() throws Exception {
        Pessoa pessoa = new Pessoa();
        pessoa.setId(1);

        doNothing().when(pessoaService).atualizar(any(Pessoa.class), anyInt());

        PessoaUpdateDTO pessoaAtualizada = new PessoaUpdateDTO();
        pessoaAtualizada.setDataNascimento("30/01/1999");
        pessoaAtualizada.setNome("Joao");

        mockMvc.perform(
                put("/pessoas/" + pessoa.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(pessoaAtualizada))
                )
                .andExpect(status().isNoContent());
    }

    @DisplayName("QUANDO atualizar pessoa cadastrada com nome vazio e data correta DEVE retornar status 204 NO CONTENT")
    @Test
    public void atualizarPessoaCadastradaComNomeVazioEDataCorreta() throws Exception {
        Pessoa pessoa = new Pessoa();
        pessoa.setId(1);

        doNothing().when(pessoaService).atualizar(any(Pessoa.class), anyInt());

        PessoaUpdateDTO pessoaAtualizada = new PessoaUpdateDTO();
        pessoaAtualizada.setDataNascimento("30/01/1999");

        mockMvc.perform(
                put("/pessoas/" + pessoa.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(pessoaAtualizada))
                )
                .andExpect(status().isNoContent());
    }

    @DisplayName("QUANDO atualizar pessoa cadastrada com nome correta e data vazia DEVE retornar status 204 NO CONTENT")
    @Test
    public void atualizarPessoaCadastradaComDataVaziaENomeCorreto() throws Exception {
        Pessoa pessoa = new Pessoa();
        pessoa.setId(1);

        doNothing().when(pessoaService).atualizar(any(Pessoa.class), anyInt());

        PessoaUpdateDTO pessoaAtualizada = new PessoaUpdateDTO();
        pessoaAtualizada.setNome("Joao");

        mockMvc.perform(
                put("/pessoas/" + pessoa.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(pessoaAtualizada))
                )
                .andExpect(status().isNoContent());
    }

    @DisplayName("QUANDO atualizar pessoa cadastrada com dados vazios DEVE retornar status 400 BAD REQUEST")
    @Test
    public void atualizarPessoaCadastradaComDadosVazios() throws Exception {
        Pessoa pessoa = new Pessoa();
        pessoa.setId(1);

        doThrow(new ParametrosInvalidosException("Dados da pessoa devem conter ao menos um campo não vazio."))
                .when(pessoaService).atualizar(any(Pessoa.class), anyInt());

        mockMvc.perform(
                put("/pessoas/" + pessoa.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("Dados da pessoa devem conter ao menos um campo não vazio."))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.instance").value("/pessoas/" + pessoa.getId()))
                .andExpect(jsonPath("$.title").value("Bad Request"));

    }

    @DisplayName("QUANDO atualizar pessoa não cadastrada DEVE retornar status 404 NOT FOUND")
    @Test
    public void atualizarPessoNaoCadastrada() throws Exception {
        Pessoa pessoa = new Pessoa();
        pessoa.setId(1);

        Mockito.doThrow(new PessoaNaoEncontradaException())
                .when(pessoaService).atualizar(any(Pessoa.class), anyInt());


        mockMvc.perform(
                put("/pessoas/" + pessoa.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Pessoa não encontrada."))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.instance").value("/pessoas/" + pessoa.getId()))
                .andExpect(jsonPath("$.title").value("Not Found"));
    }

    @DisplayName("QUANDO cadastrar nova pessoa com dados corretos DEVE retornar status 201 CREATED")
    @Test
    public void criarNovaPessoaComDadosValidos() throws Exception {
        doNothing().when(pessoaService).criar(any(Pessoa.class));

        PessoaCreateDTO novaPessoa = new PessoaCreateDTO();
        novaPessoa.setNome("Michael");
        novaPessoa.setDataNascimento("25/05/1999");

        mockMvc.perform(post("/pessoas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(novaPessoa))
        )
                .andExpect(status().isCreated());

    }

    @DisplayName("QUANDO cadastrar nova pessoa sem o campo nome DEVE retornar status 400 BAD REQUEST")
    @Test
    public void criarNovaPessoaSemNome() throws Exception {
        doNothing().when(pessoaService).criar(any(Pessoa.class));

        PessoaCreateDTO novaPessoa = new PessoaCreateDTO();
        novaPessoa.setDataNascimento("25/05/1999");

        mockMvc.perform(post("/pessoas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(novaPessoa))
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("nome: 'Nome não pode ser vazio'"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.instance").value("/pessoas"))
                .andExpect(jsonPath("$.title").value("Bad Request"));

    }

    @DisplayName("QUANDO cadastrar nova pessoa sem o campo dataNascimento DEVE retornar status 400 BAD REQUEST")
    @Test
    public void criarNovaPessoaSemDataNascimento() throws Exception {
        doNothing().when(pessoaService).criar(any(Pessoa.class));

        PessoaCreateDTO novaPessoa = new PessoaCreateDTO();
        novaPessoa.setNome("Joao");

        mockMvc.perform(post("/pessoas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(novaPessoa))
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("dataNascimento: 'Data de nascimento não pode ser vazia'"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.instance").value("/pessoas"))
                .andExpect(jsonPath("$.title").value("Bad Request"));

    }

    @DisplayName("QUANDO cadastrar nova pessoa com a data de nascimento no formato errado DEVE retornar status 400 BAD REQUEST")
    @Test
    public void criarNovaPessoaComDataNascimentoFormatoErrado() throws Exception {
        doNothing().when(pessoaService).criar(any(Pessoa.class));

        PessoaCreateDTO novaPessoa = new PessoaCreateDTO();
        novaPessoa.setNome("Joao");
        novaPessoa.setDataNascimento("10-08-2022");

        mockMvc.perform(post("/pessoas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(novaPessoa))
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("dataNascimento: 'Formato da data de nascimento inválida'"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.instance").value("/pessoas"))
                .andExpect(jsonPath("$.title").value("Bad Request"));

    }

    @DisplayName("QUANDO listar endereços de uma pessoa existente DEVE retornar endereços da pessoa")
    @Test
    public void buscarTodosEnderecosPessoaCadastrada() throws Exception {
        Pessoa pessoa = new Pessoa();
        pessoa.setId(1);

        Endereco endereco = new Endereco();
        endereco.setId(2);
        endereco.setCep("12345-190");
        endereco.setCidade("Fortaleza");
        endereco.setLogradouro("Rua 123");
        endereco.setNumero("00");
        endereco.setPessoa(pessoa);

        when(enderecoService.buscarEnderecosPessoaPorId(anyInt())).thenReturn(List.of(endereco));

        mockMvc.perform(get("/pessoas/" + pessoa.getId() + "/endereco"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(2))
                .andExpect(jsonPath("$[0].cep").value("12345-190"))
                .andExpect(jsonPath("$[0].logradouro").value("Rua 123"))
                .andExpect(jsonPath("$[0].cidade").value("Fortaleza"))
                .andExpect(jsonPath("$[0].numero").value("00"))
                .andExpect(jsonPath("$[0].principal").value(false));

    }

    @DisplayName("QUANDO listar endereços de uma pessoa não existente DEVE retornar status 404 NOT FOUND")
    @Test
    public void buscarTodosEnderecosPessoaNaoCadastrada() throws Exception {

        when(enderecoService.buscarEnderecosPessoaPorId(anyInt())).thenThrow(new PessoaNaoEncontradaException());

        mockMvc.perform(get("/pessoas/" + 1 + "/endereco"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Pessoa não encontrada."))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.instance").value("/pessoas/" + 1 + "/endereco"))
                .andExpect(jsonPath("$.title").value("Not Found"));

    }

    @DisplayName("QUANDO criar um novo endereço de uma pessoa cadastrada com dados corretos DEVE retornar 201 CREATED")
    @Test
    public void criarEnderecoPessoaCadastradaComDadosCorretos() throws Exception{
        Pessoa pessoa = new Pessoa();
        pessoa.setId(1);

        Endereco endereco = new Endereco();
        endereco.setId(2);
        endereco.setCep("12345-190");
        endereco.setCidade("Fortaleza");
        endereco.setLogradouro("Rua 123");
        endereco.setNumero("00");
        endereco.setPessoa(pessoa);

        doNothing().when(enderecoService).salvar(any(Endereco.class), anyInt());

        mockMvc.perform(post("/pessoas/" + pessoa.getId() + "/endereco")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(endereco))
        )
                .andExpect(status().isCreated());
    }

    @DisplayName("QUANDO criar um novo endereço de uma pessoa cadastrada com dados corretos DEVE retornar 201 CREATED")
    @Test
    public void criarEnderecoPessoaNaoCadastrada() throws Exception{
        Pessoa pessoa = new Pessoa();
        pessoa.setId(1);

        EnderecoCreateDTO endereco = new EnderecoCreateDTO();
        endereco.setCep("12345-190");
        endereco.setCidade("Fortaleza");
        endereco.setLogradouro("Rua 123");
        endereco.setNumero("00");

        doThrow(new PessoaNaoEncontradaException()).when(enderecoService).salvar(any(Endereco.class), anyInt());

        mockMvc.perform(post("/pessoas/" + pessoa.getId() + "/endereco")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(endereco))
        )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Pessoa não encontrada."))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.instance").value("/pessoas/" + pessoa.getId() + "/endereco"))
                .andExpect(jsonPath("$.title").value("Not Found"));
    }

    @DisplayName("QUANDO criar um novo endereço de uma pessoa cadastrada com Cep vazio DEVE retornar 400 BAD REQUEST")
    @Test
    public void criarEnderecoPessoaCadastradaComCepVazio() throws Exception{
        Pessoa pessoa = new Pessoa();
        pessoa.setId(1);

        EnderecoCreateDTO endereco = new EnderecoCreateDTO();
        endereco.setCidade("Fortaleza");
        endereco.setLogradouro("Rua 123");
        endereco.setNumero("00");

        doNothing().when(enderecoService).salvar(any(Endereco.class), anyInt());

        mockMvc.perform(post("/pessoas/" + pessoa.getId() + "/endereco")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(endereco))
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("cep: 'CEP não pode ser vazio.'"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.instance").value("/pessoas/" + pessoa.getId() + "/endereco"))
                .andExpect(jsonPath("$.title").value("Bad Request"));
    }

    @DisplayName("QUANDO criar um novo endereço de uma pessoa cadastrada com cidade vazia DEVE retornar 400 BAD REQUEST")
    @Test
    public void criarEnderecoPessoaCadastradaComCidadeVazia() throws Exception{
        Pessoa pessoa = new Pessoa();
        pessoa.setId(1);

        EnderecoCreateDTO endereco = new EnderecoCreateDTO();
        endereco.setCep("12345-190");
        endereco.setLogradouro("Rua 123");
        endereco.setNumero("00");

        doNothing().when(enderecoService).salvar(any(Endereco.class), anyInt());

        mockMvc.perform(post("/pessoas/" + pessoa.getId() + "/endereco")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(endereco))
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("cidade: 'Cidade não pode ser vazia.'"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.instance").value("/pessoas/" + pessoa.getId() + "/endereco"))
                .andExpect(jsonPath("$.title").value("Bad Request"));
    }

    @DisplayName("QUANDO criar um novo endereço de uma pessoa cadastrada com logradouro vazio DEVE retornar 400 BAD REQUEST")
    @Test
    public void criarEnderecoPessoaCadastradaComLogradouroVazio() throws Exception{
        Pessoa pessoa = new Pessoa();
        pessoa.setId(1);

        EnderecoCreateDTO endereco = new EnderecoCreateDTO();
        endereco.setCep("12345-190");
        endereco.setCidade("Fortaleza");
        endereco.setNumero("00");

        doNothing().when(enderecoService).salvar(any(Endereco.class), anyInt());

        mockMvc.perform(post("/pessoas/" + pessoa.getId() + "/endereco")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(endereco))
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("logradouro: 'Logradouro não pode ser vazio.'"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.instance").value("/pessoas/" + pessoa.getId() + "/endereco"))
                .andExpect(jsonPath("$.title").value("Bad Request"));
    }

    @DisplayName("QUANDO criar um novo endereço de uma pessoa cadastrada com logradouro vazio DEVE retornar 400 BAD REQUEST")
    @Test
    public void criarEnderecoPessoaCadastradaComNumeroVazio() throws Exception{
        Pessoa pessoa = new Pessoa();
        pessoa.setId(1);

        EnderecoCreateDTO endereco = new EnderecoCreateDTO();
        endereco.setCep("12345-190");
        endereco.setCidade("Fortaleza");
        endereco.setLogradouro("Rua 123");

        doNothing().when(enderecoService).salvar(any(Endereco.class), anyInt());

        mockMvc.perform(post("/pessoas/" + pessoa.getId() + "/endereco")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(endereco))
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("numero: 'Número não pode ser vazio.'"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.instance").value("/pessoas/" + pessoa.getId() + "/endereco"))
                .andExpect(jsonPath("$.title").value("Bad Request"));
    }

    @DisplayName("QUANDO atualizar endereço existente como principal de uma pessoa DEVE retornar 204 NO CONTENT ")
    @Test
    public void atualizarEnderecoPrincipalPessoaCadastrada() throws Exception {
        Pessoa pessoa = new Pessoa();
        pessoa.setId(1);

        Endereco endereco = new Endereco();
        endereco.setId(2);

        doNothing().when(enderecoService).salvarEnderecoPrincipalPessoa(anyInt(), anyInt());

        mockMvc.perform(put("/pessoas/" + pessoa.getId() + "/endereco/" + endereco.getId()))
                .andExpect(status().isNoContent());

    }

    @DisplayName("QUANDO atualizar endereço nao existente como principal de uma pessoa DEVE retornar 404 NOT FOUND")
    @Test
    public void atualizarEnderecoPrincipalNaoExistentePessoaCadastrada() throws Exception {
        Pessoa pessoa = new Pessoa();
        pessoa.setId(1);

        Endereco endereco = new Endereco();
        endereco.setId(2);

        doThrow(new EnderecoNaoEncontradoException()).when(enderecoService).salvarEnderecoPrincipalPessoa(anyInt(), anyInt());


        mockMvc.perform(put("/pessoas/" + pessoa.getId() + "/endereco/" + endereco.getId()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Endereço não encontrado."))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.instance").value("/pessoas/" + pessoa.getId() + "/endereco/" + endereco.getId()))
                .andExpect(jsonPath("$.title").value("Not Found"));
    }

    @DisplayName("QUANDO atualizar endereço nao existente como principal de uma pessoa nao cadastrada DEVE retornar 404 NOT FOUND")
    @Test
    public void atualizarEnderecoPrincipalExistentePessoaNaoCadastrada() throws Exception {
        Pessoa pessoa = new Pessoa();
        pessoa.setId(1);

        Endereco endereco = new Endereco();
        endereco.setId(2);

        doThrow(new PessoaNaoEncontradaException()).when(enderecoService).salvarEnderecoPrincipalPessoa(anyInt(), anyInt());

        mockMvc.perform(put("/pessoas/" + pessoa.getId() + "/endereco/" + endereco.getId()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Pessoa não encontrada."))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.instance").value("/pessoas/" + pessoa.getId() + "/endereco/" + endereco.getId()))
                .andExpect(jsonPath("$.title").value("Not Found"));
    }
}