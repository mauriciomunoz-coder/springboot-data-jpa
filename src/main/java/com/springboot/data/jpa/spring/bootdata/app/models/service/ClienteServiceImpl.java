package com.springboot.data.jpa.spring.bootdata.app.models.service;


import com.springboot.data.jpa.spring.bootdata.app.models.dao.IClienteDao;
import com.springboot.data.jpa.spring.bootdata.app.models.entity.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClienteServiceImpl implements IClienteService {

    @Autowired
    private IClienteDao iClienteDao;


    @Override
    @Transactional(readOnly = true)
    public List<Cliente> findAll() {
        return iClienteDao.findAll();
    }

    @Override
    @Transactional
    public void save(Cliente cliente) {
        iClienteDao.save(cliente);
    }

    @Override
    @Transactional(readOnly = true)
    public Cliente findOne(Long id) {
        return iClienteDao.findOne(id);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        iClienteDao.delete(id);
    }
}
