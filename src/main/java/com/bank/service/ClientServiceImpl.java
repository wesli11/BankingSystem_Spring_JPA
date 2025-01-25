package com.bank.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bank.model.Account;
import com.bank.model.Client;
import com.bank.model.Movements;
import com.bank.model.Transfer;
import com.bank.repository.ClientRepository;
import com.bank.repository.EmployeeRepository;

@Service
public class ClientServiceImpl implements ClientService {

	@Autowired
	ClientRepository cRepository;
	@Autowired
    EmployeeRepository eRepository;
	
	public Optional<Double> findBalanceByAccountService(int idAccount) {
        Account ac=cRepository.findAccountById(idAccount);
        if(ac!=null) {
        	return cRepository.findBalanceByAccount(idAccount);
        }
		return Optional.empty();
	}

    @Transactional // Úsalo aquí también si es un método que realiza múltiples operaciones
	public boolean depositService(double quantity, int idAccount) {
        Account ac=cRepository.findAccountById(idAccount);
        if(ac!=null) {
        	cRepository.deposit(quantity, idAccount);
        	return true;
        }
		return false;
	}


	public Account findAccountByIdService(int idAccount) {
		return cRepository.findAccountById(idAccount);
	 }


	public boolean withdrawService(double quantity, int idAccount) {
		Account ac=cRepository.findAccountById(idAccount);
		if(ac!=null) {
			cRepository.withdraw(quantity, idAccount);
			return true;
		}
		return false;
	}


	public List<Movements> findMovementsInDateRangeService(int idAccount, LocalDateTime from, LocalDateTime to) {
		Account ac=cRepository.findAccountById(idAccount);
		if(ac!=null) {
		    return cRepository.findMovementsInDateRange(idAccount, from, to);
		}
		 return new ArrayList<>();
		}


	@Override
	public boolean transfer(int idAccountStart, int idAccountEnd, double quantity) {
		// 1. Verificar que las cuentas existen
	    Account account1 = eRepository.findByAccountById(idAccountStart).orElseThrow(() -> new RuntimeException("Cuenta de origen no encontrada"));
	     System.out.println("CUENTA ORIGEN " + account1.getIdAccount());
	    Account account2 = eRepository.findByAccountById(idAccountEnd).orElseThrow(() -> new RuntimeException("Cuenta de destino no encontrada"));
	     System.out.println("CUENTA DESTINO " + account2.getIdAccount());

	    // 2. Verificar saldo suficiente
	    if (account1.getBalance() < quantity) {
	        throw new RuntimeException("Saldo insuficiente en la cuenta de origen");
	    }

	    // 3. Realizar operaciones de débito y crédito
	    cRepository.withdraw(quantity, idAccountStart);
	    cRepository.deposit(quantity, idAccountEnd);

	    // 4. Registrar la transferencia
	    Transfer tr = new Transfer();
	    tr.setAccountOrigen(account1);
	    tr.setAccountDestino(account2); // Corrección aquí
	    tr.setCantidad(quantity);
	    tr.setFecha(LocalDate.now());
	    tr.setEstado("COMPLETADA");
	    eRepository.saveTransfer(tr);

	    // 5. Registrar movimientos
	    Movements m1 = new Movements();
	    m1.setAccount(account1);
	    m1.setOperation("Extraccion");
	    m1.setDateMovement(LocalDateTime.now());
	    m1.setQuantity(-quantity);
	    eRepository.saveMovement(m1);

	    Movements m2 = new Movements();
	    m2.setAccount(account2); // Corrección aquí
	    m2.setOperation("Ingreso");
	    m2.setDateMovement(LocalDateTime.now());
	    m2.setQuantity(quantity);
	    eRepository.saveMovement(m2);

	    return true;
	
	}
	/*
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


	@Override
	public Account validarCuenta(int idCuenta) {
		return eRepository.findByAccountById(idCuenta)
	            .orElseThrow(() -> new RuntimeException("Cuenta de origen no encontrada"));
	}

	@Override
	public void updateClientService(Client client) {
		 Client c = eRepository.findByClientById(client.getDni());
		    if (c != null) {
		        // Actualiza los campos del cliente con los nuevos valores
		        c.setName(client.getName());
		        c.setAddress(client.getAddress());
		        c.setPhone(client.getPhone());
		        
		        // Llama al repositorio para guardar el cliente actualizado
		        cRepository.updateClient(c);
		
	}
}
}
