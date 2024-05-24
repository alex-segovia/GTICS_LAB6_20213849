package com.example.laboratorio6.Repository;

import com.example.laboratorio6.Entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva,Integer> {
    @Query(nativeQuery = true,value = "select * from reserva where idusuario=?1")
    List<Reserva> listarReservasPorCliente(int id);

    @Query(nativeQuery = true,value = "select * from reserva where idmesa=?1")
    List<Reserva> listarReservasPorMesa(int id);
}
