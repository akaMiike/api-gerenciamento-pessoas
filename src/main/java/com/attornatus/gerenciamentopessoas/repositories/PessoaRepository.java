package com.attornatus.gerenciamentopessoas.repositories;

import com.attornatus.gerenciamentopessoas.entities.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa,Integer> {
}
