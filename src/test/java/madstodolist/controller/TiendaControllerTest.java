package madstodolist.controller;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TiendaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testMostrarTienda_CarritoVacio() throws Exception {
        MockHttpSession session = new MockHttpSession();

        // Realizamos una petición GET a /Tienda
        mockMvc.perform(get("/Tienda")
                        .session(session))
                .andExpect(status().isOk());  // Verificamos que la respuesta sea 200 OK
    }

}

