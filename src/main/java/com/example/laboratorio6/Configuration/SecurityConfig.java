package com.example.laboratorio6.Configuration;

import com.example.laboratorio6.Repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig {
    final DataSource dataSource;
    final UsuarioRepository usuarioRepository;

    public SecurityConfig(DataSource dataSource,
                          UsuarioRepository usuarioRepository){
        this.dataSource = dataSource;
        this.usuarioRepository = usuarioRepository;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsManager users(DataSource dataSource){
        JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
        String sql1 = "select correo, password, estado from usuario where correo = ?";
        String sql2 = "select u.correo, r.rol from usuario u inner join rol r on (u.idrol = r.id) where u.correo = ?";
        users.setUsersByUsernameQuery(sql1);
        users.setAuthoritiesByUsernameQuery(sql2);
        return users;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.formLogin()
                .loginPage("/")
                .loginProcessingUrl("/loginSuccessful")
                .usernameParameter("correo")
                .passwordParameter("password")
                .successHandler(((request, response, authentication) -> {
                    DefaultSavedRequest defaultSavedRequest = (DefaultSavedRequest) request.getSession().getAttribute("SPRING_SECURITY_SAVED_REQUEST");

                    HttpSession session = request.getSession();
                    session.setAttribute("usuario",usuarioRepository.findByCorreo(authentication.getName()));

                    if(defaultSavedRequest!=null){
                        String targetURL = defaultSavedRequest.getRedirectUrl();
                        new DefaultRedirectStrategy().sendRedirect(request,response,targetURL);
                    }else{
                        response.sendRedirect("/mesas");
                    }
                }));

        http.logout()
                .logoutSuccessUrl("/mesas")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true);

        http.authorizeHttpRequests()
                .requestMatchers("/mesas/*").hasAnyAuthority("ADMIN")
                .requestMatchers("/reservas","/reservas/**").hasAnyAuthority("GERENTE","CLIENTE")
                .anyRequest().permitAll();

        return http.build();
    }
}
