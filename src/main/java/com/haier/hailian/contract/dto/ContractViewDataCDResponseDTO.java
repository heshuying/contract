package com.haier.hailian.contract.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ContractViewDataCDResponseDTO {
    private String contractName;
    private String nodeCode;
    private String nodeName;

    private List<TargetViewDTO> targetList = new ArrayList<>();
}
