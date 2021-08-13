package com.springboot.data.jpa.spring.bootdata.app;

import com.springboot.data.jpa.spring.bootdata.app.authhandler.LoginSuccesHandler;
import com.springboot.data.jpa.spring.bootdata.app.models.service.JpaUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {



    @Autowired
    private LoginSuccesHandler successHandler;


    @Autowired
    private JpaUserDetailsService userDetailsService;


    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/", "/css/**", "/js/**", "/images/**", "/listar").permitAll()
//                .antMatchers("/ver/**").hasAnyRole("USER")
//                .antMatchers("/uploads/**").hasAnyRole("USER")
//                .antMatchers("/form/**").hasAnyRole("ADMIN")
//                .antMatchers("/delete/**").hasAnyRole("ADMIN")
//                .antMatchers("/factura/**").hasAnyRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .successHandler(successHandler) //es opcional se usa para enviar el mensaje de inicio de sesion con exito configurada en la clase LoginSuccesHandler
                .loginPage("/login")
                .permitAll()
                .and()
                .logout().permitAll()
                .and()
                .exceptionHandling().accessDeniedPage("/error_403");
    }

    @Autowired
    public void configurerGlobal(AuthenticationManagerBuilder builder) throws Exception {

        builder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);

//        String passEncript = passwordEncoder.encode("Andres2205");
//        System.out.println("password encriptado:" + passEncript);

       /* PasswordEncoder encoder = passwordEncoder();
        User.UserBuilder users = User.builder().passwordEncoder(password -> {
            return encoder.encode(password);
        });

        //creamos unos ususarios con roles
       builder.inMemoryAuthentication()
                .withUser(users.username("admin").password("12345").roles("ADMIN", "USER"))
                .withUser(users.username("andres").password("12345").roles("USER"));*/
    }
}
