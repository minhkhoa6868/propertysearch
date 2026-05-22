package com.propertysearch.masterTrnx.controller;

import com.propertysearch.core.dto.PaginationRequestDto;
import com.propertysearch.masterTrnx.service.MasterTrnxService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class MasterTrnxController {
    private final MasterTrnxService masterTrnxService;

    @GetMapping
    public String index(
        @RequestParam(required = false) List<String> stNames,
        @RequestParam(required = false) String nPostal,
        @RequestParam(required = false) Double flrAreaMin,
        @RequestParam(required = false) Double flrAreaMax,
        @RequestParam(required = false) String contDateFrom,
        @RequestParam(required = false) String contDateTo,
        @RequestParam(required = false, defaultValue = "0") Integer offset,
        @RequestParam(required = false, defaultValue = "50") Integer limit,
        @RequestParam(required = false) String sortField,
        @RequestParam(required = false) String sortDirection,
        Model model
    ) {
        PaginationRequestDto paginationRequest = new PaginationRequestDto();
        String cleanNPostal = (nPostal != null && nPostal.isEmpty()) ? null : nPostal;
        Double cleanFlrAreaMin = (flrAreaMin != null && flrAreaMin.toString().isEmpty()) ? null : flrAreaMin;
        Double cleanFlrAreaMax = (flrAreaMax != null && flrAreaMax.toString().isEmpty()) ? null : flrAreaMax;
        String cleanContDateFrom = (contDateFrom != null && contDateFrom.isEmpty()) ? null : contDateFrom;
        String cleanContDateTo = (contDateTo != null && contDateTo.isEmpty()) ? null : contDateTo;
        paginationRequest.getFilters().put("stNames", stNames);
        paginationRequest.getFilters().put("nPostal", cleanNPostal);
        paginationRequest.getFilters().put("flrAreaMin", cleanFlrAreaMin);
        paginationRequest.getFilters().put("flrAreaMax", cleanFlrAreaMax);
        paginationRequest.getFilters().put("contDateFrom", cleanContDateFrom);
        paginationRequest.getFilters().put("contDateTo", cleanContDateTo);
        paginationRequest.setOffset(offset);
        paginationRequest.setLimit(limit);
        paginationRequest.setSortField(sortField);
        paginationRequest.setSortDirection(sortDirection);

        model.addAttribute("result", masterTrnxService.search(paginationRequest));

        model.addAttribute(
                "streetNames",
                masterTrnxService.getDistinctStreetNames()
        );

        // keep the value in the search form
        model.addAttribute("selectedStreetNames", stNames);
        model.addAttribute("nPostalValue", cleanNPostal);
        model.addAttribute("flrAreaMinValue", cleanFlrAreaMin);
        model.addAttribute("flrAreaMaxValue", cleanFlrAreaMax);
        model.addAttribute("contDateFromValue", cleanContDateFrom);
        model.addAttribute("contDateToValue", cleanContDateTo);
        model.addAttribute("offset", offset);
        model.addAttribute("limit", limit);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortDirection);

        return "masterTrnx/index";
    }
}
