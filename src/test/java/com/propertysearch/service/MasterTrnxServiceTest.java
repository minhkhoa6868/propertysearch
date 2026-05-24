package com.propertysearch.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.propertysearch.core.dto.PaginationRequestDto;
import com.propertysearch.core.dto.PaginationResponseDto;
import com.propertysearch.masterTrnx.domain.repository.MasterTrnxRepository;
import com.propertysearch.masterTrnx.dto.MasterTrnxDto;
import com.propertysearch.masterTrnx.service.MasterTrnxService;

@ExtendWith(MockitoExtension.class)
public class MasterTrnxServiceTest {
    @Mock
    private MasterTrnxRepository masterTrnxRepository;

    @InjectMocks
    private MasterTrnxService masterTrnxService;

    private PaginationRequestDto request;

    @BeforeEach
    void setUp() {
        request = new PaginationRequestDto();
        request.setOffset(0);
        request.setLimit(50);
        request.setFilters(new HashMap<>());
    }

    // ── search() ──────────────────────────────────────────────

    @Test
    void search_withNoFilters_returnsResults() {
        Page<MasterTrnxDto> mockPage = new PageImpl<>(List.of(new MasterTrnxDto()));
        when(masterTrnxRepository.searchByFilters(any(), any(), any(), any(), any(), any(), any(Pageable.class)))
            .thenReturn(mockPage);

        PaginationResponseDto<MasterTrnxDto> result = masterTrnxService.search(request);

        assertNotNull(result);
        assertEquals(1, result.getItems().size());
        verify(masterTrnxRepository, times(1)).searchByFilters(any(), any(), any(), any(), any(), any(), any());
    }

    @Test
    void search_withValidPostalCode_doesNotThrow() {
        request.getFilters().put("nPostal", "310001");
        Page<MasterTrnxDto> mockPage = new PageImpl<>(List.of());
        when(masterTrnxRepository.searchByFilters(any(), any(), any(), any(), any(), any(), any(Pageable.class)))
            .thenReturn(mockPage);

        assertDoesNotThrow(() -> masterTrnxService.search(request));
    }

    @Test
    void search_withInvalidPostalCode_throwsException() {
        request.getFilters().put("nPostal", "123"); // not 6 digits

        assertThrows(IllegalArgumentException.class, () -> masterTrnxService.search(request));
    }

    @Test
    void search_withNonNumericPostalCode_throwsException() {
        request.getFilters().put("nPostal", "abcdef");

        assertThrows(IllegalArgumentException.class, () -> masterTrnxService.search(request));
    }

    @Test
    void search_withFlrAreaMinGreaterThanMax_throwsException() {
        request.getFilters().put("flrAreaMin", "200");
        request.getFilters().put("flrAreaMax", "100");

        assertThrows(IllegalArgumentException.class, () -> masterTrnxService.search(request));
    }

    @Test
    void search_withValidFlrAreaRange_doesNotThrow() {
        request.getFilters().put("flrAreaMin", "50");
        request.getFilters().put("flrAreaMax", "150");
        Page<MasterTrnxDto> mockPage = new PageImpl<>(List.of());
        when(masterTrnxRepository.searchByFilters(any(), any(), any(), any(), any(), any(), any(Pageable.class)))
            .thenReturn(mockPage);

        assertDoesNotThrow(() -> masterTrnxService.search(request));
    }

    @Test
    void search_withContDateFromAfterContDateTo_throwsException() {
        request.getFilters().put("contDateFrom", "2024-12-31");
        request.getFilters().put("contDateTo", "2024-01-01");

        assertThrows(IllegalArgumentException.class, () -> masterTrnxService.search(request));
    }

    @Test
    void search_withValidDateRange_doesNotThrow() {
        request.getFilters().put("contDateFrom", "2024-01-01");
        request.getFilters().put("contDateTo", "2024-12-31");
        Page<MasterTrnxDto> mockPage = new PageImpl<>(List.of());
        when(masterTrnxRepository.searchByFilters(any(), any(), any(), any(), any(), any(), any(Pageable.class)))
            .thenReturn(mockPage);

        assertDoesNotThrow(() -> masterTrnxService.search(request));
    }

