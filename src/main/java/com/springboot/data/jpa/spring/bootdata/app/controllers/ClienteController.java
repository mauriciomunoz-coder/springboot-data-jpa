package com.springboot.data.jpa.spring.bootdata.app.controllers;


import com.springboot.data.jpa.spring.bootdata.app.models.dao.IClienteDao;
import com.springboot.data.jpa.spring.bootdata.app.models.entity.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.validation.Valid;
import java.util.Map;

@Controller
@SessionAttributes("cliente")
public class ClienteController {


    // inyectamos la dependencia de IClienteDao
    @Autowired
    private IClienteDao IClienteDao;


    //lista los clientes
    @GetMapping(value = "listar")
    public String listar(Model model) {
        model.addAttribute("titulo", "listado de clientes");
        model.addAttribute("clientes", IClienteDao.findAll());
        return "listar";
    }


    //redirige al formulario
    @GetMapping(value = "/form")
    public String crear(Map<String, Object> model) {
        Cliente cliente = new Cliente();
        model.put("cliente", cliente);  //se pone para mapear el objeto al formulario
        model.put("titulo", "Formulario de Cliente");

        return "form";
    }


    //guarda o modifica un cliente dependiendo de si tiene id o No, "revisar ClienteDaoImpl.java"
    @PostMapping(value = "/guardar")
    public String guardar(@Valid Cliente cliente, BindingResult result, Model model, SessionStatus status) {
        if (result.hasErrors()) {
            model.addAttribute("titulo", "Formulario de Cliente");
            return "form";
        }

        IClienteDao.save(cliente);
        status.setComplete();
        return "redirect:listar";
    }


    //redirige al formulario con el cliente encontrado para mostrarlo en los campos
    @GetMapping(value = "/form/{id}")
    public String editar(@PathVariable(value = "id") Long id, Model model) {

        Cliente cliente = null;

        if (id > 0) {
            cliente = IClienteDao.findOne(id);
        } else {
            return "redirect:/listar";
        }
        model.addAttribute("cliente", cliente);
        model.addAttribute("titulo", "Editar Cliente");
        return "form";
    }

    @GetMapping(value = "/delete/{id}")
    public String delete(@PathVariable(value = "id") Long id) {
        if (id > 0) {
            IClienteDao.delete(id);
        }
        return "redirect:/listar";
    }


}
