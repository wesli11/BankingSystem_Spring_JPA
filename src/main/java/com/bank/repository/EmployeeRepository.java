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
	 // Guarda un nuevo movimiento en la base de datos
    void saveMovement(Movements movement);
    void saveTransfer(Transfer transfer);
     // Cliente y ademas te trae sus cuentas
	Client findClientWithAccounts(int idCliente);
      //verificar Cliente
	Client findByClientById(int idCliente);
	
	Transfer trasnferById(int idTransfer);

	Optional<Account> findByAccountById(int idAccount);
	
    // Calcula el saldo total de un cliente (sumando todas sus cuentas)
    double calculateTotalBalance(int idCliente);

    // Consulta los movimientos de una cuenta específica
    List<Movements> findMovementsByAccount(int idCuenta);

    // Consulta las cuentas con saldo mayor a un monto mínimo
    List<Account> findAccountsByMinimumBalance(double saldoMinimo);

    // Obtiene todos los movimientos de un cliente
    List<Movements> findAllMovementsByClient(int idCliente);

    // Obtiene movimientos de un cliente en un rango de fechas
    List<Movements> findMovementsByClientInDateRange(int idCliente, LocalDateTime desde, LocalDateTime hasta);

    // Busca clientes con saldo negativo en alguna cuenta
    List<Client> findClientsWithNegativeBalances();

    // Obtiene el total de saldos agrupados por tipo de cuenta
    Map<String, Double> calculateTotalBalanceByAccountType();

    // Busca clientes sin movimientos desde una fecha específica
    List<Client> findClientsWithoutMovementsSince(LocalDateTime fecha);

    // Obtiene el historial de saldos de una cuenta en un rango de fechas
    List<Double> findBalanceHistoryByAccount(int idCuenta, LocalDate desde, LocalDate hasta);

    // Genera un informe de movimientos agrupados por tipo en un mes específico
    Map<String, Integer> generateMovementReportByType(int idCuenta, YearMonth mes);

    // Encuentra clientes con el saldo promedio más alto durante un año
    List<Client> findTopClientsByAverageBalance(int anio);

    // Bloquea una cuenta específica
    boolean blockAccount(int idCuenta);

    // Desbloquea una cuenta específica
    boolean unblockAccount(int idCuenta);
}
