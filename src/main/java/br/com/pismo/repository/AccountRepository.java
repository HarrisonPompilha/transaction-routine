package br.com.pismo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.pismo.model.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    public Account findByDocument(String document);
    
}
