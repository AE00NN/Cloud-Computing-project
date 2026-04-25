package com.example.cloudproject.catalog.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "libros")
public class Book {

    @Id
    private Long id;  // ← quitar @GeneratedValue, la BD ya tiene los IDs

    @JsonProperty("titulo")
    @Column(name = "titulo")
    private String title;

    @JsonProperty("autor_id")
    @Column(name = "autor_id")
    private Long authorId;   // ← authorId no autorId

    @JsonProperty("genero_id")
    @Column(name = "genero_id")
    private Long genreId;    // ← genreId no generoId

    @JsonProperty("editorial_id")
    @Column(name = "editorial_id")
    private Long editorialId;  // ← igual

    @Column(name = "isbn")
    private String isbn;

    @JsonProperty("precio")
    @Column(name = "precio")
    private Double price;

    @Column(name = "stock")
    private Integer stock;

    @JsonProperty("año_publicacion")
    @Column(name = "año_publicacion")
    private Integer anoPublicacion;

    @Column(name = "paginas")
    private Integer paginas;

    @Column(name = "idioma")
    private String idioma;
}