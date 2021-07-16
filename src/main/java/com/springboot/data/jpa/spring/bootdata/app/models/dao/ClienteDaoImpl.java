package com.springboot.data.jpa.spring.bootdata.app.models.dao;

import com.springboot.data.jpa.spring.bootdata.app.models.entity.Cliente;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class ClienteDaoImpl implements IClienteDao{


    @Override
    public List<Cliente> findAll() {
        return null;
    }
}
