package pl.sda.intermediate16;

import lombok.Getter;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InMemoryCategoryDAO {

    @Getter
    private List<Category> categoryList = new ArrayList<>();

    private static InMemoryCategoryDAO instance;

    public static InMemoryCategoryDAO getInstance() {
        if (instance == null) {
            synchronized (InMemoryCategoryDAO.class) {
                if (instance == null) {
                    instance = new InMemoryCategoryDAO();
                }
            }
        }
        return instance;
    }

    public InMemoryCategoryDAO() {
        initializeCategories();
    }

    private List<String> loadCategoriesFromFile() {
        ClassLoader classLoader = this.getClass().getClassLoader();
        URI uri = null;
        try {
            uri = classLoader.getResource("kategorie.txt").toURI();
            List<String> lines = Files.readAllLines(Paths.get(uri), StandardCharsets.UTF_16LE);
            return lines;
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private void initializeCategories() {
        List<String> categoriesFromFile = loadCategoriesFromFile();
        AtomicInteger counter = new AtomicInteger(1);
        List<Category> categories = categoriesFromFile.stream()
                .map(s -> {
                    Category category = new Category();
                    category.setCategoryName(s);
                    category.setId(counter.getAndIncrement());
                    return category;
                })
                .collect(Collectors.toList());

        Map<Integer, List<Category>> categoriesMap =
                categories.stream()
                        .collect(Collectors.groupingBy(
                                c -> calculateDepth(c.getCategoryName())));

//        for (Category category : categories) {
//            Integer depth = calculateDepth(category.getCategoryName());
//            if (categoriesMap.containsKey(depth)) {
//                categoriesMap.get(depth).add(category);
//            } else {
//                List<Category> depthCategoryList = new ArrayList<>();
//                depthCategoryList.add(category);
//                categoriesMap.put(depth,depthCategoryList);
//            }
//        }
        populateParentId(categoriesMap, 0);
        categoryList = categories;
    }

    private void populateParentId(Map<Integer, List<Category>> categoriesMap, int depth) {
        List<Category> children = categoriesMap.get(depth);
        children.stream()
                .forEach(c -> findAndSetParentId(categoriesMap, depth, c));
        if (categoriesMap.containsKey(depth + 1)) {
            populateParentId(categoriesMap, depth + 1);
        }

    }

    private void findAndSetParentId(Map<Integer, List<Category>> categoriesMap, int depth, Category c) {
        List<Category> potentialParents = categoriesMap.get(depth - 1);

        Integer parentId = potentialParents == null ? null : potentialParents.stream()
                .map(Category::getId)
                .filter(id -> id < c.getId())
                .sorted((a, b) -> b - a)
                .findFirst()
                .orElse(null);
        c.setParentId(parentId);
    }


    private Integer calculateDepth(String categoryName) {
        return categoryName.startsWith(" ")
                ? categoryName.split("\\S+")[0].length()
                : 0;
    }

}