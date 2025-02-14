package madstodolist.service;

import madstodolist.dto.UsuarioData;
import madstodolist.model.Pedido;
import madstodolist.model.Usuario;
import madstodolist.repository.UsuarioRepository;
import madstodolist.repository.PedidoRepository;  // Agregamos PedidoRepository
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UsuarioService {

    Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    public enum LoginStatus {LOGIN_OK, USER_NOT_FOUND, ERROR_PASSWORD}

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PedidoRepository pedidoRepository;  // Agregamos PedidoRepository

    @Autowired
    private ModelMapper modelMapper;

    // Método para realizar login
    @Transactional(readOnly = true)
    public LoginStatus login(String eMail, String password) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(eMail);

        if (!usuario.isPresent()) {
            return LoginStatus.USER_NOT_FOUND;
        } else if (!usuario.get().getPassword().equals(password)) {
            return LoginStatus.ERROR_PASSWORD;
        } else {
            return LoginStatus.LOGIN_OK;
        }
    }

    // Método para registrar un nuevo usuario
    @Transactional
    public UsuarioData registrar(UsuarioData usuario) {
        Optional<Usuario> usuarioBD = usuarioRepository.findByEmail(usuario.getEmail());

        if (usuarioBD.isPresent()) {
            throw new UsuarioServiceException("El usuario " + usuario.getEmail() + " ya está registrado");
        } else if (usuario.getEmail() == null || usuario.getEmail().isEmpty()) {
            throw new UsuarioServiceException("El usuario no tiene email");
        } else if (usuario.getPassword() == null || usuario.getPassword().isEmpty()) {
            throw new UsuarioServiceException("El usuario no tiene password");
        } else {
            Usuario usuarioNuevo = modelMapper.map(usuario, Usuario.class);
            usuarioNuevo = usuarioRepository.save(usuarioNuevo);
            return modelMapper.map(usuarioNuevo, UsuarioData.class);
        }
    }

    // Método para buscar un usuario por email
    @Transactional(readOnly = true)
    public UsuarioData findByEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);
        if (usuario == null) {
            return null;
        } else {
            return modelMapper.map(usuario, UsuarioData.class);
        }
    }

    // Método para buscar un usuario por ID
    @Transactional(readOnly = true)
    public UsuarioData findById(Long usuarioId) {
        Usuario usuarioPrueba = usuarioRepository.findById(usuarioId).orElse(null);
        if (usuarioPrueba == null) {
            return null;
        } else {
            return modelMapper.map(usuarioPrueba, UsuarioData.class);
        }
    }

    // Método para obtener los pedidos de un usuario por su ID
    @Transactional(readOnly = true)
    public List<Pedido> obtenerPedidosPorUsuario(Long usuarioId) {
        // Buscar el usuario por su ID
        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);

        if (usuario != null) {
            // Buscar los pedidos asociados al usuario
            return pedidoRepository.findByUsuario(usuario);
        } else {
            return List.of();  // Si no se encuentra el usuario, retornamos una lista vacía
        }
    }

    // Método para modificar un usuario
    @Transactional
    public void modificarUsuario(UsuarioData usuarioData) {
        Optional<Usuario> usuarioBD = usuarioRepository.findByEmail(usuarioData.getEmail());

        if (usuarioBD.isPresent()) {
            Usuario usuario = usuarioBD.get();
            if (usuarioData.getNombre() != null || !usuarioData.getNombre().isEmpty()) {
               usuario.setNombre(usuarioData.getNombre());
           } else if (usuarioData.getPassword() != null || !usuarioData.getPassword().isEmpty()) {
               usuario.setPassword(usuarioData.getPassword());
           }
            usuario.setFechaNacimiento(usuarioData.getFechaNacimiento());
            usuario.setAdministrador(usuarioData.getAdministrador());
            usuario = usuarioRepository.save(usuario);
            modelMapper.map(usuario, UsuarioData.class);
        } else {
            throw new UsuarioServiceException("El usuario no existe");
        }
    }

    public List<UsuarioData> findAll() {
        List<Usuario> usuarios = StreamSupport
                .stream(usuarioRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
        return usuarios.stream()
                .map(usuario -> new UsuarioData(usuario.getId(), usuario.getEmail(), usuario.getPassword(), usuario.getNombre(),  usuario.getFechaNacimiento(), usuario.getAdministrador()))
                .collect(Collectors.toList());

    }

    // Método para eliminar un usuario
    @Transactional
    public void deleteUser(String email) {
        Optional<Usuario> usuarioBD = usuarioRepository.findByEmail(email);

        if (usuarioBD.isPresent()) {
            Usuario usuario = usuarioBD.get();
            usuarioRepository.delete(usuario);
        } else {
            throw new UsuarioServiceException("El usuario no existe");
        }
    }

}
