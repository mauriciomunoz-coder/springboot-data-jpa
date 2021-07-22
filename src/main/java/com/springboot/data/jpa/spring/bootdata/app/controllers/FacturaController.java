package com.springboot.data.jpa.spring.bootdata.app.controllers;

import com.springboot.data.jpa.spring.bootdata.app.models.entity.Cliente;
import com.springboot.data.jpa.spring.bootdata.app.models.entity.Factura;
import com.springboot.data.jpa.spring.bootdata.app.models.service.IClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/factura")
@SessionAttributes("factura") //el nombre con el que se pasa a la vista
public class FacturaController {

    @Autowired
    IClienteService iClienteService;

    @GetMapping("/form/{clienteId}")
    public String crear(@PathVariable(value = "clienteId") Long clienteId, Model model, RedirectAttributes flash, SessionStatus status) {

        //encontramos el cliente para poder asignarlo a la factura
        Cliente cliente = iClienteService.findOne(clienteId);
        if (cliente == null) {
            flash.addFlashAttribute("error", "El cliente No existe en la BD");
            return "redirect:/listar";
        }

        //asignamos un cliente a la factura
        Factura factura = new Factura();
        factura.setCliente(cliente);

        model.addAttribute("factura", factura);  //compartimos la factura junto con el cliente
        status.setComplete();
        model.addAttribute("titulo", "Crear Factura");

        return "factura/form";
    }
}
