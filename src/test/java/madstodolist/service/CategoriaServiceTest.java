package madstodolist.service;

import madstodolist.dto.CategoriaData;
import madstodolist.model.Categoria;
import madstodolist.repository.CategoriaRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CategoriaServiceTest {

    @Autowired
    private CategoriaRepository categoriaRepository;

    private CategoriaService categoriaService;

    private Categoria categoria1;
    private Categoria categoria2;
    private int categoriasIniciales; // Las 3 categorias de la bd

    @BeforeAll
    void setUp() {
        categoriaService = new CategoriaService(categoriaRepository);

        categoriasIniciales = (int) categoriaRepository.count();
        assertTrue(categoriasIniciales >= 3, "Debe haber al menos 3 categorías creadas en la BD.");

        // nuevas categorias
        categoria1 = new Categoria();
        categoria1.setNombre("Pósters");
        categoria1.setDescripcion("Posters de campeones de LoL");
        categoriaRepository.save(categoria1);

        categoria2 = new Categoria();
        categoria2.setNombre("Sudaderas");
        categoria2.setDescripcion("Ropa inspirada en LoL");
        categoriaRepository.save(categoria2);
    }

    @AfterAll
    void tearDown() {
        categoriaRepository.delete(categoria1);
        categoriaRepository.delete(categoria2);
    }

    @Test
    void testFindById() {
        CategoriaData categoriaData = categoriaService.findById(categoria1.getId());
        assertNotNull(categoriaData);
        assertEquals("Pósters", categoriaData.getNombre());
        assertEquals("Posters de campeones de LoL", categoriaData.getDescripcion());
    }

    @Test
    void testFindAll() {
        List<CategoriaData> categorias = categoriaService.findAll();
        assertEquals(categoriasIniciales + 2, categorias.size(), "Las 3 existentes y las 2 creadas.");
    }
}