    @Test
    void search_withStNames_passesCorrectlyToRepository() {
        request.getFilters().put("stNames", List.of("ORCHARD ROAD", "MARINA BLVD"));
        Page<MasterTrnxDto> mockPage = new PageImpl<>(List.of());
        when(masterTrnxRepository.searchByFilters(any(), any(), any(), any(), any(), any(), any(Pageable.class)))
            .thenReturn(mockPage);

        masterTrnxService.search(request);

        verify(masterTrnxRepository).searchByFilters(
            eq(List.of("ORCHARD ROAD", "MARINA BLVD")),
            any(), any(), any(), any(), any(), any()
        );
    }

    @Test
    void search_withPagination_returnsCorrectOffsetAndLimit() {
        request.setOffset(50);
        request.setLimit(20);
        Page<MasterTrnxDto> mockPage = new PageImpl<>(List.of());
        when(masterTrnxRepository.searchByFilters(any(), any(), any(), any(), any(), any(), any(Pageable.class)))
            .thenReturn(mockPage);

        PaginationResponseDto<MasterTrnxDto> result = masterTrnxService.search(request);

        assertEquals(50, result.getOffset());
        assertEquals(20, result.getLimit());
    }

    @Test
    void search_withSortByContDateDesc_passesCorrectPageableToRepository() {
        request.setSortField("contDate");
        request.setSortDirection("desc");
        Page<MasterTrnxDto> mockPage = new PageImpl<>(List.of());
        when(masterTrnxRepository.searchByFilters(any(), any(), any(), any(), any(), any(), any(Pageable.class)))
            .thenReturn(mockPage);

        masterTrnxService.search(request);

        verify(masterTrnxRepository).searchByFilters(
            any(), any(), any(), any(), any(), any(),
            argThat(pageable ->
                pageable.getSort().getOrderFor("contDate") != null &&
                pageable.getSort().getOrderFor("contDate").getDirection().isDescending()
            )
        );
    }

    @Test
    void search_withSortByContDateAsc_passesCorrectPageableToRepository() {
        request.setSortField("contDate");
        request.setSortDirection("asc");
        Page<MasterTrnxDto> mockPage = new PageImpl<>(List.of());
        when(masterTrnxRepository.searchByFilters(any(), any(), any(), any(), any(), any(), any(Pageable.class)))
            .thenReturn(mockPage);

        masterTrnxService.search(request);

        verify(masterTrnxRepository).searchByFilters(
            any(), any(), any(), any(), any(), any(),
            argThat(pageable ->
                pageable.getSort().getOrderFor("contDate") != null &&
                pageable.getSort().getOrderFor("contDate").getDirection().isAscending()
            )
        );
    }

    @Test
    void search_withNoSortField_usesDefaultSort() {
        // no sortField set
        Page<MasterTrnxDto> mockPage = new PageImpl<>(List.of());
        when(masterTrnxRepository.searchByFilters(any(), any(), any(), any(), any(), any(), any(Pageable.class)))
            .thenReturn(mockPage);

        masterTrnxService.search(request);

        verify(masterTrnxRepository).searchByFilters(
            any(), any(), any(), any(), any(), any(),
            argThat(pageable ->
                pageable.getSort().getOrderFor("contDate") != null // default sort field
            )
        );
    }

    @Test
    void search_withInvalidSortField_stillExecutesQuery() {
        request.setSortField("nonExistentField");
        request.setSortDirection("desc");
        Page<MasterTrnxDto> mockPage = new PageImpl<>(List.of());
        when(masterTrnxRepository.searchByFilters(any(), any(), any(), any(), any(), any(), any(Pageable.class)))
            .thenReturn(mockPage);

        // should not throw, just pass the field to pageable
        assertDoesNotThrow(() -> masterTrnxService.search(request));
    }

    // ── getDistinctStreetNames() ──────────────────────────────

    @Test
    void getDistinctStreetNames_returnsListFromRepository() {
        List<String> mockNames = List.of("ORCHARD ROAD", "MARINA BLVD", "JURONG WEST");
        when(masterTrnxRepository.findDistinctStreetNames()).thenReturn(mockNames);

        List<String> result = masterTrnxService.getDistinctStreetNames();

        assertEquals(3, result.size());
        assertEquals("ORCHARD ROAD", result.get(0));
        verify(masterTrnxRepository, times(1)).findDistinctStreetNames();
    }

    @Test
    void getDistinctStreetNames_whenEmpty_returnsEmptyList() {
        when(masterTrnxRepository.findDistinctStreetNames()).thenReturn(List.of());

        List<String> result = masterTrnxService.getDistinctStreetNames();

        assertTrue(result.isEmpty());
    }
}
