package com.springboot.data.jpa.spring.bootdata.app.models.service;

import com.springboot.data.jpa.spring.bootdata.app.models.dao.IUsuarioDao;
import com.springboot.data.jpa.spring.bootdata.app.models.entity.Role;
import com.springboot.data.jpa.spring.bootdata.app.models.entity.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service("jpaUserDetailsService")
public class JpaUserDetailsService implements UserDetailsService {

    @Autowired
    private IUsuarioDao iUsuarioDao;

    private Logger loger = LoggerFactory.getLogger(JpaUserDetailsService.class);

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Usuario usuario = iUsuarioDao.findByUsername(username);


        if (usuario == null) {
            loger.error("Errorr Login:  No esxiste el ususario" + username + "en la BD");
            throw new UsernameNotFoundException("Username " + username + " not found");
        }

        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

        for (Role role : usuario.getRoles()) {
            loger.info("Role: ".concat(role.getAuthority()));
            authorities.add(new SimpleGrantedAuthority(role.getAuthority()));
        }

        if (authorities.isEmpty()) {
            loger.error("Error Login:   el ususario" + username + "No tiene roles Asignados!");
            throw new UsernameNotFoundException("Error Login:   el ususario" + username + "No tiene roles Asignados!");
        }


        return new User(usuario.getUsername(), usuario.getPassword(), usuario.getEnabled(), true, true, true, authorities);
    }
}
