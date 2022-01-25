package com.apirest.clientes.service;

import com.apirest.clientes.models.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IClienteService {

    public List<Cliente> findAll();
    public Page<Cliente> findAll(Pageable pageable);
    public Cliente findById(long id);
    public Cliente save(Cliente cliente);
    public void deleteById(long id);


}
