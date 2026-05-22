package com.propertysearch.masterTrnx.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class SearchRequestDto {
    private int offset = 0;
    private int limit = 50;
    private List<String> stNames;
    @Size(max = 6)
    private String nPostal;
    private Double flrAreaMin;
    private Double flrAreaMax;
    private LocalDate contDateFrom;
    private LocalDate contDateTo;
}
