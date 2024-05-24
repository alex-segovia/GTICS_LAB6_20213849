package com.example.laboratorio6.Repository;

import com.example.laboratorio6.Entity.Mesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MesaRepository extends JpaRepository<Mesa,Integer> {
    @Query(nativeQuery = true, value = "select capacidad from mesa where id=?1")
    int obtenerCapacidadPorId(int id);
}
