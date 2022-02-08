package com.apirest.clientes.models;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "cliente")
public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id_cliente;

    @Column(nullable = false)
    @NotEmpty(message = "es obligatorio")
    @Size(min=4, max=12, message = "4 caracteres como mínimo")
    private String nombre;

    @NotEmpty(message = "es obligatorio")
    private String apellido;

    @NotEmpty(message = "es obligatorio")
    @Email(message = "formato de correo invalido")
    @Column(nullable = false, unique = true)
    private String email;

    @NotNull(message = "es obligatorio")
    @Column(name = "create_at")
    private LocalDate createAt;

    private String foto;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public long getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(long id_cliente) {
        this.id_cliente = id_cliente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDate createAt) {
        this.createAt = createAt;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
