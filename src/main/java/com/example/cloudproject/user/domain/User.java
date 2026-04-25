package com.example.cloudproject.user.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "clientes")  // ← cambiar "users" por "clientes"
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false)  // ← agregar name
    private String name;

    @Column(name = "email", nullable = false, unique = true)  // ← agregar name
    private String email;

    @Column(name = "telefono")  // ← agregar
    private String password;   // ← puedes renombrar a "phone" si quieres

    @Column(name = "ciudad")
    private String ciudad;

    @Column(name = "pais")
    private String pais;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "fecha_registro")
    private String fechaRegistro;
}