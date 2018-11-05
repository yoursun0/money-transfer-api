package com.helic.moneytransfer.db.repo;

import com.helic.moneytransfer.db.entity.Account;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AccountRepository extends MongoRepository<Account, String> {
}
