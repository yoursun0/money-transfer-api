package com.helic.moneytransfer.db.repo;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.helic.moneytransfer.db.entity.Transaction;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {
}
