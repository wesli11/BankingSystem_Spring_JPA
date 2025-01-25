package com.bank.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bank.model.Account;
import com.bank.model.Client;
import com.bank.model.Movements;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

@Repository
public class ClientRepositoryImpl implements ClientRepository {
    
	@PersistenceContext
	EntityManager em;
	@Override
	public Optional<Double> findBalanceByAccount(int idAccount) {
		String jpql="Select c.balance from Account c where c.idAccount=?1";
		TypedQuery<Double> query= em.createQuery(jpql, Double.class);
		query.setParameter(1, idAccount);
		 try {
	            return Optional.of(query.getSingleResult());
	        } catch (NoResultException e) {
	            return Optional.empty();  // Devuelve Optional vacío si no se encuentra el balance
	        }	
		 }

	@Transactional
	@Override
	public boolean deposit(double quantity, int idAccount) {
		String jpql="Update Account d Set d.balance=d.balance + ?1 where d.idAccount =?2 AND d.status = ?3";
		Query query=em.createQuery(jpql);
		query.setParameter(1, quantity);
		query.setParameter(2, idAccount);
	    query.setParameter(3,Account.Status.ACTIVA); // Pasa el enum como parámetro

		return query.executeUpdate()>0;
				
	}

	@Override
	public Account findAccountById(int idAccount) {
		String jpql="Select c from Account c where c.idAccount=?1";
		TypedQuery<Account>query=em.createQuery(jpql, Account.class);
		query.setParameter(1, idAccount);
       List<Account>accounts=query.getResultList();
		return accounts.size()>0?accounts.get(0):null;
	}

	@Transactional
	@Override
	public boolean withdraw(double quantity, int idAccount) {
		String jpql="Update Account d Set d.balance=d.balance - ?1 where d.idAccount =?2 AND d.status=?3";
		Query query=em.createQuery(jpql);
		query.setParameter(1, quantity);
		query.setParameter(2, idAccount);
		query.setParameter(3,Account.Status.ACTIVA);
		return query.executeUpdate()>0;
	}

	

	@Override
	public List<Movements> findMovementsInDateRange(int idAccount, LocalDateTime from, LocalDateTime to) {
		String jpql="Select m from Movements m where m.account.idAccount=?1 and m.dateMovement between ?2 and ?3";
		TypedQuery<Movements>movs=em.createQuery(jpql, Movements.class);
		movs.setParameter(1, idAccount);
		movs.setParameter(2, from);
		movs.setParameter(3, to);
		return movs.getResultList();
	}

	@Transactional
	@Override
	public void updateClient(Client client) {
       em.merge(client);		
	}

	

}
