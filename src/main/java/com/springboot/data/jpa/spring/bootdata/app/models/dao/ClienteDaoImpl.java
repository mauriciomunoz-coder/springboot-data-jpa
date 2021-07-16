package com.springboot.data.jpa.spring.bootdata.app.models.dao;

import com.springboot.data.jpa.spring.bootdata.app.models.entity.Cliente;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;


@Repository
public class ClienteDaoImpl implements IClienteDao {

    @PersistenceContext
    private EntityManager em; //se encarga de manejar todas las entidades y administrar CRUD a la  BD


    @Override
    @Transactional(readOnly = true)//como es una consulta se pone de solo lectura
    public List<Cliente> findAll() {
        return em.createQuery("from Cliente").getResultList();
    }
}
