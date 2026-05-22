package com.propertysearch.masterTrnx.service;

import com.propertysearch.core.dto.PaginationRequestDto;
import com.propertysearch.core.dto.PaginationResponseDto;
import com.propertysearch.core.util.PaginationUtils;
import com.propertysearch.masterTrnx.domain.repository.MasterTrnxRepository;
import com.propertysearch.masterTrnx.dto.MasterTrnxDto;
import com.propertysearch.masterTrnx.mapper.MasterTrnxMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MasterTrnxService {
    private final MasterTrnxRepository masterTrnxRepository;
    private final MasterTrnxMapper masterTrnxMapper;

    @Transactional(readOnly = true)
    public PaginationResponseDto<MasterTrnxDto> search(PaginationRequestDto request) {
        Pageable pageable = PaginationUtils.createPageable(request, "contDate");
        // street name
        List<String> stNames = getFilterList(request.getFilters(), "stNames");
        // n_postal code
        Object nPostalObj = request.getFilters().get("nPostal");

        String nPostal =
            nPostalObj != null
                ? nPostalObj.toString()
                : "";
        // floor area min and max
        Object flrAreaMinObj = request.getFilters().get("flrAreaMin");

        String flrAreaMin =
            flrAreaMinObj != null
                ? flrAreaMinObj.toString()
                : "";

        Double flrAreaMinValue = flrAreaMin.isEmpty() ? null : Double.valueOf(flrAreaMin);

        Object flrAreaMaxObj = request.getFilters().get("flrAreaMax");

        String flrAreaMax =
            flrAreaMaxObj != null
                ? flrAreaMaxObj.toString()
                : "";

        Double flrAreaMaxValue = flrAreaMax.isEmpty() ? null : Double.valueOf(flrAreaMax);
        // contract date from and to
        LocalDate contDateFrom = parseLocalDate(request.getFilters(), "contDateFrom");
        LocalDate contDateTo = parseLocalDate(request.getFilters(), "contDateTo");

        Page<MasterTrnxDto> page = masterTrnxRepository.searchByFilters(stNames, nPostal, flrAreaMinValue, flrAreaMaxValue, contDateFrom, contDateTo, pageable);
        return PaginationUtils.buildResponse(request, page, item -> item);
    }

    @Transactional(readOnly = true)
    public List<String> getDistinctStreetNames() {
        return masterTrnxRepository.findDistinctStreetNames();
    }

    private LocalDate parseLocalDate(Map<String, Object> filters, String key) {
        if (filters == null)
            return null;
        Object value = filters.get(key);
        if (value == null || value.toString().isEmpty())
            return null;
        try {
            return LocalDate.parse(value.toString());
        } catch (Exception e) {
            return null;
        }
    }

    private List<String> getFilterList(Map<String, Object> filters, String key) {
        Object val = filters.get(key);
        if (val == null) return null;
        List<String> list = (List<String>) val;
        return list.isEmpty() ? null : list;
    }
}
