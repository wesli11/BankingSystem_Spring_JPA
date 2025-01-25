package com.bank.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.bank.model.Account;
import com.bank.model.Client;
import com.bank.model.Movements;

public interface ClientRepository {
    
   // Consulta el saldo de una cuenta específica
    Optional<Double> findBalanceByAccount(int idAccount);

    // Realiza un ingreso en una cuenta
    boolean deposit(double quantity, int idAccount);

    // Obtiene los datos de una cuenta por su ID
    Account findAccountById(int idAccount);

    // Realiza un retiro de una cuenta
    boolean withdraw(double quantity, int idAccount);

    void updateClient(Client client);
    //ESTA OPERACION ES DEL SERVICIO
    // Realiza una transferencia entre cuentas
  //  boolean transfer(int idAccountStart, int idAccountEnd, double quantity);

    // Consulta los movimientos de una cuenta en un rango de fechas
    List<Movements> findMovementsInDateRange(int idAccount, LocalDateTime from, LocalDateTime to);

   
}
