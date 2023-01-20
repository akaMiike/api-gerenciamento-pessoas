# Gerenciamento de Pessoas

Uma API de gerenciamento de pessoas, feita para o desafio técnico do processo seletivo da Attornatus.

# Tecnologias Usadas

- Java 17
- Spring Boot 3.0.1
- Maven 3.8.6
- JUnit 5.9.1
- H2 Database

# Como executar

É necessário ter o Java 17 instalado, assim como o Maven na versão 3.8+.
Para executar, clone o projeto e execute

`./mvnw spring-boot:run`

Para executar os testes, execute:

`./mvnw test`

# Funcionalidades

Assim como descrito nos requisitos, a API possui as seguintes funcionalidades:

<b>Criar uma pessoa</b>

Para registrar um novo usuário, é necessário enviar o nome e data de nascimento (formato dia/mes/ano) via POST para `/pessoas`.
```
{
  "nome": "Exemplo",
  "dataNascimento":"28/10/1999"
}
```
- O servidor retorna 201 CREATED caso seja criado com sucesso.
- Se o nome ou data de nascimento não forem enviados, ou se a data de nascimento estiver no formato errado, será retornado um erro 400 BAD REQUEST.

<b>Editar os dados de uma pessoa</b>

Para editar os dados de uma pessoa, é necessário enviar o nome ou a data de nascimento (formato dia/mes/ano) via PUT para `/pessoas/{idPessoa}`.
```
{
  "nome":"Nome atualizado",
  "dataNascimento:"28/11/1998"
}
```
- É obrigatório enviar pelo menos um dos campos, caso contrário, será retornado um erro 400 BAD REQUEST
- Caso a data seja enviada, ela deve estar no formato correto, caso contrário, será retornado um erro 400 BAD REQUEST também.
- Se o id for de uma pessoa não existente, será retornado um erro 404 NOT FOUND.

<b>Consultar uma pessoa</b>

Para consultar uma pessoa cadastrada, é necessário somente enviar o id da pessoa via GET para `/pessoas/{idPessoa}`.
- A API retornará os dados do nome e data de nascimento com status 200 OK caso a pessoa exista
- Caso a pessoa não exista, será retorando um erro com status 404 NOT FOUND

<b>Listar pessoas</b>

Para listar todas as pessoas cadastradas, é necessário somente fazer a requisição via GET para `/pessoas`.
- A API retornará uma lista com todos as pessoas cadastradas, com seus respectivos nomes e datas de nascimento.

<b>Criar endereço para pessoa</b>

Para criar um novo endereço para uma pessoa cadastrada, o usuário deve enviar o CEP, Logradouro, Numero e Cidade via POST para `/pessoas/{idPessoa}/endereco`.
```
{
	"logradouro":"Laranjeiras",
	"numero":"123",
	"cidade":"Fortaleza",
	"cep":"12345"
}
```
- A API retornará o status 201 CREATED caso a criação seja feita com sucesso.
- Todos os campos são obrigatórios e não podem ser vazios, caso falte algum, será retornado um erro 400 BAD REQUEST.

<b>Listar endereços da pessoa</b>

Para listar todos os endereços de uma pessoa, é necessário somente fazer a requisição via GET para `/pessoas/{idPessoa}/endereco`
- A API retornará uma lista de todos os endereços cadastrados, com suas respectivas informações de ID, Cep, Logradouro, Numero, Cidade e um booleano Principal que indica se aquele é o endereço principal da pessoa ou não.
- Se o id for de uma pessoa não existente, será retornado um erro 404 NOT FOUND.

<b>Informar qual o endereço principal da pessoa</b>

Para informar o endereço principal cadastrado de uma pessoa, basta fazer uma requisição via PUT para `/pessoas/{idPessoa}/endereco/{idEndereco}`.
- A API definirá aquele endereço como o principal, retornando status 204 NO CONTENT. Caso já exista outro endereço principal definido, será definido como não principal.
- Se o id do endereço ou da pessoa não existirem, será retornado um erro 404 NOT FOUND.
