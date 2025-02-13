package madstodolist.controller;

import madstodolist.dto.UsuarioData;
import madstodolist.model.Pedido;
import madstodolist.model.Usuario; // Asegúrate de importar tu clase Usuario
import madstodolist.service.UsuarioService; // Servicio para obtener el usuario
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class PerfilController {

    private final UsuarioService usuarioService;

    public PerfilController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/Tienda/Perfil")
    public String mostrarPerfil(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId"); // Obtén el ID de la sesión

        if (userId != null) {
            // Obtiene el usuario con su lista de pedidos
            UsuarioData usuario = usuarioService.findById(userId); // Obtiene el usuario desde el servicio

            // Si el usuario tiene pedidos, los agregamos al DTO
            List<Pedido> pedidos = usuarioService.obtenerPedidosPorUsuario(userId);
            usuario.setPedidos(pedidos);

            model.addAttribute("usuario", usuario); // Agrega el usuario con los pedidos al modelo
        } else {
            model.addAttribute("usuario", null); // Manejo si no hay usuario en sesión
        }

        return "perfil"; // Retorna la vista "perfil"
    }
}
