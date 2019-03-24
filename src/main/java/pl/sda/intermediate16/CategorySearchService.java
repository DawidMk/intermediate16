package pl.sda.intermediate16;

import java.util.ArrayList;
import java.util.List;

public class CategorySearchService {
    private Category category;

    public List<CategoryDTO> filterCategories(String categoryName) {
        List<Category> categoryList = InMemoryCategoryDAO.getInstance().getCategoryList();
        List<CategoryDTO> categoryDTOList = new ArrayList<>();

        for (Category category : categoryList) {
            categoryDTOList.add(buildCategoryDTO(category));
        }
        for (CategoryDTO categoryDTO : categoryDTOList) {
            if (categoryDTO.getParentId() == null) {
                continue;
            }
            //todo mapa
            categoryDTO.setParentCat(
                    categoryDTOList.stream()
                            .filter(c -> c.getId().equals(categoryDTO.getParentId()))
                            .findFirst()
                            .orElse(null));
        }

        for (CategoryDTO categoryDTO : categoryDTOList) {
            if (categoryName.trim().equals(categoryDTO.getCategoryName())) {
                categoryDTO.getCategoryState().setOpen(true);
                categoryDTO.getCategoryState().setSelected(true);
                openParent(categoryDTO.getParentCat());
            }
        }

        return categoryDTOList;
    }

    private void openParent(CategoryDTO categoryDTO) {
        categoryDTO.getCategoryState().setOpen(true);
        if (categoryDTO.getParentId() == null) {
            return;
        }
        openParent(categoryDTO.getParentCat());
    }


    private CategoryDTO buildCategoryDTO(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryName(category.getCategoryName().trim());
        categoryDTO.setId(category.getId());
        categoryDTO.setParentId(category.getParentId());
        categoryDTO.setCategoryState(new CategoryState());
        return categoryDTO;
    }
}

