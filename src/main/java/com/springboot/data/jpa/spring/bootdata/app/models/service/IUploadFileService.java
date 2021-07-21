package com.springboot.data.jpa.spring.bootdata.app.models.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;

public interface IUploadFileService {

    public Resource load(String filename) throws MalformedURLException; //muestra la imagen

    public String copy(MultipartFile file) throws IOException; //regresa el nombre de la nueva imagen

    public boolean delete(String filename); //regresa true o false para saber si elimino o no la imagen

    public void deleteAll();

    public void init() throws IOException;


}
