package br.com.pismo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.pismo.model.AccountCreditLimit;

public interface AccountCreditLimitRepository extends JpaRepository<AccountCreditLimit, Long> {

}
