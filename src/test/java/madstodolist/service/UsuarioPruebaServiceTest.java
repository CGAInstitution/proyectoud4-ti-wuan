package madstodolist.service;

import madstodolist.dto.UsuarioData;
import madstodolist.model.Usuario;
import madstodolist.repository.UsuarioRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class UsuarioPruebaServiceTest {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Long usuarioTestId;  // Para almacenar el ID del usuario de prueba creado

    @BeforeEach
    void setUp() {
        // Verificar si el usuario "user@ua" ya existe antes de crearlo
        Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail("user@ua");
        if (usuarioExistente.isEmpty()) {
            UsuarioData usuario = new UsuarioData();
            usuario.setEmail("user@ua");
            usuario.setNombre("Usuario Ejemplo");
            usuario.setPassword("123");
            usuario.setAdministrador(true);
            UsuarioData nuevoUsuario = usuarioService.registrar(usuario);
            usuarioTestId = nuevoUsuario.getId();
        } else {
            usuarioTestId = usuarioExistente.get().getId();
        }
    }

    @AfterEach
    void tearDown() {
        // Eliminar solo el usuario de prueba si existe
        if (usuarioTestId != null) {
            usuarioRepository.deleteById(usuarioTestId);
        }
    }

    @Test
    public void testServicioLoginUsuario() {
        // WHEN
        UsuarioService.LoginStatus loginStatus1 = usuarioService.login("user@ua", "123");
        UsuarioService.LoginStatus loginStatus2 = usuarioService.login("user@ua", "000");
        UsuarioService.LoginStatus loginStatus3 = usuarioService.login("pepito.perez@gmail.com", "12345678");

        // THEN
        assertEquals(UsuarioService.LoginStatus.LOGIN_OK, loginStatus1);
        assertEquals(UsuarioService.LoginStatus.ERROR_PASSWORD, loginStatus2);
        assertEquals(UsuarioService.LoginStatus.USER_NOT_FOUND, loginStatus3);
    }

    @Test
    public void testServicioRegistroUsuario() {
        // GIVEN
        UsuarioData usuario = new UsuarioData();
        usuario.setEmail("usuario.prueba2@gmail.com");
        usuario.setNombre("Usuario Prueba");
        usuario.setPassword("12345678");
        usuario.setAdministrador(false);

        // WHEN
        UsuarioData nuevoUsuario = usuarioService.registrar(usuario);

        // THEN
        assertNotNull(nuevoUsuario);
        assertEquals("usuario.prueba2@gmail.com", nuevoUsuario.getEmail());
        assertNotNull(nuevoUsuario.getId());
    }

    @Test
    public void testServicioRegistroUsuarioExcepcionConNullPassword() {
        UsuarioData usuario = new UsuarioData();
        usuario.setEmail("usuario.prueba3@gmail.com");
        usuario.setAdministrador(false);

        assertThrows(UsuarioServiceException.class, () -> usuarioService.registrar(usuario));
    }

    @Test
    public void testServicioRegistroUsuarioExcepcionConEmailRepetido() {
        UsuarioData usuario = new UsuarioData();
        usuario.setEmail("user@ua");
        usuario.setPassword("12345678");
        usuario.setAdministrador(false);

        assertThrows(UsuarioServiceException.class, () -> usuarioService.registrar(usuario));
    }

    @Test
    public void testServicioRegistroUsuarioDevuelveUsuarioConId() {
        UsuarioData usuario = new UsuarioData();
        usuario.setNombre("Usuario Prueba");
        usuario.setEmail("usuario.prueba4@gmail.com");
        usuario.setPassword("12345678");
        usuario.setAdministrador(false);

        UsuarioData usuarioNuevo = usuarioService.registrar(usuario);

        assertNotNull(usuarioNuevo.getId());

        UsuarioData usuarioBD = usuarioService.findById(usuarioNuevo.getId());
        assertEquals(usuarioNuevo.getEmail(), usuarioBD.getEmail());
    }

    @Test
    public void testServicioConsultaUsuarioDevuelveUsuario() {
        // WHEN
        UsuarioData usuario = usuarioService.findByEmail("user@ua");

        // THEN
        assertNotNull(usuario);
        assertEquals(usuarioTestId, usuario.getId());
        assertEquals("user@ua", usuario.getEmail());
        assertEquals("Usuario Ejemplo", usuario.getNombre());
    }

    @Test
    public void testEliminarUsuario() {
        // GIVEN
        UsuarioData usuario = new UsuarioData();
        usuario.setNombre("Usuario Eliminar");
        usuario.setEmail("usuario.eliminar@gmail.com");
        usuario.setPassword("delete123");
        usuario.setAdministrador(false);

        UsuarioData usuarioNuevo = usuarioService.registrar(usuario);

        // WHEN
        usuarioService.deleteUser("usuario.eliminar@gmail.com");

        // THEN
        UsuarioData usuarioEliminado = usuarioService.findByEmail("usuario.eliminar@gmail.com");
        assertNull(usuarioEliminado);
    }
}
