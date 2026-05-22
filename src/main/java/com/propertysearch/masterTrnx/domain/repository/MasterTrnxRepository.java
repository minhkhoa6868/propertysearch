package com.propertysearch.masterTrnx.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.propertysearch.masterTrnx.domain.entity.MasterTrnxEntity;
import com.propertysearch.masterTrnx.dto.MasterTrnxDto;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MasterTrnxRepository extends JpaRepository<MasterTrnxEntity, Long> {

    @Query("""
        SELECT new com.propertysearch.masterTrnx.dto.MasterTrnxDto(
            m.recNum, m.projName, m.address, m.hseNum, m.stName, m.unitNum, m.flrArea, m.flrSf, m.areaType, m.consider,
            m.psm, m.psf, m.contDate, m.propertyType, m.tenure, m.completionDate, m.saleType, m.purAddType, m.district,
            m.nPostal, m.planningArea
        )
        FROM MasterTrnxEntity m
        WHERE
            (:#{#stNames == null || #stNames.isEmpty()} = true OR m.stName IN (:stNames))
            AND (:nPostal IS NULL OR :nPostal = '' OR m.nPostal = :nPostal)
            AND (:flrAreaMin IS NULL OR m.flrArea >= :flrAreaMin)
            AND (:flrAreaMax IS NULL OR m.flrArea <= :flrAreaMax)
            AND (:contDateFrom IS NULL OR CAST(m.contDate AS LocalDate) >= :contDateFrom)
            AND (:contDateTo IS NULL OR CAST(m.contDate AS LocalDate) <= :contDateTo)
        """)
    Page<MasterTrnxDto> searchByFilters(
            @Param("stNames") List<String> stNames,
            @Param("nPostal") String nPostal,
            @Param("flrAreaMin") Double flrAreaMin,
            @Param("flrAreaMax") Double flrAreaMax,
            @Param("contDateFrom") LocalDate contDateFrom,
            @Param("contDateTo") LocalDate contDateTo,
            Pageable pageable
    );

    @Query("""
        SELECT DISTINCT m.stName
        FROM MasterTrnxEntity m
        """)
    List<String> findDistinctStreetNames();
}
