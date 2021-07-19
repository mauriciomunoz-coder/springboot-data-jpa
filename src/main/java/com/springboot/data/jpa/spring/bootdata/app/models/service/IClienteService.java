package com.springboot.data.jpa.spring.bootdata.app.models.service;

import com.springboot.data.jpa.spring.bootdata.app.models.entity.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;

public interface IClienteService {

    public List<Cliente> findAll();

    public Page<Cliente> findAll(Pageable pageable); //se usa para la paginacion

    public void save(Cliente cliente);

    public Cliente findOne(Long id);

    public void delete(Long id);
}
