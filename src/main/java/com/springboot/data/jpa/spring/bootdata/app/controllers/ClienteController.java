package com.springboot.data.jpa.spring.bootdata.app.controllers;


import com.springboot.data.jpa.spring.bootdata.app.models.entity.Cliente;
import com.springboot.data.jpa.spring.bootdata.app.models.service.IClienteService;
import com.springboot.data.jpa.spring.bootdata.app.util.paginator.PageRender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@SessionAttributes("cliente")
public class ClienteController {


    // inyectamos la dependencia de IClienteDao
    @Autowired
    private IClienteService iClienteService;

    @GetMapping(value = "/ver/{id}")
    public String ver(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {

        Cliente cliente = iClienteService.findOne(id);
        if (cliente == null) {
            flash.addFlashAttribute("error", "El cliente no existe en la base de datos");
            return "redirect:/listar";
        }

        model.put("cliente", cliente);
        model.put("titulo", "Detalle cliente: " + cliente.getNombre());
        return "ver";
    }

    //lista los clientes
    @GetMapping(value = "/listar")
    public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {

        //**************** codigo de paginacion *************************
        Pageable pageRequest = PageRequest.of(page, 4);
        Page<Cliente> clientes = iClienteService.findAll(pageRequest);
        PageRender<Cliente> pageRender = new PageRender<>("/listar", clientes);
        //***************************************************************
        model.addAttribute("titulo", "listado de clientes");
        model.addAttribute("clientes", clientes);
        model.addAttribute("page", pageRender);
        //model.addAttribute("clientes", iClienteService.findAll()); //se usa sin paginacion
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

    private final Logger log = LoggerFactory.getLogger(getClass());
    //guarda o modifica un cliente dependiendo de si tiene id o No, "revisar ClienteDaoImpl.java"
    @PostMapping(value = "/guardar")
    public String guardar(@Valid Cliente cliente, BindingResult result, Model model, @RequestParam("file") MultipartFile foto, RedirectAttributes flash, SessionStatus status) {
        if (result.hasErrors()) {
            model.addAttribute("titulo", "Formulario de Cliente");
            return "form";

            //******************** codigo para subir imagenes *******************************************************************
        }
        if (!foto.isEmpty()) {

            String uniqueFilename = UUID.randomUUID().toString() + "_" + foto.getOriginalFilename();
            Path rootPath = Paths.get("uploads").resolve(uniqueFilename);

            Path rootAbsolutPath = rootPath.toAbsolutePath();

            log.info("rootPath: " + rootPath);
            log.info("rootAbsolutPath: " + rootAbsolutPath);

            try {

                Files.copy(foto.getInputStream(), rootAbsolutPath);

                flash.addFlashAttribute("info", "Has subido correctamente '" + uniqueFilename + "'");

                cliente.setFoto(uniqueFilename);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //******************************************************************************************************************************************
        String mensajeFlash = (cliente.getId() != null) ? "Cliente editado con éxito!" : "Cliente creado con éxito!"; //verifica si el cliente ya existe o es nuevo y dependiendo saca el mensaje


        iClienteService.save(cliente);
        status.setComplete();
        flash.addFlashAttribute("success", mensajeFlash);
        return "redirect:listar";
    }


    //redirige al formulario con el cliente encontrado para mostrarlo en los campos
    @GetMapping(value = "/form/{id}")
    public String editar(@PathVariable(value = "id") Long id, Model model, RedirectAttributes flash) {

        Cliente cliente = null;

        if (id > 0) {
            cliente = iClienteService.findOne(id);

            if (cliente == null) {
                flash.addFlashAttribute("error", "El ID del Cliente no existe en la BD");
                return "redirect:/listar";

            }
        } else {
            flash.addAttribute("danger", "El ID del cliente No puede ser cero");
            return "redirect:/listar";
        }
        model.addAttribute("cliente", cliente);
        model.addAttribute("titulo", "Editar Cliente");
        return "form";
    }

    @GetMapping(value = "/delete/{id}")
    public String delete(@PathVariable(value = "id") Long id, RedirectAttributes flash) {
        if (id > 0) {
            iClienteService.delete(id);
            flash.addFlashAttribute("success", "Cliente Eliminado con Exito");
        }
        return "redirect:/listar";
    }


}
