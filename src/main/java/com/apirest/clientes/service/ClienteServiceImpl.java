package com.apirest.clientes.service;

import com.apirest.clientes.models.Cliente;
import com.apirest.clientes.repository.IClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteServiceImpl implements IClienteService{

    @Autowired
    private IClienteRepository iclienterepo;

    @Override
    public List<Cliente> findAll() {
        return (List<Cliente>) iclienterepo.findAll();
    }
}
