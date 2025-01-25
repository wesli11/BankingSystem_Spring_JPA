package com.bank.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bank.model.Account;
import com.bank.model.Client;
import com.bank.repository.EmployeeRepository;
import com.bank.service.EmployeeService;

@Controller
public class EmployeeController {
       @Autowired
       EmployeeService service;
       
      @GetMapping("/newClient")
       public String showNewClientForm(Model model) {
           // Crear un objeto vacío de Client y añadirlo al modelo
           model.addAttribute("client", new Client());
           return "newcustomer"; // Nombre del archivo HTML (newcustomer.html)
       }
       
    @PostMapping("/doSaveClient")
    public String doSaveClient(@ModelAttribute Client client) {
    	service.saveClientService(client);
    	return "menu";
    }
       
    @PostMapping("doSaveAccount")
    public String doSaveAccount(@RequestParam("idAccount")int idCuenta,@RequestParam("dni")int idCliente ) {
    	service.registrarCuenta(idCliente,idCuenta);
    	return "menu";
    }
      
    @PostMapping("doFindMovementsByClientInDateRange")
    public String findMovementsByClientInDateRange(Model model, @RequestParam("idCliente")int idCliente,@RequestParam("fecha1") String fecha1,
            @RequestParam("fecha2")String fecha2) {
    	 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	        
	        // Convertir las fechas directamente a LocalDateTime con tiempos predeterminados
	        LocalDateTime dateTime1 = LocalDate.parse(fecha1, formatter).atStartOfDay();
	        LocalDateTime dateTime2 = LocalDate.parse(fecha2, formatter).atTime(23, 59, 59);
      model.addAttribute("movclienterango", service.findMovementsByClientInDateRangeService(idCliente, dateTime1, dateTime2));
        return "movclienterango";
    }
    
    @GetMapping("doFindClientWithAccounts")
	public String findClientWithAccounts(Model model, @RequestParam("idCliente")int idCliente) {
    	Client client=service.findClientWithAccountsService(idCliente);
    	System.out.println("CLIENT CONTROLLER " + client);
    	model.addAttribute("clientaccounts",client);
		return "clientaccounts";
	}
    
   
    
    
    
    
    
    
    
    
    @GetMapping("doFindMovementsByAccount")
	public String findMovementsByAccount(Model model, @RequestParam("idCuenta")int idCuenta) {
	  model.addAttribute("findMovementsByAccount", service.findMovementsByAccountService(idCuenta));
      return "findMovementsByAccount";
    }
    
    @GetMapping("doFindClientsWithoutMovementsSince")
    public String findClientsWithoutMovementsSince(Model model,@RequestParam("fecha1") String desde) {
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        // Convertir las fechas directamente a LocalDateTime con tiempos predeterminados
        LocalDateTime dateTime1 = LocalDate.parse(desde, formatter).atStartOfDay() ;
	    model.addAttribute("findClientsWithoutMovementsSince", service.findClientsWithoutMovementsSinceService(dateTime1));
        return "sinmovimientodesde";
    }
    
    @GetMapping("doFindAllMovementsByClient")
    public String findAllMovementsByClient(Model model, @RequestParam("dni")int idCliente) {
	    model.addAttribute("findAllMovementsByClient", service.findAllMovementsByClientService(idCliente));
        return "movcuentacliente";
    }
    
    @PostMapping("blockAccount")
	public String blockAccount(Model model, @RequestParam("numeroCuenta")int idCuenta) {
	   service.blockAccountService(idCuenta);
      return "menu";
    }
    

	@PostMapping("unblockAccount")
	public String unblockAccount(Model model, @RequestParam("numeroCuenta")int idCuenta) {
	   service.unblockAccountService(idCuenta);
      return "menu";
    }
	
	 @PostMapping("doCancelTransfer")
     public String cancelTransfer(@RequestParam("idTransferencia")int idTransferencia, 
     		@RequestParam("fecha1")String fecha1) {     	
	        if (fecha1 == null || fecha1.trim().isEmpty()) {
	            throw new IllegalArgumentException("La fecha no puede estar vacía.");
	        }
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Ajusta según tu formato
	        LocalDate fecha = LocalDate.parse(fecha1, formatter); 
     	      service.anularTransferencia(idTransferencia, fecha);
     	      return "menu";
     }
	 
	
    ////////////////////////////////////////////////////////
    
    
    
    
    
    
	

	@GetMapping("doCalculateTotalBalance")
    public String calculateTotalBalance(Model model, @RequestParam("idCliente")int idCliente) {
	    model.addAttribute("calculateTotalBalance", service.calculateTotalBalanceService(idCliente));
        return "calculateTotalBalance";
    }

	

	@GetMapping("doFindAccountsByMinimumBalance")
	public String findAccountsByMinimumBalance(Model model, @RequestParam("idCuenta")int idCuenta) {
	  model.addAttribute("findAccountsByMinimumBalance", service.findAccountsByMinimumBalanceService(idCuenta));
      return "findAccountsByMinimumBalance";
    }
	   //     List<Movements> findAllMovementsByClient(int idCliente);

	
	  //      List<Movements> findMovementsByClientInDateRange(int idCliente, LocalDate desde, LocalDate hasta);

	
	//    List<Client> findClientsWithNegativeBalances();
	@GetMapping("doFindClientsWithNegativeBalances")
    public String findClientsWithNegativeBalances(Model model) {
	    model.addAttribute("findClientsWithNegativeBalances", service.findClientsWithNegativeBalancesService());
        return "findClientsWithNegativeBalances";
    }
	
	//      Map<String, Double> calculateTotalBalanceByAccountType();
	@GetMapping("calculateTotalBalanceByAccountType")
	public String calculateTotalBalanceByAccountType(Model model) {
	    model.addAttribute("calculateTotalBalanceByAccountType", service.findClientsWithNegativeBalancesService());
	     return "calculateTotalBalanceByAccountType";
	    }
	
	
	
	@GetMapping("doFindBalanceHistoryByAccount")
    public String findBalanceHistoryByAccount(Model model, @RequestParam("idCuenta")int idCuenta,@RequestParam("fecha1") String desde,
            @RequestParam("fecha2")String hasta){
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Ajusta según tu formato
		   LocalDate str1 = LocalDate.parse(desde, formatter);  
		   LocalDate str2 = LocalDate.parse(hasta, formatter);
	   // model.addAttribute("findBalanceHistoryByAccount", service.findMovementsByClientInDateRangeService(idCuenta, str1, str2));
     return "findBalanceHistoryByAccount";
    }
     // HACER ESTE METODO
	//   Map<String, Integer> generateMovementReportByType(int idCuenta, YearMonth mes){
	
     //}
	@GetMapping("doFindTopClientsByAverageBalance")
    public String findTopClientsByAverageBalance(Model model,@RequestParam("anio")int anio) {
		 model.addAttribute("findTopClientsByAverageBalance", service.findTopClientsByAverageBalanceService(anio));
	     return "findTopClientsByAverageBalance";
    }

	//     boolean blockAccount(int idCuenta);
	
	
}