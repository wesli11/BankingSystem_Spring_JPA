package com.bank.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

import com.bank.model.Account;
import com.bank.model.Client;
import com.bank.model.Movements;
import com.bank.model.Transfer;

public interface EmployeeService {

	void saveClientService(Client client);
	Client clientByIdService(int idClient);
	
	void saveAccountService(Account account);
	 // Guarda un nuevo movimiento en la base de datos
    void saveMovementService(Movements movement);
    
    boolean registrarCuenta(int idCliente, int idCuenta);//si

     // Cliente y ademas te trae sus cuentas
	Client findClientWithAccountsService(int idCliente);
      //verificar Cliente
	
	// Client findByClientByIdService(int idCliente); ya lo tengo en repository
	
    // Calcula el saldo total de un cliente (sumando todas sus cuentas)
    double calculateTotalBalanceService(int idCliente);

    // Consulta los movimientos de una cuenta específica
    List<Movements> findMovementsByAccountService(int idCuenta);

    // Consulta las cuentas con saldo mayor a un monto mínimo
    List<Account> findAccountsByMinimumBalanceService(double saldoMinimo);

    // Obtiene todos los movimientos de un cliente
    List<Movements> findAllMovementsByClientService(int idCliente);

    // Obtiene movimientos de un cliente en un rango de fechas
    List<Movements> findMovementsByClientInDateRangeService(int idCliente, LocalDateTime desde, LocalDateTime hasta);

    // Busca clientes con saldo negativo en alguna cuenta
    List<Client> findClientsWithNegativeBalancesService();

    // Obtiene el total de saldos agrupados por tipo de cuenta
    Map<String, Double> calculateTotalBalanceByAccountTypeService();

    // Busca clientes sin movimientos desde una fecha específica
    List<Client> findClientsWithoutMovementsSinceService(LocalDateTime fecha);

    // Obtiene el historial de saldos de una cuenta en un rango de fechas
    List<Double> findBalanceHistoryByAccountService(int idCuenta, LocalDate desde, LocalDate hasta);

    // Genera un informe de movimientos agrupados por tipo en un mes específico
    Map<String, Integer> generateMovementReportByTypeService(int idCuenta, YearMonth mes);

    // Encuentra clientes con el saldo promedio más alto durante un año
    List<Client> findTopClientsByAverageBalanceService(int anio);

    // Bloquea una cuenta específica
    boolean blockAccountService(int idCuenta);

    // Desbloquea una cuenta específica
    boolean unblockAccountService(int idCuenta);
    
  //14.- anular transferencia
    public boolean anularTransferencia(int idTransferencia, LocalDate fechaActual);
}
