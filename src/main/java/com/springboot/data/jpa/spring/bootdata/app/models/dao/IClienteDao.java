package com.springboot.data.jpa.spring.bootdata.app.models.dao;

import com.springboot.data.jpa.spring.bootdata.app.models.entity.Cliente;

import java.util.List;

public interface IClienteDao {

    public List<Cliente> findAll();

    public void save(Cliente cliente);
}
