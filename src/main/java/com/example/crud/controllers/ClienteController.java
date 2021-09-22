package com.example.crud.controllers;

import com.example.crud.models.dao.IClienteDao;
import com.example.crud.models.entity.Cliente;
import com.example.crud.service.IClienteService;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

@Controller
@SessionAttributes("cliente")
//es mejor usar un Session attribute es mucho mas seguro podemos guardar el objeto del formulario
public class ClienteController {

    @Autowired
    private IClienteService clienteService;

    @RequestMapping(value = "/listar", method = RequestMethod.GET)
    public String listar(Model model) {
        model.addAttribute("titulo", "Listado de Clientes");
        model.addAttribute("clientes", clienteService.findAll());
        return "listar";
    }

    @RequestMapping(value = "/form")
    public String crear(Map<String, Object> model) {
        Cliente cliente = new Cliente();
        model.put("cliente", cliente);
        model.put("titulo", "Formulario de cliente");

        return "form";
    }

    @RequestMapping(value = "/index")
    public String index(Model model) {
        return "/index";
    }

    @RequestMapping(value = "/form/{id}")
    public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {
        Cliente cliente = null;

        if (id > 0) {
            cliente = clienteService.finOne(id);
            if (cliente == null) {
                flash.addFlashAttribute("error", "El id no existe en la base de datos");
                return "redirect:/listar";
            }
        } else {
            flash.addFlashAttribute("error", "El Id del cliente no puede ser 0");
            return "redirect:/listar";
        }
        model.put("cliente", cliente);
        model.put("titulo", "editar cliente");
        return "form";
    }


    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String guardar(@Valid Cliente cliente, BindingResult result, Model model, RedirectAttributes flash, SessionStatus sessionStatus) { //aqui guardamos el objeto del dao
        //para el @Valid siempre va de la mano el objeto que esta mapeado y despues el binding result
        if (result.hasErrors()) {
            model.addAttribute("titulo", "formulario de cliente");
            return "form";
        }
        Date fechaActual = new Date();
        System.out.println("fechaActual = " + fechaActual);
        Date fechaFormulario = cliente.getCreateAt();
        System.out.println("fecha Formulario = " + fechaFormulario);

        if (fechaFormulario.before(fechaActual)) {
            System.out.println("si funciona");
            String mensajeFlash = (cliente.getId() != null) ? "Cliente editado con éxito" : "Cliente creado con éxito";
            clienteService.save(cliente); //recibe los datos mapeados de cliente
            sessionStatus.setComplete();
            flash.addFlashAttribute("success", mensajeFlash);
            return "redirect:listar";
        } else if (fechaFormulario.after(fechaActual)) {
            System.out.println("no funciona");
            model.addAttribute("titulo", "formulario de cliente");
            String mensajeFlash = (fechaFormulario != fechaActual) ? "Fecha debe ser Menor a la Actual"
                    : "Fecha debe ser Menor a la Actual";
            flash.addFlashAttribute("error", mensajeFlash);
            return "redirect:form";
        }
        return "redirect:listar";
    }

    @RequestMapping(value = "/eliminar/{id}")
    public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash) {
        if (id > 0) {
            clienteService.delete(id);
            flash.addFlashAttribute("success", "Eliminado Con éxito");
        }
        return "redirect:/listar";
    }
}

























