package com.example.application.repository;

import com.example.application.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("select a from Account a where a.name = :name")
    Account findByName(@Param("name") String name);
}
