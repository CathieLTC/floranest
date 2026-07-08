package com.example.floranest.backend.service.impl;

import com.example.floranest.backend.dto.*;
import com.example.floranest.backend.entity.Category;
import com.example.floranest.backend.entity.Product;
import com.example.floranest.backend.service.AiService;
import com.example.floranest.backend.service.CategoryService;
import com.example.floranest.backend.service.PlantChatService;
import com.example.floranest.backend.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AiServiceImpl implements AiService {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final PlantChatService plantChatService;

    public AiServiceImpl(ProductService productService, CategoryService categoryService,
                         PlantChatService plantChatService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.plantChatService = plantChatService;
    }

    @Override
    public List<PlantRecommendationDto> searchPlants(AiSearchRequest request) {
        SearchFilters filters = request.getFilters() != null ? request.getFilters() : new SearchFilters();
        Map<Integer, String> categoryNames = loadCategoryNames();

        return productService.getAllProducts().stream()
                .filter(product -> matchesQuery(product, request.getQuery(), categoryNames))
                .filter(product -> matchesFilters(product, filters, categoryNames))
                .map(product -> toDto(product, categoryNames, null, null))
                .collect(Collectors.toList());
    }

    @Override
    public List<PlantRecommendationDto> getRecommendations(AiRecommendationRequest request) {
        Map<Integer, String> categoryNames = loadCategoryNames();

        return productService.getAllProducts().stream()
                .map(product -> {
                    int score = scoreProduct(product, request, categoryNames);
                    String reason = buildRecommendationReason(product, request, categoryNames, score);
                    return new ScoredPlant(product, score, reason);
                })
                .filter(scored -> scored.score > 0)
                .sorted(Comparator.comparingInt(ScoredPlant::score).reversed())
                .limit(4)
                .map(scored -> toDto(scored.product, categoryNames, scored.score, scored.reason))
                .collect(Collectors.toList());
    }

    @Override
    public ChatMessageDto chat(AiChatRequest request) {
        String reply = plantChatService.generateReply(request);

        ChatMessageDto response = new ChatMessageDto();
        response.setRole("assistant");
        response.setContent(reply);
        response.setTimestamp(Instant.now().toString());
        return response;
    }

    private Map<Integer, String> loadCategoryNames() {
        return categoryService.getAllCategories().stream()
                .collect(Collectors.toMap(
                        Category::getCategoryId,
                        Category::getCategoryName,
                        (a, b) -> a
                ));
    }

    private boolean matchesQuery(Product product, String query, Map<Integer, String> categoryNames) {
        if (!StringUtils.hasText(query)) {
            return true;
        }

        String term = query.trim().toLowerCase();
        String category = resolveCategoryLabel(product.getCategoryId(), categoryNames);

        return containsIgnoreCase(product.getProductName(), term)
                || containsIgnoreCase(product.getDescription(), term)
                || containsIgnoreCase(product.getSunlight(), term)
                || containsIgnoreCase(product.getDifficulty(), term)
                || containsIgnoreCase(product.getWatering(), term)
                || containsIgnoreCase(category, term);
    }

    private boolean matchesFilters(Product product, SearchFilters filters, Map<Integer, String> categoryNames) {
        if (StringUtils.hasText(filters.getCategory())) {
            String category = resolveCategoryLabel(product.getCategoryId(), categoryNames);
            if (!category.equalsIgnoreCase(filters.getCategory())) {
                return false;
            }
        }

        if (StringUtils.hasText(filters.getCareLevel())) {
            String careLevel = normalizeCareLevel(product.getDifficulty());
            if (!careLevel.equalsIgnoreCase(filters.getCareLevel())) {
                return false;
            }
        }

        if (StringUtils.hasText(filters.getLight())) {
            if (product.getSunlight() == null
                    || !product.getSunlight().toLowerCase().contains(filters.getLight().toLowerCase())) {
                return false;
            }
        }

        return true;
    }

    private int scoreProduct(Product product, AiRecommendationRequest prefs, Map<Integer, String> categoryNames) {
        int score = 40;
        String category = resolveCategoryLabel(product.getCategoryId(), categoryNames);
        String careLevel = normalizeCareLevel(product.getDifficulty());
        String sunlight = product.getSunlight() != null ? product.getSunlight().toLowerCase() : "";

        if ("indoor".equalsIgnoreCase(prefs.getLocation()) && category.equalsIgnoreCase("Indoor")) {
            score += 20;
        }
        if ("outdoor".equalsIgnoreCase(prefs.getLocation()) && category.equalsIgnoreCase("Outdoor")) {
            score += 20;
        }

        if ("beginner".equalsIgnoreCase(prefs.getExperience()) && "Easy".equalsIgnoreCase(careLevel)) {
            score += 15;
        } else if ("intermediate".equalsIgnoreCase(prefs.getExperience()) && "Moderate".equalsIgnoreCase(careLevel)) {
            score += 12;
        } else if ("expert".equalsIgnoreCase(prefs.getExperience())) {
            score += 8;
        }

        if ("low".equalsIgnoreCase(prefs.getLight()) && sunlight.contains("low")) {
            score += 15;
        } else if ("bright".equalsIgnoreCase(prefs.getLight()) && sunlight.contains("bright")) {
            score += 15;
        } else if ("direct".equalsIgnoreCase(prefs.getLight())
                && (sunlight.contains("direct") || sunlight.contains("full sun"))) {
            score += 15;
        }

        if ("small".equalsIgnoreCase(prefs.getSpace()) && isCompactPlant(product)) {
            score += 10;
        } else if ("large".equalsIgnoreCase(prefs.getSpace()) && !isCompactPlant(product)) {
            score += 10;
        }

        if ("air-purifying".equalsIgnoreCase(prefs.getPurpose()) && isAirPurifying(product)) {
            score += 12;
        }
        if ("herbs".equalsIgnoreCase(prefs.getPurpose()) && isHerb(product)) {
            score += 12;
        }
        if ("low-maintenance".equalsIgnoreCase(prefs.getPurpose()) && "Easy".equalsIgnoreCase(careLevel)) {
            score += 12;
        }
        if ("decoration".equalsIgnoreCase(prefs.getPurpose())) {
            score += 5;
        }

        if (product.getStock() != null && product.getStock() > 0) {
            score += 5;
        }

        return Math.min(score, 99);
    }

    private String buildRecommendationReason(Product product, AiRecommendationRequest prefs,
                                             Map<Integer, String> categoryNames, int score) {
        List<String> parts = new ArrayList<>();
        String category = resolveCategoryLabel(product.getCategoryId(), categoryNames);
        String careLevel = normalizeCareLevel(product.getDifficulty());

        if ("beginner".equalsIgnoreCase(prefs.getExperience()) && "Easy".equalsIgnoreCase(careLevel)) {
            parts.add("beginner-friendly");
        }

        if ("indoor".equalsIgnoreCase(prefs.getLocation()) && category.equalsIgnoreCase("Indoor")) {
            parts.add("ideal for indoor spaces");
        }

        if ("outdoor".equalsIgnoreCase(prefs.getLocation()) && category.equalsIgnoreCase("Outdoor")) {
            parts.add("great for outdoor gardens");
        }

        if ("small".equalsIgnoreCase(prefs.getSpace()) && isCompactPlant(product)) {
            parts.add("compact enough for small spaces");
        }

        if ("air-purifying".equalsIgnoreCase(prefs.getPurpose()) && isAirPurifying(product)) {
            parts.add("known for air-purifying qualities");
        }

        if (parts.isEmpty()) {
            parts.add("a strong match for your preferences");
        }

        return "Recommended because it is " + String.join(" and ", parts) + " (match score: " + score + "%).";
    }

    private PlantRecommendationDto toDto(Product product, Map<Integer, String> categoryNames,
                                         Integer matchScore, String reason) {
        PlantRecommendationDto dto = new PlantRecommendationDto();
        dto.setId(product.getProductId());
        dto.setName(product.getProductName());
        dto.setImageUrl(product.getImageUrl());
        dto.setDescription(product.getDescription());
        dto.setCareLevel(normalizeCareLevel(product.getDifficulty()));
        dto.setLight(product.getSunlight() != null ? product.getSunlight() : "Varies");
        dto.setCategory(resolveCategoryLabel(product.getCategoryId(), categoryNames));
        dto.setMatchScore(matchScore);
        dto.setReason(reason);
        dto.setPrice(product.getPrice());
        return dto;
    }

    private String resolveCategoryLabel(Integer categoryId, Map<Integer, String> categoryNames) {
        if (categoryId == null) {
            return "General";
        }

        String name = categoryNames.getOrDefault(categoryId, "General");
        String lower = name.toLowerCase();

        if (lower.contains("indoor")) {
            return "Indoor";
        }
        if (lower.contains("outdoor") || lower.contains("garden")) {
            return "Outdoor";
        }

        return name;
    }

    private String normalizeCareLevel(String difficulty) {
        if (!StringUtils.hasText(difficulty)) {
            return "Moderate";
        }

        String lower = difficulty.toLowerCase();
        if (lower.contains("easy") || lower.contains("beginner")) {
            return "Easy";
        }
        if (lower.contains("hard") || lower.contains("expert") || lower.contains("advanced")) {
            return "Hard";
        }
        return "Moderate";
    }

    private boolean isCompactPlant(Product product) {
        String text = ((product.getProductName() != null ? product.getProductName() : "") + " "
                + (product.getDescription() != null ? product.getDescription() : "")).toLowerCase();
        return text.contains("snake") || text.contains("pothos") || text.contains("succulent")
                || text.contains("desk") || text.contains("small") || text.contains("mini");
    }

    private boolean isAirPurifying(Product product) {
        String text = ((product.getProductName() != null ? product.getProductName() : "") + " "
                + (product.getDescription() != null ? product.getDescription() : "")).toLowerCase();
        return text.contains("snake") || text.contains("peace lily") || text.contains("pothos")
                || text.contains("spider") || text.contains("purif");
    }

    private boolean isHerb(Product product) {
        String text = ((product.getProductName() != null ? product.getProductName() : "") + " "
                + (product.getDescription() != null ? product.getDescription() : "")).toLowerCase();
        return text.contains("herb") || text.contains("basil") || text.contains("mint")
                || text.contains("rosemary") || text.contains("lavender") || text.contains("thyme");
    }

    private boolean containsIgnoreCase(String value, String term) {
        return value != null && value.toLowerCase().contains(term);
    }

    private record ScoredPlant(Product product, int score, String reason) {}
}
