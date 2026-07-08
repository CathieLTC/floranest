package com.example.floranest.backend.dto;

import lombok.Data;

@Data
public class AiSearchRequest {

    private String query;
    private SearchFilters filters;

}
