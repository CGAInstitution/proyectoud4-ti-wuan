package madstodolist.controller;

import madstodolist.dto.RegistroData;
import madstodolist.dto.UsuarioData;
import madstodolist.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@Controller
public class AdminController {

    @Autowired
    UsuarioService usuarioService;

    @GetMapping("/admin")
    public String mostrarTienda(HttpSession session, Model model) {
        session.getAttribute("userId");
        model.addAttribute("registroData", new RegistroData());

        List<UsuarioData> usuarios = usuarioService.findAll();

        model.addAttribute("usuarios", usuarios);

        return "admin";
    }

    @PostMapping("/admin/registro")
    public String registroUsuario(@Valid RegistroData registroData, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors() || registroData.getEmail().isEmpty() || registroData.getNombre().isEmpty() || registroData.getPassword().isEmpty()) {
            model.addAttribute("errorMessage", "Errores en el formulario. Por favor, corrige los campos marcados y asegúrate de que todos los campos estén llenos.");

            model.addAttribute("registroData", registroData);
            return "admin";
        }

        System.out.println("Registrando usuario con email: " + registroData.getEmail());

        UsuarioData usuarioData = new UsuarioData();
        usuarioData.setEmail(registroData.getEmail());
        usuarioData.setNombre(registroData.getNombre());
        usuarioData.setPassword(registroData.getPassword());
        usuarioData.setFechaNacimiento(registroData.getFechaNacimiento());
        usuarioData.setAdministrador(false);

        usuarioService.registrar(usuarioData);
        System.out.println("Usuario registrado con éxito.");

        return "redirect:/admin";
    }

    @PostMapping("/admin/modUser")
    public String modificarUsuario(@Valid RegistroData registroData, BindingResult bindingResult, Model model) {

            if (bindingResult.hasErrors() || registroData.getEmail().isEmpty() || registroData.getNombre().isEmpty() || registroData.getPassword().isEmpty()) {
                model.addAttribute("errorMessage", "Errores en el formulario. Por favor, corrige los campos marcados y asegúrate de que todos los campos estén llenos.");

                model.addAttribute("registroData", registroData);
                return "admin";
            }

            System.out.println("Modificando usuario con email: " + registroData.getEmail());

            UsuarioData usuarioData = new UsuarioData();
            usuarioData.setEmail(registroData.getEmail());
            usuarioData.setNombre(registroData.getNombre());
            usuarioData.setPassword(registroData.getPassword());
            usuarioData.setFechaNacimiento(registroData.getFechaNacimiento());
            usuarioData.setAdministrador(registroData.getAdministrador());

            usuarioService.modificarUsuario(usuarioData);
            System.out.println("Usuario modificado con éxito.");

            return "redirect:/admin";
    }
}
