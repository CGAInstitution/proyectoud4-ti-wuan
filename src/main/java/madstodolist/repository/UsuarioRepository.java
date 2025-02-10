package madstodolist.repository;

import madstodolist.model.UsuarioPrueba;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UsuarioRepository extends CrudRepository<UsuarioPrueba, Long> {
    Optional<UsuarioPrueba> findByEmail(String s);
}
