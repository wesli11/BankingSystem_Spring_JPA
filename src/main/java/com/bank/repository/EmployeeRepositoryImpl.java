package com.bank.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.bank.model.Account;
import com.bank.model.Client;
import com.bank.model.Movements;
import com.bank.model.Transfer;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

@Repository
public class EmployeeRepositoryImpl implements EmployeeRepository {

	@PersistenceContext
	EntityManager em;
	
	@Transactional
	@Override
	public void saveMovement(Movements movement) {
      em.persist(movement);
	}

	@Override
	public Client findClientWithAccounts(int idCliente) {
      String jpql="Select c from Client c LEFT JOIN FETCH c.accounts where c.dni=?1";
      TypedQuery<Client>query=em.createQuery(jpql, Client.class);
      query.setParameter(1, idCliente);
      List<Client>clientId= query.getResultList();
      System.out.println("Cliente Repository : " + clientId);
       return clientId.size()>0?clientId.get(0):null;
	 }
	
	@Override
	public Client findByClientById(int idCliente) {
		String jpql="Select c from Client c where c.dni=?1";
	      TypedQuery<Client>query=em.createQuery(jpql, Client.class);
	      query.setParameter(1, idCliente);
	          List<Client>clientId=query.getResultList();
         	return clientId.size()>0?clientId.get(0):null;
	}

	@Override
	public double calculateTotalBalance(int idCliente) {
		String jpql="Select SUM(c.balance) from Account c join c.clients t where t.dni=?1";
		 TypedQuery<Double>query=em.createQuery(jpql, Double.class);
	      query.setParameter(1, idCliente);
        	return query.getSingleResult();
        }

	@Override
	public List<Movements> findMovementsByAccount(int idCuenta) {
		String jpql="Select m from Movements m where m.account.idAccount=?1";
	      TypedQuery<Movements>query=em.createQuery(jpql, Movements.class);
           query.setParameter(1, idCuenta);
     	   return query.getResultList();	
     	  }

	@Override
	public List<Account> findAccountsByMinimumBalance(double saldoMinimo) {
		String jpql="Select c from Account c where c.balance>=?1";
	      TypedQuery<Account>query=em.createQuery(jpql, Account.class);
         query.setParameter(1, saldoMinimo);
   	   return query.getResultList();	
   	  }

    @Override
	public List<Movements> findAllMovementsByClient(int idCliente) {
	  //SELECT m FROM Movements m JOIN m.account.clients c WHERE c.dni = :dni
    String jpql="Select m from Movements m WHERE m.account IN (Select c From Account c join c.clients a where a.dni=?1)";
	   TypedQuery<Movements>query=em.createQuery(jpql, Movements.class);
	      query.setParameter(1, idCliente);
     	   return query.getResultList();	
       }

	@Override
	public List<Movements> findMovementsByClientInDateRange(int idCliente, LocalDateTime desde, LocalDateTime hasta) {
		//"SELECT m FROM Movements m JOIN m.account a JOIN a.clients c WHERE c.dni = :dni " +
         // "AND m.dateMovement BETWEEN :desde AND :hasta"
		String jpql="Select m From Movements m WHERE m.account IN( "
				+ "Select a From Account a join a.clients c where c.dni=?1) AND m.dateMovement between ?2 AND ?3";
	   TypedQuery<Movements>query=em.createQuery(jpql, Movements.class);
	      query.setParameter(1, idCliente);
	      query.setParameter(2, desde);
	      query.setParameter(3, hasta);
     	   return query.getResultList();	
     
	}

	@Override
	public List<Client> findClientsWithNegativeBalances() {
		 String jpql="Select c from Client c JOIN c.accounts a where a.balance<0";
	      TypedQuery<Client>query=em.createQuery(jpql, Client.class);
	        return query.getResultList();
	}

	@Override
	public Map<String, Double> calculateTotalBalanceByAccountType() {
     String jpql="Select c.typeAccount, SUM(c.balance) From Account c GROUP BY c.typeAccount";
     List<Object[]>results=em.createQuery(jpql).getResultList();
		Map<String, Double>totalBalanceByType=new HashMap<>();
		for (Object[] result : results) {
	        String typeAccount = (String) result[0];  // Tipo de cuenta
	        Double totalBalance = (Double) result[1]; // Suma de saldos
	        totalBalanceByType.put(typeAccount, totalBalance);
	    }
     return totalBalanceByType;
	}

