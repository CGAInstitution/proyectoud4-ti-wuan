package madstodolist.service;

import madstodolist.dto.CategoriaData;
import madstodolist.model.Categoria;
import madstodolist.repository.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoriaService {
    private CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public CategoriaData findById(Long id) {
        Categoria categoria = categoriaRepository.findById(id).get();
        return new CategoriaData(categoria.getId(), categoria.getNombre(), categoria.getDescripcion());
    }

    public List<CategoriaData> findAll() {
        List<Categoria> categorias = categoriaRepository.findAll();
        List<CategoriaData> categoriasData = new ArrayList<>();
        for (Categoria categoria : categorias) {
            categoriasData.add(new CategoriaData(categoria.getId(), categoria.getNombre(), categoria.getDescripcion()));
        }
        return categoriasData;
    }
}
