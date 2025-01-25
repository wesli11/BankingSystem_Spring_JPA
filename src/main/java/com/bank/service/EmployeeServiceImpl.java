package com.bank.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

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
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	EmployeeRepository eRepository;
	@Autowired
	ClientRepository cRepository;
	@Override
	public void saveMovementService(Movements movement) {
		eRepository.saveMovement(movement);
	}

	@Override
	public Client findClientWithAccountsService(int idCliente) {
	    Client client = eRepository.findClientWithAccounts(idCliente); // JPQL para cargar cliente y sus cuentas
      	if (client == null) {
	        throw new IllegalArgumentException("Cliente no encontrado con ID: " + idCliente);
	    }
	    return client;
	}


	@Override
	public double calculateTotalBalanceService(int idCliente) {
		Client client=eRepository.findByClientById(idCliente);
		if(client!=null) {
			return eRepository.calculateTotalBalance(idCliente);
		}
		return 0;
	}

	@Override
	public List<Movements> findMovementsByAccountService(int idCuenta) {
		Account account=eRepository.findByAccountById(idCuenta).orElseThrow(() -> new RuntimeException("Account dont exist"));
		if(account!=null) {
			return eRepository.findMovementsByAccount(account.getIdAccount());
		}
		return null;
	}

	@Override
	public List<Account> findAccountsByMinimumBalanceService(double saldoMinimo) {
		return eRepository.findAccountsByMinimumBalance(saldoMinimo);
	}

	@Override
	public List<Movements> findAllMovementsByClientService(int idCliente) {
		Client client=eRepository.findByClientById(idCliente);
		if(client!=null) {
			return eRepository.findAllMovementsByClient(client.getDni());
		}
		return null;
	}

	@Override
	public List<Movements> findMovementsByClientInDateRangeService(int idCliente, LocalDateTime desde, LocalDateTime hasta) {
		Client client=eRepository.findByClientById(idCliente);
		if(client==null) {
			throw new IllegalArgumentException("Client dont exist");
		}
		if(desde.isAfter(hasta)) {
			throw new IllegalArgumentException("Dates error");
		}
		return eRepository.findMovementsByClientInDateRange(idCliente, desde, hasta);
	}

	@Override
	public List<Client> findClientsWithNegativeBalancesService() {
		return eRepository.findClientsWithNegativeBalances();
	}

	@Override
	public Map<String, Double> calculateTotalBalanceByAccountTypeService() {
		return eRepository.calculateTotalBalanceByAccountType();
	}

	@Override
	public List<Client> findClientsWithoutMovementsSinceService(LocalDateTime fecha) {
       if(fecha!=null) {
    	   return eRepository.findClientsWithoutMovementsSince(fecha);
       }
		return null;
	}

	@Override
	public List<Double> findBalanceHistoryByAccountService(int idCuenta, LocalDate desde, LocalDate hasta) {
		Account account=eRepository.findByAccountById(idCuenta).orElseThrow(() -> new RuntimeException("Account dont exist"));
        if(account!=null) {
        	return eRepository.findBalanceHistoryByAccount(idCuenta, desde, hasta);
        }
        return null;

	}

	@Override
	public Map<String, Integer> generateMovementReportByTypeService(int idCuenta, YearMonth mes) {
		Account account=eRepository.findByAccountById(idCuenta).orElseThrow(() -> new RuntimeException("Account dont exist"));
        if(account!=null) {
        	return eRepository.generateMovementReportByType(idCuenta, mes);
        }
        return null;
	}

	@Override
	public List<Client> findTopClientsByAverageBalanceService(int anio) {
		return eRepository.findTopClientsByAverageBalance(anio);
	}

	@Override
	public boolean blockAccountService(int idCuenta) {
		Account account=eRepository.findByAccountById(idCuenta).orElseThrow(() -> new RuntimeException("Account dont exist"));
        if(account!=null) {
        	eRepository.blockAccount(account.getIdAccount());
        	return true;
        }
		return false;
	}

	@Override
	public boolean unblockAccountService(int idCuenta) {
		Account account=eRepository.findByAccountById(idCuenta).orElseThrow(() -> new RuntimeException("Account dont exist"));
        if(account!=null) {
        	eRepository.unblockAccount(idCuenta);
        	return true;
        }
		return false;
	}

	@Transactional
	public boolean anularTransferencia(int idTransferencia, LocalDate fechaActual) {
		Transfer tf=eRepository.trasnferById(idTransferencia);
		if(tf==null) {
			throw new IllegalArgumentException("transfer not exist");
		}
	    Account accountOrigen= tf.getAccountOrigen();
		Account accountDestino=tf.getAccountDestino();
		  if (accountOrigen == null || accountDestino == null) {
		        throw new IllegalStateException("Una o ambas cuentas asociadas a la transferencia no existen.");
		      }
		  if(accountOrigen.getStatus()!=Account.Status.ACTIVA || accountDestino.getStatus()!=Account.Status.ACTIVA){
		        throw new IllegalStateException("Una de las cuentas no está activa.");
		  }
		  //compruebo que si deseo anular no sea mas de 7 dias
		  if (ChronoUnit.DAYS.between(tf.getFecha(), fechaActual) > 7) {
		        return false; // Fuera del límite de tiempo
		    }
              accountOrigen.setBalance(accountOrigen.getBalance()+ tf.getCantidad());
              accountDestino.setBalance(accountDestino.getBalance()- tf.getCantidad());
              // Crear el movimiento de reversión
              Movements movement = new Movements();
              movement.setDateMovement(LocalDateTime.now());
              movement.setQuantity(tf.getCantidad());
              movement.setOperation("Anulación de transferencia");
              movement.setAccount(accountOrigen);  // Asocia el movimiento con la cuenta de origen
              movement.setTransfers(tf);  // Relaciona el movimiento con la transferencia original

              // Guardar el movimiento de reversión
              eRepository.saveMovement(movement);
              tf.setEstado("ANULADA");
              tf.setFecha(LocalDate.now());
              eRepository.saveTransfer(tf);
              eRepository.saveAccount(accountOrigen);

              eRepository.saveAccount(accountDestino);
              return true;
	
		  }
	

	@Override
	public void saveClientService(Client client) {
		 Client c=eRepository.findByClientById(client.getDni());
		 if(c==null) {
			 eRepository.saveClient(c);
		 }
	}

	@Override
	public void saveAccountService(Account account) {
		 Account a=eRepository.findByAccountById(account.getIdAccount()).orElseThrow();
			 eRepository.saveAccount(a);
		 }

    @Transactional
	@Override
	public boolean registrarCuenta(int idCliente, int idCuenta) {
		Client client=eRepository.findByClientById(idCliente);
		
		if(client==null) {
			throw new IllegalArgumentException("Client dont exist");
		}
		if(client.getAccounts().contains(idCuenta)) {
			return false;
		}
		Account account=new Account();
		account.setIdAccount(idCuenta);
		account.setBalance(0);
		account.setStatus(Account.Status.ACTIVA);
		account.setTypeAccount("visa");
		client.getAccounts().add(account);
		eRepository.saveClient(client);
		eRepository.saveAccount(account);
		return true;
	}

	@Override
	public Client clientByIdService(int idClient) {
		return eRepository.findByClientById(idClient);
	}		



}
