package com.apirest.clientes.service;

import com.apirest.clientes.models.Cliente;

import java.util.List;

public interface IClienteService {

    public List<Cliente> findAll();
}
