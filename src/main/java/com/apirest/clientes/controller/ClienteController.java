package com.apirest.clientes.controller;

import com.apirest.clientes.models.Cliente;
import com.apirest.clientes.service.ClienteServiceImpl;
import com.apirest.clientes.service.IClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ClienteController {

    @Autowired
    private IClienteService clienteService;

    @GetMapping("/clientes")
    public ResponseEntity<List<Cliente>> getAll(){
        return new ResponseEntity<>(clienteService.findAll(), HttpStatus.OK);
    }
}
