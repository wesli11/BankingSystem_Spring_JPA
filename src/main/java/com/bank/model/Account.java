package com.bank.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="cuentas")
public class Account {
	 @Id
	 @Column(name = "numeroCuenta") 
	 private int idAccount;
	  @Column(name = "saldo") // Mapeo de la columna saldo
	  private double balance;

	  @Column(name = "tipocuenta") // Mapeo de la columna tipoCuenta
	  private String typeAccount;

	  @Enumerated(EnumType.STRING) // Esto asegura que se mapea como texto
	  @Column(name = "estado") // Mapeo de la columna tipoCuenta
      private Status status;
	
	@ManyToMany(mappedBy = "accounts")
	private List<Client>clients;
	
	@OneToMany(mappedBy = "account")
	private List<Movements>movements;
	
	@OneToMany(mappedBy = "accountOrigen")
    private List<Transfer> outgoingTransfers; // Transferencias enviadas

    @OneToMany(mappedBy = "accountDestino")
    private List<Transfer> incomingTransfers;  // Transferencias recibidas
	public enum Status{
		ACTIVA,
		BLOQUEADA
	}
	public Account() {}
	
	
	public Account(int idAccount, double balance, String typeAccount) {
		super();
		this.idAccount = idAccount;
		this.balance = balance;
		this.typeAccount = typeAccount;
		this.status=Status.ACTIVA;
		clients=new ArrayList<>();
		movements=new ArrayList<>();
		outgoingTransfers=new ArrayList<>();
		incomingTransfers=new ArrayList<>();
	}



	public List<Client> getClients() {
		return clients;
	}


	public void setClients(List<Client> clients) {
		this.clients = clients;
	}


	public List<Movements> getMovements() {
		return movements;
	}


	public void setMovements(List<Movements> movements) {
		this.movements = movements;
	}


	public List<Transfer> getOutgoingTransfers() {
		return outgoingTransfers;
	}


	public void setOutgoingTransfers(List<Transfer> outgoingTransfers) {
		this.outgoingTransfers = outgoingTransfers;
	}


	public List<Transfer> getIncomingTransfers() {
		return incomingTransfers;
	}


	public void setIncomingTransfers(List<Transfer> incomingTransfers) {
		this.incomingTransfers = incomingTransfers;
	}


	public int getIdAccount() {
		return idAccount;
	}

	public void setIdAccount(int idAccount) {
		this.idAccount = idAccount;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public String getTypeAccount() {
		return typeAccount;
	}

	public void setTypeAccount(String typeAccount) {
		this.typeAccount = typeAccount;
	}


	public Status getStatus() {
		return status;
	}


	public void setStatus(Status status) {
		this.status = status;
	}


	@Override
	public String toString() {
		return "Account [idAccount=" + idAccount + ", balance=" + balance + ", typeAccount=" + typeAccount + ", status="
				+ status + "]";
	}
	
	

}
