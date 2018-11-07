package com.helic.moneytransfer.db.repo;

import com.helic.moneytransfer.db.entity.Account;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<Account, Long>, JpaSpecificationExecutor<Account> {
}
