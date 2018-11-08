package com.helic.moneytransfer.db.repo;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.helic.moneytransfer.db.entity.Account;

public interface AccountRepository extends CrudRepository<Account, Long>, JpaSpecificationExecutor<Account> {
}
