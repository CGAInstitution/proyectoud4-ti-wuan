package madstodolist.controller;

import madstodolist.dto.RegistroData;
import madstodolist.dto.UsuarioData;
import madstodolist.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Collections;

@Controller
public class AdminController {

    @Autowired
    UsuarioService usuarioService;

    @GetMapping("/admin")
    public String mostrarTienda(HttpSession session) {
        session.getAttribute("userId");
        return "admin";
    }

    @PostMapping("/admin/registro")
    public ResponseEntity<String> registroSubmit(@RequestBody @Valid RegistroData registroData, BindingResult result) {
        System.out.println("Solicitud recibida: " + registroData);  // <-- Log en backend

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Datos inválidos");
        }

        if (usuarioService.findByEmail(registroData.getEmail()) != null) {
            return ResponseEntity.badRequest().body("El usuario " + registroData.getEmail() + " ya existe");
        }

        UsuarioData usuario = new UsuarioData();
        usuario.setEmail(registroData.getEmail());
        usuario.setPassword(registroData.getPassword());
        usuario.setFechaNacimiento(registroData.getFechaNacimiento());
        usuario.setNombre(registroData.getNombre());

        usuarioService.registrar(usuario);
        return ResponseEntity.ok("Usuario registrado exitosamente");
    }


}
