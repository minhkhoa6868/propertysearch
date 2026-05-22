package com.propertysearch.masterTrnx.mapper;

import org.springframework.stereotype.Component;

import com.propertysearch.masterTrnx.domain.entity.MasterTrnxEntity;
import com.propertysearch.masterTrnx.dto.MasterTrnxDto;

@Component
public class MasterTrnxMapper {
    public MasterTrnxDto toDto(MasterTrnxEntity entity) {
        if (entity == null) {
            return null;
        }

        MasterTrnxDto dto = new MasterTrnxDto();

        dto.setRecNum(entity.getRecNum());
        dto.setProjName(entity.getProjName());
        dto.setAddress(entity.getAddress());
        dto.setHseNum(entity.getHseNum());
        dto.setStName(entity.getStName());
        dto.setUnitNum(entity.getUnitNum());

        // dto.setArea(entity.getArea());
        dto.setFlrArea(entity.getFlrArea());
        dto.setFlrSf(entity.getFlrSf());
        // dto.setFlrRate(entity.getFlrRate());
        // dto.setFlrRaSf(entity.getFlrRaSf());

        // dto.setLandArea(entity.getLandArea());
        // dto.setLandSf(entity.getLandSf());
        // dto.setLandRate(entity.getLandRate());
        // dto.setLandRaSf(entity.getLandRaSf());

        dto.setAreaType(entity.getAreaType());

        dto.setConsider(entity.getConsider());

        dto.setPsm(entity.getPsm());
        dto.setPsf(entity.getPsf());

        dto.setContDate(entity.getContDate());

        dto.setPropertyType(entity.getPropertyType());
        dto.setTenure(entity.getTenure());
        // dto.setTitle(entity.getTitle());

        dto.setCompletionDate(entity.getCompletionDate());

        dto.setSaleType(entity.getSaleType());

        dto.setPurAddType(entity.getPurAddType());

        dto.setDistrict(entity.getDistrict());
        // dto.setSector(entity.getSector());

        dto.setNPostal(entity.getNPostal());

        // dto.setPlanningRegion(entity.getPlanningRegion());
        dto.setPlanningArea(entity.getPlanningArea());

        // dto.setPostal(entity.getPostal());

        return dto;
    }
}
