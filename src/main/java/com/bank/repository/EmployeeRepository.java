package com.bank.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.bank.model.Account;
import com.bank.model.Client;
import com.bank.model.Movements;
import com.bank.model.Transfer;

public interface EmployeeRepository {
	void saveClient(Client client);
	void saveAccount(Account account);
    void saveMovement(Movements movement);
    void saveTransfer(Transfer transfer);
	Client findClientWithAccounts(int idCliente);
	Client findByClientById(int idCliente);
	
	Transfer trasnferById(int idTransfer);

	Optional<Account> findByAccountById(int idAccount);
        double calculateTotalBalance(int idCliente);

    List<Movements> findMovementsByAccount(int idCuenta);

    // Consulta las cuentas con saldo mayor a un monto mínimo
    List<Account> findAccountsByMinimumBalance(double saldoMinimo);

    List<Movements> findAllMovementsByClient(int idCliente);

    List<Movements> findMovementsByClientInDateRange(int idCliente, LocalDateTime desde, LocalDateTime hasta);

    List<Client> findClientsWithNegativeBalances();

    Map<String, Double> calculateTotalBalanceByAccountType();

    List<Client> findClientsWithoutMovementsSince(LocalDateTime fecha);

    List<Double> findBalanceHistoryByAccount(int idCuenta, LocalDate desde, LocalDate hasta);

    Map<String, Integer> generateMovementReportByType(int idCuenta, YearMonth mes);

    List<Client> findTopClientsByAverageBalance(int anio);

    boolean blockAccount(int idCuenta);

    boolean unblockAccount(int idCuenta);
}
