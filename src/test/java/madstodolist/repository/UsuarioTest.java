package madstodolist.repository;

import madstodolist.model.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UsuarioTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    //
    // Tests modelo Usuario en memoria, sin conexión a la BD
    //

    @Test
    public void crearUsuario() throws Exception {
        // GIVEN: Creación de usuario
        Usuario usuario = new Usuario("juan.gutierrez@gmail.com");

        // WHEN: Se actualizan sus propiedades
        usuario.setNombre("Juan Gutiérrez");
        usuario.setPassword("12345678");
        usuario.setAdministrador(false);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        usuario.setFechaNacimiento(sdf.parse("1997-02-20"));

        // THEN: Se verifican los valores guardados
        assertThat(usuario.getEmail()).isEqualTo("juan.gutierrez@gmail.com");
        assertThat(usuario.getNombre()).isEqualTo("Juan Gutiérrez");
        assertThat(usuario.getPassword()).isEqualTo("12345678");
        assertThat(usuario.getFechaNacimiento()).isEqualTo(sdf.parse("1997-02-20"));
        assertThat(usuario.getAdministrador()).isFalse();
    }

    @Test
    public void comprobarIgualdadUsuariosSinId() {
        // GIVEN: Creación de usuarios sin ID
        Usuario usuario1 = new Usuario("juan.gutierrez@gmail.com");
        usuario1.setAdministrador(false);
        Usuario usuario2 = new Usuario("juan.gutierrez@gmail.com");
        usuario2.setAdministrador(false);
        Usuario usuario3 = new Usuario("ana.gutierrez@gmail.com");
        usuario3.setAdministrador(false);

        // THEN: Son iguales si tienen el mismo email
        assertThat(usuario1).isEqualTo(usuario2);
        assertThat(usuario1).isNotEqualTo(usuario3);
    }

    @Test
    public void comprobarIgualdadUsuariosConId() {
        // GIVEN: Creación de usuarios con ID
        Usuario usuario1 = new Usuario("juan.gutierrez@gmail.com");
        Usuario usuario2 = new Usuario("pedro.gutierrez@gmail.com");
        Usuario usuario3 = new Usuario("ana.gutierrez@gmail.com");

        usuario1.setId(1L);
        usuario2.setId(2L);
        usuario3.setId(1L);

        // THEN: Son iguales si tienen el mismo ID
        assertThat(usuario1).isEqualTo(usuario3);
        assertThat(usuario1).isNotEqualTo(usuario2);
    }

    //
    // Tests con UsuarioRepository y base de datos
    //

    @Test
    @Transactional
    public void crearUsuarioBaseDatos() throws ParseException {
        // GIVEN: Un usuario nuevo
        Usuario usuario = new Usuario("juan.gutierrez@gmail.com");
        usuario.setNombre("Juan Gutiérrez");
        usuario.setPassword("12345678");
        usuario.setAdministrador(false);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        usuario.setFechaNacimiento(sdf.parse("1997-02-20"));

        // WHEN: Se guarda en la base de datos
        usuarioRepository.save(usuario);

        // THEN: Se actualiza el ID y los valores son correctos
        assertThat(usuario.getId()).isNotNull();
        Usuario usuarioBD = usuarioRepository.findById(usuario.getId()).orElse(null);
        assertThat(usuarioBD).isNotNull();
        assertThat(usuarioBD.getEmail()).isEqualTo("juan.gutierrez@gmail.com");
        assertThat(usuarioBD.getNombre()).isEqualTo("Juan Gutiérrez");
        assertThat(usuarioBD.getPassword()).isEqualTo("12345678");
        assertThat(usuarioBD.getFechaNacimiento()).isEqualTo(sdf.parse("1997-02-20"));
        assertThat(usuarioBD.getAdministrador()).isFalse();
    }

    @Test
    @Transactional
    public void buscarUsuarioEnBaseDatos() {
        // GIVEN: Un usuario guardado en la BD
        Usuario usuario = new Usuario("user@ua");
        usuario.setNombre("Usuario Ejemplo");
        usuario.setPassword("12345");
        usuario.setAdministrador(false);
        usuarioRepository.save(usuario);
        Long usuarioId = usuario.getId();

        // WHEN: Se recupera por ID
        Optional<Usuario> usuarioBD = usuarioRepository.findById(usuarioId);

        // THEN: Se verifica que el usuario existe
        assertThat(usuarioBD).isPresent();
        assertThat(usuarioBD.get().getId()).isEqualTo(usuarioId);
        assertThat(usuarioBD.get().getNombre()).isEqualTo("Usuario Ejemplo");
        assertThat(usuarioBD.get().getAdministrador()).isFalse();
    }

    @Test
    @Transactional
    public void buscarUsuarioPorEmail() {
        // GIVEN: Un usuario guardado en la BD
        Usuario usuario = new Usuario("user@ua");
        usuario.setNombre("Usuario Ejemplo");
        usuario.setPassword("12345");
        usuario.setAdministrador(false);
        usuarioRepository.save(usuario);

        // WHEN: Se busca por email
        Usuario usuarioBD = usuarioRepository.findByEmail("user@ua").orElse(null);

        // THEN: Se encuentra el usuario correcto
        assertThat(usuarioBD).isNotNull();
        assertThat(usuarioBD.getNombre()).isEqualTo("Usuario Ejemplo");
        assertThat(usuarioBD.getAdministrador()).isFalse();
    }
}
