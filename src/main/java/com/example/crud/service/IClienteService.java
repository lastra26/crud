package com.example.crud.service;

import com.example.crud.models.entity.Cliente;
import org.springframework.stereotype.Service;

import java.util.List;

public interface IClienteService {

    public List<Cliente> findAll();

    public void save(Cliente cliente);

    public Cliente finOne(Long id);

    public void delete(Long id);

}
