package com.springboot.data.jpa.spring.bootdata.app.models.dao;

import com.springboot.data.jpa.spring.bootdata.app.models.entity.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface IUsuarioDao extends CrudRepository<Usuario, Long> {


    @Query("select u from Usuario u where u.username=?1")
    public Usuario findByUsername(String username);
}
