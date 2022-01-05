package com.apirest.clientes.controller;

import com.apirest.clientes.models.Cliente;
import com.apirest.clientes.service.ClienteServiceImpl;
import com.apirest.clientes.service.IClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ClienteController {

    @Autowired
    private IClienteService clienteService;

    @GetMapping("/clientes")
    public ResponseEntity<List<Cliente>> getAll(){
        return new ResponseEntity<>(clienteService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/clientes/{id}")
    public ResponseEntity<Optional<Cliente>> findById(@PathVariable("id") long id){
        Optional<Cliente> cliente = clienteService.findById(id);
        if (cliente.isPresent()){
            return new ResponseEntity<>(cliente, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/clientes/save")
    public ResponseEntity<Cliente> save(@RequestBody Cliente cliente){
        return new ResponseEntity<>(clienteService.save(cliente), HttpStatus.CREATED);
    }

   @DeleteMapping("/clientes/delete/{id}")
   public void deleteById(@PathVariable("id") long id){
        clienteService.deleteById(id);
   }
}
