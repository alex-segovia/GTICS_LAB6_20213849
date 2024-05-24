package com.example.laboratorio6.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "mesa")
public class Mesa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(min=1, max = 45,message = "El nombre debe tener como mínimo un caracter y como máximo 45 caracteres")
    @NotBlank(message = "Debes ingresar un nombre")
    @Column(name = "nombre", nullable = false, length = 45)
    private String nombre;

    @NotNull(message = "Debes ingresar una cantidad")
    @Digits(integer = 10,fraction = 0,message = "El número debe ser entero")
    @Min(value = 1,message = "La capacidad debe ser como mínimo 1")
    @Column(name = "capacidad", nullable = false)
    private Integer capacidad;

    @Size(min=1,max = 45,message = "La ubicación debe tener como mínimo un caracter y como máximo 45 caracteres")
    @NotBlank(message = "Debes ingresar una ubicación")
    @Column(name = "ubicacion", nullable = false, length = 45)
    private String ubicacion;

    @Column(name = "disponibles", nullable = false)
    private Integer disponibles;

}
