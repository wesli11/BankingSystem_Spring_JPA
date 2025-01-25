package com.bank.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="movimientos")
public class Movements {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idMovimiento") 
	private int idMovements;
    @Column(name = "fecha") // Mapeo de la columna tipoCuenta
	private LocalDateTime dateMovement;
    @Column(name = "cantidad") // Mapeo de la columna tipoCuenta
    private double quantity;
    @Column(name = "operacion") // Mapeo de la columna tipoCuenta
    private String operation;
	
	@ManyToOne
	@JoinColumn(name="idCuenta", referencedColumnName = "numeroCuenta")
	private Account account;
	@ManyToOne
	@JoinColumn(name="idTransferencia", referencedColumnName = "idTransferencia")
	Transfer transfers;
	
	public Movements() {}

	public Movements(int idMovements, LocalDateTime dateMovement, double quantity, 
			String operation, Account account, Transfer transfers) {
		super();
		this.idMovements = idMovements;
		this.dateMovement = dateMovement;
		this.quantity = quantity;
		this.operation = operation;
		this.account = account; // Aseguramos que se pase la entidad completa
		this.transfers = transfers; // Aseguramos que se pase la entidad completa
	}

	public int getIdMovements() {
		return idMovements;
	}

	public void setIdMovements(int idMovements) {
		this.idMovements = idMovements;
	}

	public LocalDateTime getDateMovement() {
		return dateMovement;
	}

	public void setDateMovement(LocalDateTime dateMovement) {
		this.dateMovement = dateMovement;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Transfer getTransfers() {
		return transfers;
	}

	public void setTransfers(Transfer transfers) {
		this.transfers = transfers;
	}
	
	

}
