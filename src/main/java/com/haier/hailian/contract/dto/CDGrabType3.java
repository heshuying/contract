package com.haier.hailian.contract.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CDGrabType3 {
    private String lqCode;
    private String xwType3Code;
    private String xwType3Name;
    private String sharePercent;
    private String grabCount = "0";
    private String countTotal = "0";
    private List<CDGrabDataDTO> grabList = new ArrayList<>();
}
