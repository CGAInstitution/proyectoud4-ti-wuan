package madstodolist.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "tareas")
public class Tarea implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String titulo;

    @NotNull
    // Relación muchos-a-uno entre tareas y usuario
    @ManyToOne
    // Nombre de la columna en la BD que guarda físicamente
    // el ID del usuario con el que está asociada una tarea
    @JoinColumn(name = "usuario_id")
    private UsuarioPrueba usuarioPrueba;

    // Constructor vacío necesario para JPA/Hibernate.
    public Tarea() {}

    // Constructor para asociar una tarea a un usuario
    public Tarea(UsuarioPrueba usuarioPrueba, String titulo) {
        this.titulo = titulo;
        setUsuario(usuarioPrueba); // Esto añadirá la tarea a la lista de tareas del usuario
    }

    // Getters y setters básicos

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    // Getters y setters de la relación muchos-a-uno con Usuario

    public UsuarioPrueba getUsuario() {
        return usuarioPrueba;
    }

    public void setUsuario(UsuarioPrueba usuarioPrueba) {
        if(this.usuarioPrueba != usuarioPrueba) {
            this.usuarioPrueba = usuarioPrueba;
            usuarioPrueba.addTarea(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tarea tarea = (Tarea) o;
        if (id != null && tarea.id != null)
            return Objects.equals(id, tarea.id);
        return titulo.equals(tarea.titulo) && usuarioPrueba.equals(tarea.usuarioPrueba);
    }

    @Override
    public int hashCode() {
        return Objects.hash(titulo, usuarioPrueba);
    }
}

