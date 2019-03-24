package pl.sda.intermediate16;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryCategoryDAOTest {

    @Test
    void shouldPopulateCategoriesListProperly() {
        InMemoryCategoryDAO inMemoryCategoryDAO = new InMemoryCategoryDAO();
        List<Category> categoryList = inMemoryCategoryDAO.getCategoryList();
//        Assertions.assertEquals(4,categoryList.get(5).getParentId());
        Assertions.assertEquals(4,categoryList.stream().filter(c -> c.getId().equals(5)).findFirst().orElse(null).getParentId());
    }
}