package com.propertysearch.masterTrnx.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "MasterTrnx", schema = "dbo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MasterTrnxEntity {
    @Id
    @Column(name = "rec_num", precision = 18, scale = 0)
    private Long recNum;

    @Column(name = "proj_name", length = 500)
    private String projName;

    @Column(name = "Address", length = 255)
    private String address;

    @Column(name = "Hse_num", length = 100)
    private String hseNum;

    @Column(name = "St_name", length = 500)
    private String stName;

    @Column(name = "unit_num", length = 200)
    private String unitNum;

    @Column(name = "Area")
    private Double area;

    @Column(name = "Flr_area")
    private Double flrArea;

    @Column(name = "Flr_sf")
    private Double flrSf;

    @Column(name = "Flr_rate")
    private Double flrRate;

    @Column(name = "Flr_ra_sf")
    private Double flrRaSf;

    @Column(name = "Land_area")
    private Double landArea;

    @Column(name = "Land_sf")
    private Double landSf;

    @Column(name = "Land_rate")
    private Double landRate;

    @Column(name = "Land_ra_sf")
    private Double landRaSf;

    @Column(name = "Area_Type", nullable = false, length = 100)
    private String areaType;

    @Column(name = "Consider")
    private Double consider;

    @Column(name = "PSM")
    private Double psm;

    @Column(name = "PSF")
    private Double psf;

    @Column(name = "Cont_date")
    private LocalDateTime contDate;

    @Column(name = "Property_type", length = 200)
    private String propertyType;

    @Column(name = "Tenure", length = 250)
    private String tenure;

    @Column(name = "Title", length = 100)
    private String title;

    @Column(name = "Completion_date", length = 100)
    private String completionDate;

    @Column(name = "Sale_type", length = 100)
    private String saleType;

    @Column(name = "Pur_Add_type", length = 100)
    private String purAddType;

    @Column(name = "District", length = 2)
    private String district;

    @Column(name = "Sector", length = 50)
    private String sector;

    @Column(name = "n_postal", length = 6)
    private String nPostal;

    @Column(name = "Planning_Region", length = 200)
    private String planningRegion;

    @Column(name = "Planning_Area", length = 200)
    private String planningArea;

    @Column(name = "postal", length = 100)
    private String postal;
}
