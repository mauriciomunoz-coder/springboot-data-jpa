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
   // @Transactional(readOnly = true)//como es una consulta se pone de solo lectura
    public List findAll() {
        return em.createQuery("from Cliente").getResultList();
    }


    //guarda o actualiza cliente segun si tiene id o No, si tiene id es que ya existe y solo modifica
    // si NO tiene id es porque se va a crear y lo guarda en la BD.
    @Override
    //@Transactional
    public void save(Cliente cliente) {

        if (cliente.getId() != null && cliente.getId() > 0) {
            em.merge(cliente);  //merge actualiza el objeto ya existente
        } else {
            em.persist(cliente);  //guarda un nuevo elemento a la BD
        }

    }


    //encuentra cliente por id
   // @Transactional(readOnly = true)
    @Override
    public Cliente findOne(Long id) {
        return em.find(Cliente.class, id);
    }

   // @Transactional
    @Override
    public void delete(Long id) {
        Cliente cliente = findOne(id);
        em.remove(cliente);
    }
}
