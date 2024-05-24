package com.example.laboratorio6.Repository;

import com.example.laboratorio6.Entity.Mesa;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MesaRepository extends JpaRepository<Mesa,Integer> {
    @Query(nativeQuery = true, value = "select capacidad from mesa where id=?1")
    int obtenerCapacidadPorId(int id);

    @Query(nativeQuery = true, value = "select * from mesa where disponibles>0")
    List<Mesa> listarMesasConDisponibilidad();

    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "update mesa set disponibles=disponibles-1 where id=?1")
    void disminuirDisponibilidad(int id);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "update mesa set disponibles=disponibles+1 where id=?1")
    void aumentarDisponibilidad(int id);
}
