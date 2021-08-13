package com.springboot.data.jpa.spring.bootdata.app.authhandler;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.support.SessionFlashMapManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class LoginSuccesHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        SessionFlashMapManager flashMapManager = new SessionFlashMapManager();

        FlashMap flashMap = new FlashMap();

        flashMap.put("success","Hola" + authentication.getName() +  "Has iniciado session con Exito!");

        flashMapManager.saveOutputFlashMap(flashMap, request, response);

        if (authentication != null){
            logger.info("El usuario'" + authentication.getName() + "'ha iniciado sesion con exito");
        }

        super.onAuthenticationSuccess(request, response, authentication); //para el codigo de encontrar el nombre del usuario del controlador
    }
}
