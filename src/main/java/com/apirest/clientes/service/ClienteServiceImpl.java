package com.apirest.clientes.service;

import com.apirest.clientes.models.Cliente;
import com.apirest.clientes.repository.IClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ClienteServiceImpl implements IClienteService{

    @Autowired
    private IClienteRepository iclienterepo;

    @Override
    public List<Cliente> findAll() {
        return (List<Cliente>) iclienterepo.findAll();
    }

    @Override
    public Page<Cliente> findAll(Pageable pageable) {
        return iclienterepo.findAll(pageable);
    }

    @Override
    public Cliente findById(long id) {
        return iclienterepo.findById(id).orElse(null);
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
