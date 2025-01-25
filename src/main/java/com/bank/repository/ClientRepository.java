package com.bank.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.bank.model.Account;
import com.bank.model.Client;
import com.bank.model.Movements;

public interface ClientRepository {
    
    Optional<Double> findBalanceByAccount(int idAccount);

    boolean deposit(double quantity, int idAccount);

    Account findAccountById(int idAccount);

    boolean withdraw(double quantity, int idAccount);

    void updateClient(Client client);
   
    List<Movements> findMovementsInDateRange(int idAccount, LocalDateTime from, LocalDateTime to);

   
}
