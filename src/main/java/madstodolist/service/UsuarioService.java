package madstodolist.service;

import madstodolist.dto.UsuarioData;
import madstodolist.model.Usuario;
import madstodolist.repository.UsuarioRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UsuarioService {

    Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    public enum LoginStatus {LOGIN_OK, USER_NOT_FOUND, ERROR_PASSWORD}

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ModelMapper modelMapper;

    // Método para realizar login
    @Transactional(readOnly = true)
    public LoginStatus login(String eMail, String password) {
        // Buscar usuario por email
        Optional<Usuario> usuario = usuarioRepository.findByEmail(eMail);

        // Si el usuario no existe
        if (!usuario.isPresent()) {
            return LoginStatus.USER_NOT_FOUND;
        }

        // Si la contraseña es incorrecta
        else if (!usuario.get().getPassword().equals(password)) {
            return LoginStatus.ERROR_PASSWORD;
        }

        // Si el login es correcto
        else {
            return LoginStatus.LOGIN_OK;
        }
    }

    // Método para registrar un nuevo usuario
    @Transactional
    public UsuarioData registrar(UsuarioData usuario) {
        // Comprobamos si el email ya está registrado
        Optional<Usuario> usuarioBD = usuarioRepository.findByEmail(usuario.getEmail());

        // Si el usuario ya está registrado, lanzamos excepción
        if (usuarioBD.isPresent()) {
            throw new UsuarioServiceException("El usuario " + usuario.getEmail() + " ya está registrado");
        }

        // Si el email es nulo, lanzamos excepción
        else if (usuario.getEmail() == null || usuario.getEmail().isEmpty()) {
            throw new UsuarioServiceException("El usuario no tiene email");
        }

        // Si la contraseña es nula, lanzamos excepción
        else if (usuario.getPassword() == null || usuario.getPassword().isEmpty()) {
            throw new UsuarioServiceException("El usuario no tiene password");
        }

        // Si todo está correcto, creamos el usuario
        else {
            // Mapeamos el objeto de usuario de tipo UsuarioData a Usuario
            Usuario usuarioNuevo = modelMapper.map(usuario, Usuario.class);

            // Guardamos el nuevo usuario en la base de datos
            usuarioNuevo = usuarioRepository.save(usuarioNuevo);

            // Retornamos los datos del usuario ya guardado, mapeados de nuevo a UsuarioData
            return modelMapper.map(usuarioNuevo, UsuarioData.class);
        }
    }

    // Método para buscar un usuario por email
    @Transactional(readOnly = true)
    public UsuarioData findByEmail(String email) {
        // Buscamos el usuario por su email
        Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);

        // Si no se encuentra el usuario, retornamos null
        if (usuario == null) {
            return null;
        }

        // Si el usuario se encuentra, lo mapeamos y retornamos
        else {
            return modelMapper.map(usuario, UsuarioData.class);
        }
    }

    // Método para buscar un usuario por ID
    @Transactional(readOnly = true)
    public UsuarioData findById(Long usuarioId) {
        // Buscamos el usuario por su ID
        Usuario usuarioPrueba = usuarioRepository.findById(usuarioId).orElse(null);

        // Si no se encuentra el usuario, retornamos null
        if (usuarioPrueba == null) {
            return null;
        }

        // Si el usuario se encuentra, lo mapeamos y retornamos
        else {
            return modelMapper.map(usuarioPrueba, UsuarioData.class);
        }
    }
}
