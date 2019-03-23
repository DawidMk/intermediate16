package pl.sda.intermediate16;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InMemoryCategoryDAO {
    private List<String> categoryList;

    public InMemoryCategoryDAO() {
        initializeCategories();
    }

    private List<String> loadCategoriesFromFile() {
        List<String> linesList = new ArrayList<>();
        this.getClass().getClassLoader().getResource("kategorie.txt");
        ClassLoader classLoader = this.getClass().getClassLoader();
        URI uri = null;
        try {
            uri = classLoader.getResource("kategorie.txt").toURI();
            linesList = Files.readAllLines(Paths.get(uri));
            return linesList;
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return linesList;
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
                }).collect(Collectors.toList());
        populateCategoriesMap(categories);

        Map<Integer, List<Category>> categoriesMap = categories.stream()
                .collect(Collectors.groupingBy(
                        c -> calculateDepth(c.getCategoryName())));
        populateParentId(categoriesMap,0);




    }

    private Map<Integer, List<Category>> populateCategoriesMap(List<Category> categories) {
    /*
            for (Category category : categories) {
                Integer depth = calculateDepth(category.getCategoryName());
                if (categoriesMap.containsKey(depth)) {
                    categoriesMap.get(depth).add(category);
                } else {
                    List<Category> depthCategoryList = new ArrayList<>();
                    depthCategoryList.add(category);
                    categoriesMap.put(depth, depthCategoryList);
                }
            }*/
        return categories.stream()
                .collect(Collectors.groupingBy(
                        c -> calculateDepth(c.getCategoryName())));
//        populateParentId(categoriesMap,0);
//        return categoriesMap;
    }

    private Integer calculateDepth(String word) {
        return word.split("\\s+")[0].length();
    }

    private void populateParentId(Map<Integer, List<Category>> categoriesMap, Integer depth) {
        List<Category> children = categoriesMap.get(depth);
        children.stream().forEach(c -> {
            List<Category> potentialParents = categoriesMap.get(depth - 1);
            Integer parentId = potentialParents.stream()
                    .map(Category::getId)
                    .filter(id -> id < c.getId())
                    .sorted((a, b) -> b - a)
                    .findFirst()
                    .orElse(null);
            c.setParentId(parentId);
        });
        if (categoriesMap.containsKey(depth + 1)) {
            populateParentId(categoriesMap, depth + 1);
        }
    }

}
