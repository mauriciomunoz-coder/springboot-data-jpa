package com.springboot.data.jpa.spring.bootdata.app.models.entity;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "clientes")
public class Cliente implements Serializable {

    @Id //indica que es la llave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@Column(name = "nombres")  //cambia el nombre en la tabla de BD

    @NotEmpty
    @Size(min = 4, max = 20) //valida que el nombre este entre 4 a 12 caracteres
    private String nombre;

    @NotEmpty
    private String apellido;

    @NotEmpty
    @Email
    private String email;

    @NotNull
    @Column(name = "create_at")  //cambia el nombre en la tabla de BD
    //@Temporal(TemporalType.DATE)//indica el formato en el que se va a guardar la fecha en la BD
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createAt;


    /*@PrePersist
    public void prePersisit(){
        createAt = new Date();
    }*/

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    private static final long serialVersionUID = 1L;


}
