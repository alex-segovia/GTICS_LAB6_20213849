package com.example.laboratorio6.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "reserva")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotBlank(message = "Debes ingresar una fecha de inicio")
    @Column(name = "fechainicio", nullable = false)
    private String fechainicio;

    @NotBlank(message = "Debes ingresar una fecha de fin")
    @Column(name = "fechafin", nullable = false)
    private String fechafin;

    @ManyToOne
    @JoinColumn(name = "idusuario", nullable = false)
    private Usuario usuario;

    @NotNull(message = "Debes seleccionar una mesa")
    @ManyToOne
    @JoinColumn(name = "idmesa", nullable = false)
    private Mesa mesa;

}
