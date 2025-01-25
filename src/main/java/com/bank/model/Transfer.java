package com.bank.model;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
@Entity
@Table(name="transferencias")
public class Transfer {
	   @Id
	   @GeneratedValue(strategy = GenerationType.IDENTITY)
       private int idTransferencia;
	   
	    private double cantidad;
	    private String estado; // COMPLETADA, ANULADA, etc.
	    private LocalDate fecha;
	    @ManyToOne
	    @JoinColumn(name = "cuentaOrigen", referencedColumnName = "numeroCuenta")
	    private Account accountOrigen;

	    @ManyToOne
	    @JoinColumn(name = "cuentaDestino", referencedColumnName = "numeroCuenta")
	    private Account accountDestino;

        @OneToMany(mappedBy = "transfers")
	    private List<Movements>movements;
	    // Constructor por defecto
	    public Transfer() {}

	    // Constructor parametrizado
	    public Transfer(Account accountOrigen, Account accountDestino, double cantidad, String estado, LocalDate fecha) {
	    	this.accountOrigen = accountOrigen;  // Aseguramos que se pase la entidad completa
			this.accountDestino = accountDestino;  // Aseguramos que se pase la entidad completa this.cantidad = cantidad;
	        this.estado = estado;
	        this.fecha = fecha;
	    }

		public Account getAccountOrigen() {
			return accountOrigen;
		}

		public void setAccountOrigen(Account accountOrigen) {
			this.accountOrigen = accountOrigen;
		}

		public Account getAccountDestino() {
			return accountDestino;
		}

		public void setAccountDestino(Account accountDestino) {
			this.accountDestino = accountDestino;
		}

		public List<Movements> getMovements() {
			return movements;
		}

		public void setMovements(List<Movements> movements) {
			this.movements = movements;
		}

		public int getIdTransferencia() {
			return idTransferencia;
		}

		public void setIdTransferencia(int idTransferencia) {
			this.idTransferencia = idTransferencia;
		}

	

		public double getCantidad() {
			return cantidad;
		}

		public void setCantidad(double cantidad) {
			this.cantidad = cantidad;
		}

		public String getEstado() {
			return estado;
		}

		public void setEstado(String estado) {
			this.estado = estado;
		}

		public LocalDate getFecha() {
			return fecha;
		}

		public void setFecha(LocalDate fecha) {
			this.fecha = fecha;
		}
	    
	    
}
