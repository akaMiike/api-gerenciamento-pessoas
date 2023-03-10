package com.attornatus.gerenciamentopessoas.repositories;

import com.attornatus.gerenciamentopessoas.entities.Endereco;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Integer> {

    @Query("SELECT e FROM Pessoa p JOIN Endereco e WHERE e.principal = true AND p.id = :idPessoa")
    Optional<Endereco> buscarEnderecoPrincipalPorPessoa(@Param("idPessoa") Integer idPessoa);

    @Transactional
    @Modifying
	@Query("update Endereco e set e.principal = FALSE where e.pessoa.id = :idPessoa")
    void removerEnderecoPrincipalAtualPessoa(@Param("idPessoa") Integer idPessoa);
}
