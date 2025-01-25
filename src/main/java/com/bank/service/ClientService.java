package com.bank.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.bank.model.Account;
import com.bank.model.Client;
import com.bank.model.Movements;

public interface ClientService {
	public Account validarCuenta(int idCuenta);

	public void updateClientService(Client client);
	// Consulta el saldo de una cuenta específica
    Optional<Double> findBalanceByAccountService(int idAccount);

    // Realiza un ingreso en una cuenta
    boolean depositService(double quantity, int idAccount);

    // Obtiene los datos de una cuenta por su ID
    Account findAccountByIdService(int idAccount);

    // Realiza un retiro de una cuenta
    boolean withdrawService(double quantity, int idAccount);

    //ESTA OPERACION ES DEL SERVICIO
    // Realiza una transferencia entre cuentas
  //  boolean transfer(int idAccountStart, int idAccountEnd, double quantity);

    // Consulta los movimientos de una cuenta en un rango de fechas
    List<Movements> findMovementsInDateRangeService(int idAccount, LocalDateTime from, LocalDateTime to);

     boolean transfer(int idAccountStart, int idAccountEnd, double quantity);

	
	
	
	
	/*@Service
	public class TransferService {

	    @Autowired
	    private AccountRepository accountRepository;

	    @Autowired
	    private MovementRepository movementRepository;

	    @Autowired
	    private TransferRepository transferRepository;

	    public boolean transfer(int idAccountStart, int idAccountEnd, double quantity) {
	        // 1. Verificar que las cuentas existen
	        Account startAccount = accountRepository.findById(idAccountStart).orElseThrow(() -> new RuntimeException("Cuenta de origen no encontrada"));
	        Account endAccount = accountRepository.findById(idAccountEnd).orElseThrow(() -> new RuntimeException("Cuenta de destino no encontrada"));

	        // 2. Verificar saldo suficiente en la cuenta de origen
	        if (startAccount.getBalance() < quantity) {
	            throw new RuntimeException("Saldo insuficiente");
	        }

	        // 3. Actualizar los saldos de ambas cuentas
	        startAccount.setBalance(startAccount.getBalance() - quantity);
	        endAccount.setBalance(endAccount.getBalance() + quantity);
	        accountRepository.save(startAccount);
	        accountRepository.save(endAccount);

	        // 4. Crear los movimientos
	        Movement movementStart = new Movement(startAccount, quantity, "Transferencia a cuenta " + idAccountEnd);
	        Movement movementEnd = new Movement(endAccount, quantity, "Transferencia desde cuenta " + idAccountStart);
	        movementRepository.save(movementStart);
	        movementRepository.save(movementEnd);

	        // 5. Registrar la transferencia
	        Transfer transfer = new Transfer(startAccount, endAccount, quantity);
	        transferRepository.save(transfer);

	        return true;*/
}
