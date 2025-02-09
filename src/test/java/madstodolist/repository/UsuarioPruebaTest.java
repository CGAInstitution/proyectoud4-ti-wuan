package madstodolist.repository;

import madstodolist.model.UsuarioPrueba;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Sql(scripts = "/clean-db.sql")
public class UsuarioPruebaTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    //
    // Tests modelo Usuario en memoria, sin la conexión con la BD
    //

    @Test
    public void crearUsuario() throws Exception {

        // GIVEN
        // Creado un nuevo usuario,
        UsuarioPrueba usuarioPrueba = new UsuarioPrueba("juan.gutierrez@gmail.com");

        // WHEN
        // actualizamos sus propiedades usando los setters,

        usuarioPrueba.setNombre("Juan Gutiérrez");
        usuarioPrueba.setPassword("12345678");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        usuarioPrueba.setFechaNacimiento(sdf.parse("1997-02-20"));

        // THEN
        // los valores actualizados quedan guardados en el usuario y se
        // pueden recuperar con los getters.

        assertThat(usuarioPrueba.getEmail()).isEqualTo("juan.gutierrez@gmail.com");
        assertThat(usuarioPrueba.getNombre()).isEqualTo("Juan Gutiérrez");
        assertThat(usuarioPrueba.getPassword()).isEqualTo("12345678");
        assertThat(usuarioPrueba.getFechaNacimiento()).isEqualTo(sdf.parse("1997-02-20"));
    }

    @Test
    public void comprobarIgualdadUsuariosSinId() {
        // GIVEN
        // Creados tres usuarios sin identificador, y dos de ellas con
        // el mismo e-mail

        UsuarioPrueba usuarioPrueba1 = new UsuarioPrueba("juan.gutierrez@gmail.com");
        UsuarioPrueba usuarioPrueba2 = new UsuarioPrueba("juan.gutierrez@gmail.com");
        UsuarioPrueba usuarioPrueba3 = new UsuarioPrueba("ana.gutierrez@gmail.com");

        // THEN
        // son iguales (Equal) los que tienen el mismo e-mail.

        assertThat(usuarioPrueba1).isEqualTo(usuarioPrueba2);
        assertThat(usuarioPrueba1).isNotEqualTo(usuarioPrueba3);
    }


    @Test
    public void comprobarIgualdadUsuariosConId() {
        // GIVEN
        // Creadas tres usuarios con distintos e-mails y dos de ellos
        // con el mismo identificador,

        UsuarioPrueba usuarioPrueba1 = new UsuarioPrueba("juan.gutierrez@gmail.com");
        UsuarioPrueba usuarioPrueba2 = new UsuarioPrueba("pedro.gutierrez@gmail.com");
        UsuarioPrueba usuarioPrueba3 = new UsuarioPrueba("ana.gutierrez@gmail.com");

        usuarioPrueba1.setId(1L);
        usuarioPrueba2.setId(2L);
        usuarioPrueba3.setId(1L);

        // THEN
        // son iguales (Equal) los usuarios que tienen el mismo identificador.

        assertThat(usuarioPrueba1).isEqualTo(usuarioPrueba3);
        assertThat(usuarioPrueba1).isNotEqualTo(usuarioPrueba2);
    }

    //
    // Tests UsuarioRepository.
    // El código que trabaja con repositorios debe
    // estar en un entorno transactional, para que todas las peticiones
    // estén en la misma conexión a la base de datos, las entidades estén
    // conectadas y sea posible acceder a colecciones LAZY.
    //

    @Test
    @Transactional
    public void crearUsuarioBaseDatos() throws ParseException {
        // GIVEN
        // Un usuario nuevo creado sin identificador

        UsuarioPrueba usuarioPrueba = new UsuarioPrueba("juan.gutierrez@gmail.com");
        usuarioPrueba.setNombre("Juan Gutiérrez");
        usuarioPrueba.setPassword("12345678");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        usuarioPrueba.setFechaNacimiento(sdf.parse("1997-02-20"));

        // WHEN
        // se guarda en la base de datos

        usuarioRepository.save(usuarioPrueba);

        // THEN
        // se actualiza el identificador del usuario,

        assertThat(usuarioPrueba.getId()).isNotNull();

        // y con ese identificador se recupera de la base de datos el usuario con
        // los valores correctos de las propiedades.

        UsuarioPrueba usuarioPruebaBD = usuarioRepository.findById(usuarioPrueba.getId()).orElse(null);
        assertThat(usuarioPruebaBD.getEmail()).isEqualTo("juan.gutierrez@gmail.com");
        assertThat(usuarioPruebaBD.getNombre()).isEqualTo("Juan Gutiérrez");
        assertThat(usuarioPruebaBD.getPassword()).isEqualTo("12345678");
        assertThat(usuarioPruebaBD.getFechaNacimiento()).isEqualTo(sdf.parse("1997-02-20"));
    }

    @Test
    @Transactional
    public void buscarUsuarioEnBaseDatos() {
        // GIVEN
        // Un usuario en la BD
        UsuarioPrueba usuarioPrueba = new UsuarioPrueba("user@ua");
        usuarioPrueba.setNombre("Usuario Ejemplo");
        usuarioRepository.save(usuarioPrueba);
        Long usuarioId = usuarioPrueba.getId();

        // WHEN
        // se recupera de la base de datos un usuario por su identificador,

        UsuarioPrueba usuarioPruebaBD = usuarioRepository.findById(usuarioId).orElse(null);

        // THEN
        // se obtiene el usuario correcto y se recuperan sus propiedades.

        assertThat(usuarioPruebaBD).isNotNull();
        assertThat(usuarioPruebaBD.getId()).isEqualTo(usuarioId);
        assertThat(usuarioPruebaBD.getNombre()).isEqualTo("Usuario Ejemplo");
    }

    @Test
    @Transactional
    public void buscarUsuarioPorEmail() {
        // GIVEN
        // Un usuario en la BD
        UsuarioPrueba usuario = new UsuarioPrueba("user@ua");
        usuario.setNombre("Usuario Ejemplo");
        usuarioRepository.save(usuario);

        // WHEN
        // buscamos al usuario por su correo electrónico,

        UsuarioPrueba usuarioPruebaBD = usuarioRepository.findByEmail("user@ua").orElse(null);

        // THEN
        // se obtiene el usuario correcto.

        assertThat(usuarioPruebaBD.getNombre()).isEqualTo("Usuario Ejemplo");
    }
}