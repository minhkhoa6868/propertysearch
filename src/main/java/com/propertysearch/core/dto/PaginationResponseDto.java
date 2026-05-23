package com.propertysearch.core.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PaginationResponseDto<T> {
    private int offset;
    private int limit;
    private long total;
    private int totalPages;
    private List<T> items;
}