	@Override
	@Transactional
	public List<Client> findClientsWithoutMovementsSince(LocalDateTime fecha) {
		String jpql = "SELECT DISTINCT c FROM Client c " +
	              "JOIN FETCH c.accounts a " +
	              "LEFT JOIN a.movements m " +
	              "WHERE m.dateMovement IS NULL OR m.dateMovement < :fecha";

//		String jpql = "SELECT c FROM Client c " +
//                "JOIN FETCH c.accounts a " + // Asociamos las cuentas
//                "WHERE EXISTS (SELECT 1 FROM a.movements m WHERE m.dateMovement IS NULL OR m.dateMovement < :fecha)"; 
//		
  TypedQuery<Client> query = em.createQuery(jpql, Client.class);
  query.setParameter("fecha", fecha);
		  return query.getResultList();
	}

	@Override
	public List<Double> findBalanceHistoryByAccount(int idCuenta, LocalDate desde, LocalDate hasta) {
     String jpql="Select SUM(m.quantity) From Movements m WHERE m.account.idAccount=?1 AND m.dateMovement between ?2 AND ?3";
	 TypedQuery<Double>query=em.createQuery(jpql, Double.class);
	 query.setParameter(1, idCuenta);
     query.setParameter(2, java.sql.Date.valueOf(desde));
     query.setParameter(3, java.sql.Date.valueOf(hasta));return query.getResultList();
	}

	@Override
	public Map<String, Integer> generateMovementReportByType(int idCuenta, YearMonth mes) {
		 String jpql = "SELECT m.operacion, COUNT(m) " +
                 "FROM Movements m " +
                 "WHERE m.account.idAccount = :idCuenta " +
                 "AND FUNCTION('YEAR', m.dateMovement) = :year " +
                 "AND FUNCTION('MONTH', m.dateMovement) = :month " +
                 "GROUP BY m.operacion";

   TypedQuery<Object[]> query = em.createQuery(jpql, Object[].class);
   query.setParameter("idCuenta", idCuenta);
   query.setParameter("year", mes.getYear());
   query.setParameter("month", mes.getMonthValue());

   // Ejecutar la consulta y transformar los resultados en un Map
   List<Object[]> results = query.getResultList();
   Map<String, Integer> report = new HashMap<>();
   for (Object[] result : results) {
       String operacion = (String) result[0];
       Integer count = ((Number) result[1]).intValue();
       report.put(operacion, count);
   }

   return report;
	}

	@Override
	public List<Client> findTopClientsByAverageBalance(int anio) {
		String jpql = "SELECT c " +
                "FROM Client c " +
                "JOIN c.accounts a " +
                "JOIN a.movements m " +
                "WHERE FUNCTION('YEAR', m.dateMovement) = :anio " +
                "GROUP BY c " +
                "HAVING AVG(a.balance) = (SELECT MAX(AVG(a2.balance)) " +
                "                        FROM Account a2 " +
                "                        JOIN a2.movements m2 " +
                "                        WHERE FUNCTION('YEAR', m2.dateMovement) = :anio " +
                "                        GROUP BY a2.client)";
		 TypedQuery<Client> query = em.createQuery(jpql, Client.class);
		    query.setParameter("anio", anio);
		    return query.getResultList();
	}
	@Transactional
	@Override
	public boolean blockAccount(int idCuenta) {
     String jpql="UPDATE Account a SET a.status=:status WHERE a.idAccount=?1";
      Query query=em.createQuery(jpql);
      query.setParameter("status", Account.Status.BLOQUEADA);
      query.setParameter(1, idCuenta);
      	return query.executeUpdate()>0;
	}
	@Transactional
	@Override
	public boolean unblockAccount(int idCuenta) {
		 String jpql="UPDATE Account a SET a.status=:status WHERE a.idAccount=:idCuenta";
	      Query query=em.createQuery(jpql);
	      query.setParameter("status", Account.Status.ACTIVA); // Usar el valor de la enumeración
	      query.setParameter("idCuenta", idCuenta);
         return query.executeUpdate()>0;
	}

	@Override
	public Optional<Account> findByAccountById(int idAccount) {
       String jpql="Select c From Account c where c.idAccount=?1";
       TypedQuery<Account>query=em.createQuery(jpql, Account.class);
       query.setParameter(1, idAccount);
       try {
           Account account = query.getSingleResult(); // Puede lanzar NoResultException
           return Optional.ofNullable(account);
       } catch (NoResultException e) {
           return Optional.empty(); // Si no encuentra resultados, devuelve Optional vacío
       }	}

	@Transactional
	@Override
	public void saveTransfer(Transfer transfer) {
       em.persist(transfer);		
	}

	@Override
	public Transfer trasnferById(int idTransfer) {
		String jpql="Select t From Transfer t where idTransferencia=?1";
	       TypedQuery<Transfer>query=em.createQuery(jpql, Transfer.class);
           query.setParameter(1, idTransfer);
		 return query.getSingleResult();
	}

	@Transactional
	@Override
	public void saveClient(Client client) {
    	   em.persist(client);
   }

	@Transactional
	@Override
	public void saveAccount(Account account) {
    	   em.persist(account);
      
		
	}

	

}
