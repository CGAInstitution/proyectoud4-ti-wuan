package madstodolist.service;

import madstodolist.dto.CategoriaData;
import madstodolist.model.Categoria;
import madstodolist.repository.CategoriaRepository;
import org.springframework.stereotype.Service;

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
}
