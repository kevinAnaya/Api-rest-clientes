package com.apirest.clientes.service;

import com.apirest.clientes.models.Cliente;
import com.apirest.clientes.repository.IClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteServiceImpl implements IClienteService{

    @Autowired
    private IClienteRepository iclienterepo;

    @Override
    public List<Cliente> findAll() {
        return (List<Cliente>) iclienterepo.findAll();
    }

    @Override
    public Optional<Cliente> findById(long id) {
        return iclienterepo.findById(id);
    }

    @Override
    public Cliente save(Cliente cliente) {
        return iclienterepo.save(cliente);
    }

    @Override
    public void deleteById(long id) {
        iclienterepo.deleteById(id);
    }


}
