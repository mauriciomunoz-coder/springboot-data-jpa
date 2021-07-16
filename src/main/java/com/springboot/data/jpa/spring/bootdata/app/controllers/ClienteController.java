package com.springboot.data.jpa.spring.bootdata.app.controllers;


import com.springboot.data.jpa.spring.bootdata.app.models.dao.IClienteDao;
import com.springboot.data.jpa.spring.bootdata.app.models.entity.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@Controller
public class ClienteController {


    @Autowired
    private IClienteDao IClienteDao;

    @GetMapping(value = "listar")
    public String listar(Model model) {
        model.addAttribute("titulo", "listado de clientes");
        model.addAttribute("clientes", IClienteDao.findAll());
        return "listar";
    }


    @GetMapping(value = "/form")
    public String crear(Map<String, Object> model) {
        Cliente cliente = new Cliente();
        model.put("cliente", cliente);
        model.put("titulo", "Formulario de Cliente");

        return "form";
    }

    @PostMapping(value = "/form")
    public String guardar(Cliente cliente){
        IClienteDao.save(cliente);
        return "redirect:listar";
    }
}
