package com.apirest.clientes.service;

import com.apirest.clientes.models.Cliente;

import java.util.List;
import java.util.Optional;

public interface IClienteService {

    public List<Cliente> findAll();
    public Cliente findById(long id);
    public Cliente save(Cliente cliente);
    public void deleteById(long id);


}
