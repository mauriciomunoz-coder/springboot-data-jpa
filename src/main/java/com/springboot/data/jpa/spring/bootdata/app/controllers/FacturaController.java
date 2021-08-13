package com.springboot.data.jpa.spring.bootdata.app.controllers;

import com.springboot.data.jpa.spring.bootdata.app.models.entity.Cliente;
import com.springboot.data.jpa.spring.bootdata.app.models.entity.Factura;
import com.springboot.data.jpa.spring.bootdata.app.models.entity.ItemFactura;
import com.springboot.data.jpa.spring.bootdata.app.models.entity.Producto;
import com.springboot.data.jpa.spring.bootdata.app.models.service.IClienteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Secured("ROLE_ADMIN") //solo el ROL_ADMIN podra acceder a estos metodos
@Controller
@RequestMapping("/factura")
@SessionAttributes("factura") //el nombre con el que se pasa a la vista
public class FacturaController {

    @Autowired
    IClienteService iClienteService;

    private final Logger log = LoggerFactory.getLogger(getClass());

    @GetMapping("/form/{clienteId}")
    public String crear(@PathVariable(value = "clienteId") Long clienteId, Model model, RedirectAttributes flash) {

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
        model.addAttribute("titulo", "Crear Factura");

        return "factura/form";
    }

    @GetMapping(value = "/cargar-productos/{term}", produces = {"application/json"})
    public @ResponseBody
    List<Producto> cargarProductos(@PathVariable String term) {
        return iClienteService.findByNombre(term);
    }


    @PostMapping("/form")
    public String guardar(@Valid Factura factura, BindingResult result, Model model,
                          @RequestParam(name = "item_id[]", required = false) Long[] itemId,
                          @RequestParam(name = "cantidad[]", required = false) Integer[] cantidad,
                          RedirectAttributes flash,
                          SessionStatus status) {

        if (result.hasErrors()) {
            model.addAttribute("titulo", "crear factura");
            return "factura/form";
        }

        if (itemId == null || itemId.length == 0) {
            model.addAttribute("titulo", "crear factura");
            model.addAttribute("error", "la factura debe contener productos agregados!");
            return "factura/form";
        }

        for (int i = 0; i < itemId.length; i++) {
            Producto producto = iClienteService.findProductoById(itemId[i]);

            ItemFactura linea = new ItemFactura();
            linea.setCantidad(cantidad[i]);
            linea.setProducto(producto);
            factura.addItemFactura(linea);

            log.info("ID: " + itemId[i].toString() + ", cantidad: " + cantidad[i].toString());
        }

        iClienteService.saveFactura(factura);
        status.setComplete();

        flash.addFlashAttribute("success", "Factura creada con éxito!");

        return "redirect:/ver/" + factura.getCliente().getId();
    }

    @GetMapping("/ver/{id}")
    public String ver(@PathVariable Long id, Model model, RedirectAttributes flash) {


        Factura factura = iClienteService.findFacturaById(id);

        if (factura == null) {
            flash.addFlashAttribute("error", "La Factura No existe en la base de datos");
            return "redirect:/listar";
        }

        model.addAttribute("factura", factura);
        model.addAttribute("titulo", "Factura:".concat(factura.getDescripcion()));
        return "factura/ver";
    }


    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes flash) {
        Factura factura = iClienteService.findFacturaById(id);

        if (factura != null) {
            iClienteService.deleteFactura(id);
            flash.addFlashAttribute("success", "Factura eliminada con exito!");
            return "redirect:/ver/" + factura.getCliente().getId();

        }
        flash.addFlashAttribute("error", "La factura No existe en la base de datos");
        return "redirect:/listar";
    }


}
