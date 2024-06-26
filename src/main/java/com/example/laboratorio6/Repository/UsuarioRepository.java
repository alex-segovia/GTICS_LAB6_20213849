package com.example.laboratorio6.Repository;

import com.example.laboratorio6.Entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario,Integer> {
    public Usuario findByCorreo(String correo);
}
