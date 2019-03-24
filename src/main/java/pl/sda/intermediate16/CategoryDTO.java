package pl.sda.intermediate16;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CategoryDTO {

    private Integer id;
    private String categoryName;
    private Integer parentId;
    private CategoryState categoryState;
    private CategoryDTO parentCat;

}
