package com.bank.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.bank.model.Client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.bank.model.Account;
import com.bank.model.Movements;
import com.bank.service.ClientService;
import com.bank.service.EmployeeService;
import com.bank.model.Client;

import jakarta.servlet.http.HttpSession;

@Controller
public class ClientController {

	@Autowired
	ClientService service;
	@Autowired
    EmployeeService eservice;
	
	@PostMapping("doValidate")
	public String validateAccount(@RequestParam("numeroCuenta")int numeroCuenta, HttpSession sesion) {
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		Account account=service.validarCuenta(numeroCuenta);
		      if(account!=null) {
				sesion.setAttribute("idAccount", account.getIdAccount());
				return "menu";
			}
		      return "acces";
		}
	
	@PostMapping("doIncome")
	public String deposited(@RequestParam("cantidad")double quantity,@SessionAttribute("idAccount") int idAccount) {
		service.depositService(quantity, idAccount);
		return "menu";
	}
	
	@PostMapping("doWithdraw")
     public String withdraw(@RequestParam("cantidad")double quantity,@SessionAttribute("idAccount") int idAccount) {
		 service.withdrawService(quantity, idAccount);
			return "menu";
	 }
	 
	@GetMapping("doFindMovementsInDateRange")
	 public String findMovementsInDateRange(Model model,@SessionAttribute("idAccount")int idAccount, @RequestParam("fecha1") String fecha1,
             @RequestParam("fecha2")String fecha2) {
		 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	        
	        // Convertir las fechas directamente a LocalDateTime con tiempos predeterminados
	        LocalDateTime dateTime1 = LocalDate.parse(fecha1, formatter).atStartOfDay();
	        LocalDateTime dateTime2 = LocalDate.parse(fecha2, formatter).atTime(23, 59, 59);
            model.addAttribute("movs",service.findMovementsInDateRangeService(idAccount, dateTime1, dateTime2));
       return "movements";
	}
	
	@PostMapping("doTransfer")
	public String trasnferToMoney(@SessionAttribute("idAccount")int idAccount,@RequestParam("cuentaDestino") int idAccount2,@RequestParam("cantidad") double quantity) {;
        service.transfer(idAccount, idAccount2, quantity);
      return "menu";
	}
	
	//2- actualizar cliente
    @PostMapping("doUpdateClient")
    public String updateClient(@ModelAttribute("client")Client client) {
    	Client c = eservice.clientByIdService(client.getDni());
    	   
    	        // Guardar el cliente actualizado
    	        service.updateClientService(c);
    	        return "menu";
    	    
    }
   
    @GetMapping("toUpdateClient")
    public String getClientDni(Model model, @RequestParam(name = "dni", required = false) Integer dni) {
        if (dni == null) {
            model.addAttribute("clientUpdate", null); // No hay cliente cargado
            return "updateclient"; // Mostrar formulario vacío
        }
        
        Client clientUpdate = eservice.clientByIdService(dni); // Buscar cliente por DNI
        if (clientUpdate == null) {
            model.addAttribute("error", "Cliente no encontrado");
            return "updateclient"; // Mostrar vista con mensaje de error
        }

        model.addAttribute("clientUpdate", clientUpdate); // Pasar cliente a la vista
        return "updateclient"; // Mostrar formulario de actualización con los datos

    }
}