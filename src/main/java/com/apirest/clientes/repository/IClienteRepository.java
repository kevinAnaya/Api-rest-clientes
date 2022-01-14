package com.apirest.clientes.repository;

import com.apirest.clientes.models.Cliente;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IClienteRepository extends CrudRepository<Cliente, Long> {

}
