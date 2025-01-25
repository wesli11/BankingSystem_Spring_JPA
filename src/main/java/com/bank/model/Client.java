package com.bank.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="clientes")
public class Client {
    @Id
	private int dni;
    @Column(name = "nombre") // Mapeo de la columna 
	private String name;
    @Column(name = "direccion") 
	private String address;
    @Column(name = "telefono") 
	private int phone;
	
	@ManyToMany
	@JoinTable(name="titulares",
	           joinColumns = @JoinColumn(name="idCliente",referencedColumnName ="dni"),
	           inverseJoinColumns = @JoinColumn(name="idCuenta",referencedColumnName = "numeroCuenta"))
	private List<Account>accounts;
	
	public Client() {}

	public Client(int dni, String name, String address, int phone) {
		super();
		this.dni = dni;
		this.name = name;
		this.address = address;
		this.phone = phone;
		this.accounts=new ArrayList<>();
	}

	public int getDni() {
		return dni;
	}

	public void setDni(int dni) {
		this.dni = dni;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getPhone() {
		return phone;
	}

	public void setPhone(int phone) {
		this.phone = phone;
	}

	public List<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	}
	@Override
	public String toString() {
	    return "Client{" +
	            "dni=" + dni +
	            ", name='" + name + '\'' +
	            ", address='" + address + '\'' +
	            ", phone=" + phone +
	            ", accounts=" + accounts +
	            '}';
	}

}
