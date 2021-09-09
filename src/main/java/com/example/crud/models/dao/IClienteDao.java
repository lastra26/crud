package com.example.crud.models.dao;

import com.example.crud.models.entity.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface IClienteDao extends CrudRepository<Cliente, Long> {


}
