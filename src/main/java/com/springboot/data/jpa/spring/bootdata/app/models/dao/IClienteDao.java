package com.springboot.data.jpa.spring.bootdata.app.models.dao;

import com.springboot.data.jpa.spring.bootdata.app.models.entity.Cliente;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

//se usa PagingAndSortingRepository<Cliente, Long>  para usar la paginacion
//si no necesitamos paginacion usamos CrudRepository<Cliente, Long>

public interface IClienteDao extends PagingAndSortingRepository<Cliente, Long> {


}
