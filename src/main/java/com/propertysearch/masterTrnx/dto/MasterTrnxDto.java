package com.propertysearch.masterTrnx.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MasterTrnxDto {
    private Long recNum;
    private String projName;
    private String address;
    private String hseNum;
    private String stName;
    private String unitNum;
    // private Double area;
    private Double flrArea;
    private Double flrSf;
    // private Double flrRate;
    // private Double flrRaSf;
    // private Double landArea;
    // private Double landSf;
    // private Double landRate;
    // private Double landRaSf;
    private String areaType;
    private Double consider;
    private Double psm;
    private Double psf;
    private LocalDateTime contDate;
    private String propertyType;
    private String tenure;
    // private String title;
    private String completionDate;
    private String saleType;
    private String purAddType;
    private String district;
    // private String sector;
    @JsonProperty("nPostal")
    private String nPostal;
    // private String planningRegion;
    private String planningArea;
    // private String postal;

    public String getConsiderFormatted() {
        return consider != null ? String.format("$%.2f", consider) : "";
    }
    public String getPsmFormatted() {
        return psm != null ? String.format("$%.2f", psm) : "";
    }
    public String getPsfFormatted() {
        return psf != null ? String.format("$%.2f", psf) : "";
    }

    public String getContDateFormatted() {
        return contDate != null 
            ? contDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) 
            : "";
    }
}
