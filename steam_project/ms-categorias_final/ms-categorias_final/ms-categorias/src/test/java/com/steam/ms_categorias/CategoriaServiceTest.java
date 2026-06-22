package com.steam.ms_categorias;

import com.steam.ms_categorias.client.JuegoClient;
import com.steam.ms_categorias.exception.RecursoNoEncontradoException;
import com.steam.ms_categorias.exception.ReglaDeNegocioException;
import com.steam.ms_categorias.model.Categoria;
import com.steam.ms_categorias.model.dto.CategoriaRequestDTO;
import com.steam.ms_categorias.model.dto.CategoriaResponseDTO;
import com.steam.ms_categorias.repository.CategoriaRepository;
import com.steam.ms_categorias.service.CategoriaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {

    @Mock private CategoriaRepository categoriaRepository;
    @Mock private JuegoClient juegoClient;

    @InjectMocks private CategoriaService categoriaService;

    private Categoria categoria;

    @BeforeEach
    void setUp() {
        categoria = new Categoria();
        categoria.setId(1);
        categoria.setNombre("Acción");
        categoria.setDescripcion("Juegos de combate y ritmo rápido");
        categoria.setActivo(true);
    }

    @Test
    @DisplayName("findById: retorna la categoria cuando existe")
    void testFindById_exitoso() {
        when(categoriaRepository.findById(1)).thenReturn(Optional.of(categoria));

        CategoriaResponseDTO dto = categoriaService.findById(1);

        assertNotNull(dto);
        assertEquals(1, dto.getId());
        assertEquals("Acción", dto.getNombre());
    }

    @Test
    @DisplayName("findById: lanza RecursoNoEncontradoException cuando no existe")
    void testFindById_noEncontrado() {
        when(categoriaRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> categoriaService.findById(999));
    }

    @Test
    @DisplayName("save: crea la categoria cuando el nombre no existe")
    void testSave_exitoso() {
        CategoriaRequestDTO request = new CategoriaRequestDTO();
        request.setNombre("Acción");
        request.setDescripcion("Juegos de combate y ritmo rápido");

        when(categoriaRepository.existsByNombreIgnoreCase("Acción")).thenReturn(false);
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoria);

        CategoriaResponseDTO dto = categoriaService.save(request);

        assertNotNull(dto);
        assertEquals("Acción", dto.getNombre());
        verify(categoriaRepository).save(any(Categoria.class));
    }

    @Test
    @DisplayName("save: lanza ReglaDeNegocioException con nombre duplicado")
    void testSave_nombreDuplicado() {
        CategoriaRequestDTO request = new CategoriaRequestDTO();
        request.setNombre("Acción");
        request.setDescripcion("Otra descripción");

        when(categoriaRepository.existsByNombreIgnoreCase("Acción")).thenReturn(true);

        assertThrows(ReglaDeNegocioException.class, () -> categoriaService.save(request));
        verify(categoriaRepository, never()).save(any(Categoria.class));
    }

    @Test
    @DisplayName("delete: elimina cuando la categoria existe")
    void testDelete_exitoso() {
        when(categoriaRepository.existsById(1)).thenReturn(true);

        categoriaService.delete(1);

        verify(categoriaRepository).deleteById(1);
    }

    @Test
    @DisplayName("delete: lanza RecursoNoEncontradoException cuando no existe")
    void testDelete_noEncontrado() {
        when(categoriaRepository.existsById(999)).thenReturn(false);

        assertThrows(RecursoNoEncontradoException.class,
                () -> categoriaService.delete(999));
        verify(categoriaRepository, never()).deleteById(any());
    }
}
