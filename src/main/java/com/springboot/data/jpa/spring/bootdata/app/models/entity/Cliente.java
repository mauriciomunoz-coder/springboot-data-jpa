package com.springboot.data.jpa.spring.bootdata.app.models.entity;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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


    private String foto;


    @OneToMany(mappedBy = "cliente",fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    private List<Factura> facturas;

    public Cliente() {
        facturas = new ArrayList<Factura>();
    }


    // agrega la fecha actual sin necesidad de poner el campo en el formulario
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


    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public List<Factura> getFacturas() {
        return facturas;
    }

    public void setFacturas(List<Factura> facturas) {
        this.facturas = facturas;
    }

    //guarda las facturas es opcional pero recomendado
    public void addFactura(Factura factura){
        facturas.add(factura);
    }

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return "Cliente{" +
                "nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                '}';
    }
}
