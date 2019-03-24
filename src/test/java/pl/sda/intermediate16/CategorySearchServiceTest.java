package pl.sda.intermediate16;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CategorySearchServiceTest {
    @Test
    void shouldFilterCategories() {
        CategorySearchService categorySearchService = new CategorySearchService();
        List<CategoryDTO> categoryDTOList = categorySearchService.filterCategories("Lektury");

        CategoryDTO c4 = categoryDTOList.stream()
                .filter(c -> c.getId().equals(4))
                .findFirst()
                .orElse(null);

        Assertions.assertTrue(c4.getCategoryState().isOpen());
    }

}