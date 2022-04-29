package br.com.pismo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.pismo.model.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    
    
}
