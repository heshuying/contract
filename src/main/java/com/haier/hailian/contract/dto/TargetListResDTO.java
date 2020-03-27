package com.haier.hailian.contract.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
public class TargetListResDTO {
    private String factorCode;
    private String factorName;
    private String factorUnit;
    private String factorDirecton;
    // 计算逻辑
    private String computeLogic;

    private String targetXwCategoryCode;

    List<FactorGrabResDTO> grabList = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TargetListResDTO that = (TargetListResDTO) o;
        return factorName.equals(that.factorName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(factorName);
    }
}
