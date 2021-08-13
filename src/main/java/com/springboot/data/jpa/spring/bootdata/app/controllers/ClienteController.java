package com.springboot.data.jpa.spring.bootdata.app.controllers;


import com.springboot.data.jpa.spring.bootdata.app.models.entity.Cliente;
import com.springboot.data.jpa.spring.bootdata.app.models.service.IClienteService;
import com.springboot.data.jpa.spring.bootdata.app.models.service.IUploadFileService;
import com.springboot.data.jpa.spring.bootdata.app.util.paginator.PageRender;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
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


    // inyectamos la dependencia de iuploadFileService
    @Autowired
    private IUploadFileService iuploadFileService;

    //se usa para encontrar el usuario logueado
    protected final Log logger = LogFactory.getLog(this.getClass());


    //**** este es un metodo alternativo para ver la imagen sin tener que usar la clase MvcConfig.java ***********************

    @Secured("ROLE_USER")
    @GetMapping("/uploads/{filename:.+}") //es la misma ruta que tiene el campo para img de la vista ver
    public ResponseEntity<Resource> verFoto(@PathVariable String filename) {

        Resource recurso = null;

        try {
            recurso = iuploadFileService.load(filename);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"").body(recurso);
    }

    //****************************************************** fin  codigo *****************************************************

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
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
    @GetMapping(value = {"/listar", "/"})
    public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model, Authentication authentication) {

        // ***********************  encontrar el nombre del usuario autenticado  ***************************
        if (authentication != null) {
            logger.info("Hola usuario autenticado tu usuario es: ".concat(authentication.getName()));
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            logger.info("Utilizando forma estatica Hola usuario autenticado tu usuario es: ".concat(auth.getName()));
        }
        //***************** fin codigo usuario autenticado *******************

        //**************** codigo de paginacion *************************
        Pageable pageRequest = PageRequest.of(page, 4);
        Page<Cliente> clientes = iClienteService.findAll(pageRequest);
        PageRender<Cliente> pageRender = new PageRender<>("/listar", clientes);

        //******************************** fin codigo de paginacion  *******************************
        model.addAttribute("titulo", "listado de clientes");
        model.addAttribute("clientes", clientes);
        model.addAttribute("page", pageRender);
        //model.addAttribute("clientes", iClienteService.findAll()); //se usa sin paginacion
        return "listar";
    }


    //redirige al formulario
    @Secured("ROLE_ADMIN")
    @GetMapping(value = "/form")
    public String crear(Map<String, Object> model) {
        Cliente cliente = new Cliente();
        model.put("cliente", cliente);  //se pone para mapear el objeto al formulario
        model.put("titulo", "Formulario de Cliente");

        return "form";
    }

    private final Logger log = LoggerFactory.getLogger(getClass());

    //guarda o modifica un cliente dependiendo de si tiene id o No, "revisar ClienteDaoImpl.java"
    @Secured("ROLE_ADMIN")
    @PostMapping(value = "/guardar")
    public String guardar(@Valid Cliente cliente, BindingResult result, Model model, @RequestParam("file") MultipartFile foto, RedirectAttributes flash, SessionStatus status) {
        if (result.hasErrors()) {
            model.addAttribute("titulo", "Formulario de Cliente");
            return "form";
        }


        //******************** codigo para subir imagenes *******************************************************************

        if (!foto.isEmpty()) {
            // codigo que elimina la foto anterior cuando se cambia la foto o elimina el cliente  **********************************************************
            if (cliente.getId() != null && cliente.getId() > 0 && cliente.getFoto() != null && cliente.getFoto().length() > 0) {

                iuploadFileService.delete(cliente.getFoto());
            }

            String uniqueFilename = null;

            try {
                uniqueFilename = iuploadFileService.copy(foto);
            } catch (IOException e) {
                e.printStackTrace();
            }

            flash.addFlashAttribute("info", "Has subido correctamente '" + uniqueFilename + "'");

            cliente.setFoto(uniqueFilename);
            //********* fin codigo que elimina la imagen cuando se cambia la foto o elimina el cliente*************************************************

        }
        //********************************** fin codigo para cargar foto o imagen ********************************************************************************************************
        String mensajeFlash = (cliente.getId() != null) ? "Cliente editado con éxito!" : "Cliente creado con éxito!"; //verifica si el cliente ya existe o es nuevo y dependiendo saca el mensaje


        iClienteService.save(cliente);
        status.setComplete();
        flash.addFlashAttribute("success", mensajeFlash);
        return "redirect:listar";
    }


    //redirige al formulario con el cliente encontrado para mostrarlo en los campos
    @Secured("ROLE_ADMIN")
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

    @Secured("ROLE_ADMIN")
    @GetMapping(value = "/delete/{id}")
    public String delete(@PathVariable(value = "id") Long id, RedirectAttributes flash) {
        if (id > 0) {

            Cliente cliente = iClienteService.findOne(id);
            iClienteService.delete(id);
            flash.addFlashAttribute("success", "Cliente Eliminado con Exito");

            //***** codigo para eliminar la foto cuando se cambie la imagen o se elimine el cliente **************************

            if (iuploadFileService.delete(cliente.getFoto())) {
                flash.addFlashAttribute("info", "Foto " + cliente.getFoto() + "eliminada con exito!");
            }
            // ******************************* fin del codigo eliminar imagen ************************************************
        }

        return "redirect:/listar";
    }


}
