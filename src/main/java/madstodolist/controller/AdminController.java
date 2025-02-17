package madstodolist.controller;

import madstodolist.dto.CategoriaData;
import madstodolist.dto.ProductoData;
import madstodolist.dto.RegistroData;
import madstodolist.dto.UsuarioData;
import madstodolist.model.Categoria;
import madstodolist.model.Producto;
import madstodolist.service.CategoriaService;
import madstodolist.service.ProductoService;
import madstodolist.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.List;

@Controller
public class AdminController {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    ProductoService productoService;

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping("/admin")
    public String mostrarTienda(HttpSession session, Model model) {
        session.getAttribute("userId");
        model.addAttribute("registroData", new RegistroData());
        model.addAttribute("productoData", new ProductoData());

        List<UsuarioData> usuarios = usuarioService.findAll();
        List<ProductoData> productos = productoService.findAll();
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("productos", productos);

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

    @PostMapping("/admin/deleteUser")
    public String deleteUser(@Valid RegistroData registroData, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors() || registroData.getEmail().isEmpty()) {
            model.addAttribute("errorMessage", "Errores en el formulario. Por favor, corrige los campos marcados y asegúrate de que todos los campos estén llenos.");

            model.addAttribute("registroData", registroData);
        }

        usuarioService.deleteUser(registroData.getEmail());
        return "redirect:/admin";
    }

    @PostMapping("/admin/delProduct")
    public String deleteProduct(@Valid ProductoData productoData, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", "Errores en el formulario. Por favor, corrige los campos marcados y asegúrate de que todos los campos estén llenos.");

            model.addAttribute("productoData", productoData);
        }

        productoService.deleteProduct(productoData.getId());
        return "redirect:/admin";
    }

    @PostMapping("/admin/addProduct")
    public String addProduct(@Valid ProductoData productoData,
                             @RequestParam("imagen") MultipartFile imagen,
                             BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors() || productoData.getNombre().isEmpty() || productoData.getDescripcion().isEmpty() ||
                productoData.getPrecio() == null || productoData.getCategoriaId() == null) {
            model.addAttribute("errorMessage", "Errores en el formulario. Por favor, corrige los campos.");
            model.addAttribute("productoData", productoData);
            return "admin"; // Retorna a la página del formulario
        }

        try {
            if (!imagen.isEmpty()) {
                // Ruta donde se guardará la imagen
                String uploadDir = "src/main/resources/static/img/";
                String fileName = imagen.getOriginalFilename();

                // Guardar la imagen en la carpeta del servidor
                Path filePath = Paths.get(uploadDir + fileName);
                Files.copy(imagen.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                // Establecer la URL de la imagen en el objeto ProductoData
                productoData.setImagenUrl(fileName);
                System.out.println("Imagen guardada."+ productoData.getImagenUrl());
            }
        } catch (IOException e) {
            model.addAttribute("errorMessage", "Error al guardar la imagen.");
            return "admin";
        }

        CategoriaData categoria = categoriaService.findById(productoData.getCategoriaId());
        productoService.addProduct(productoData, categoria);

        return "redirect:/admin";
    }


    @PostMapping("/admin/modProduct")
    public String modProduct(@Valid ProductoData productoData,
                             @RequestParam("imagen") MultipartFile imagen,
                             BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors() || productoData.getNombre().isEmpty() || productoData.getDescripcion().isEmpty() ||
                productoData.getPrecio() == null || productoData.getCategoriaId() == null) {
            model.addAttribute("errorMessage", "Errores en el formulario. Por favor, corrige los campos.");
            model.addAttribute("productoData", productoData);
            return "admin"; // Retorna a la página del formulario
        }
        try {
            if (!imagen.isEmpty()) {
                // Ruta donde se guardará la imagen
                String uploadDir = "src/main/resources/static/img/";
                String fileName = imagen.getOriginalFilename();

                // Guardar la imagen en la carpeta del servidor
                Path filePath = Paths.get(uploadDir + fileName);
                Files.copy(imagen.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                // Establecer la URL de la imagen en el objeto ProductoData
                productoData.setImagenUrl(fileName);
                System.out.println("Imagen guardada."+ productoData.getImagenUrl());
            }
        } catch (IOException e) {
            model.addAttribute("errorMessage", "Error al guardar la imagen.");
            return "admin";
        }

        CategoriaData categoria = categoriaService.findById(productoData.getCategoriaId());

        productoService.modProduct(productoData, categoria);
        return "redirect:/admin";
    }
}
