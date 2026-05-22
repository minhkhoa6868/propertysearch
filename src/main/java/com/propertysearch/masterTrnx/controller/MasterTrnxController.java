package com.propertysearch.masterTrnx.controller;

import com.propertysearch.core.dto.PaginationRequestDto;
import com.propertysearch.masterTrnx.dto.SearchRequestDto;
import com.propertysearch.masterTrnx.service.MasterTrnxService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class MasterTrnxController {
    private final MasterTrnxService masterTrnxService;

    @GetMapping
    public String index(Model model) {

        model.addAttribute(
                "streetNames",
                masterTrnxService.getDistinctStreetNames()
        );

        return "masterTrnx/index";
    }

    @PostMapping("/search")
    public String search(@ModelAttribute @Valid SearchRequestDto searchRequest, Model model) {
        PaginationRequestDto paginationRequest = new PaginationRequestDto();
        paginationRequest.setOffset(searchRequest.getOffset());
        paginationRequest.setLimit(searchRequest.getLimit());
        paginationRequest.getFilters().put("stNames", searchRequest.getStNames());
        paginationRequest.getFilters().put("nPostal", searchRequest.getNPostal());
        paginationRequest.getFilters().put("flrAreaMin", searchRequest.getFlrAreaMin());
        paginationRequest.getFilters().put("flrAreaMax", searchRequest.getFlrAreaMax());
        paginationRequest.getFilters().put("contDateFrom", searchRequest.getContDateFrom());
        paginationRequest.getFilters().put("contDateTo", searchRequest.getContDateTo());

        model.addAttribute("result", masterTrnxService.search(paginationRequest));

        return "masterTrnx/index";
    }
}
