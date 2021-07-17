package com.springboot.data.jpa.spring.bootdata.app.models.dao;

import com.springboot.data.jpa.spring.bootdata.app.models.entity.Cliente;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IClienteDao extends CrudRepository<Cliente, Long> {


//CrudRepository ya trae estos metodos implementados

//    public List<Cliente> findAll();
//
//    public void save(Cliente cliente);
//
//    public Cliente findOne(Long id);
//
//    public void delete(Long id);


}
