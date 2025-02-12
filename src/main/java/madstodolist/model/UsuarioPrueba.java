package madstodolist.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "usuarios")
public class UsuarioPrueba implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true, nullable = false)
    private String email;

    private String nombre;
    private String password;

    @Column(name = "fecha_nacimiento")
    @Temporal(TemporalType.DATE)
    private Date fechaNacimiento;

    // Relación con tareas
    @OneToMany(mappedBy = "usuarioPrueba", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Tarea> tareas = new HashSet<>();

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean administrador;


    // Constructor vacío requerido por JPA
    public UsuarioPrueba() {}

    // Constructor con email (obligatorio)
    public UsuarioPrueba(String email) {
        this.email = email;
    }

    // Getters y setters básicos
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Set<Tarea> getTareas() {
        return tareas;
    }


    public Boolean getAdministrador() {
        return administrador;
    }

    public void setAdministrador(Boolean administrador) {
        this.administrador = administrador;
    }

    // Método helper para añadir una tarea a la lista y establecer la relación inversa
    public void addTarea(Tarea tarea) {
        if (tareas.contains(tarea)) return;
        tareas.add(tarea);
        tarea.setUsuario(this);
    }

    // Método helper para eliminar una tarea de la lista y romper la relación inversa
    public void removeTarea(Tarea tarea) {
        if (tareas.remove(tarea)) {
            tarea.setUsuario(null);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsuarioPrueba usuarioPrueba = (UsuarioPrueba) o;
        return id != null && id.equals(usuarioPrueba.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
