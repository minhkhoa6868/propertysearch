package com.propertysearch.core.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PaginationRequestDto {
    @Builder.Default
    private int offset = 0;
    @Builder.Default
    private int limit = 10;
    @Builder.Default
    private Map<String, Object> filters = new HashMap<>();
    private String sortField;
    private String sortDirection;
    public int getPageNumber() {
        return limit == 0 ? 0 : (offset / limit);
    }
}
