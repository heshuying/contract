package com.haier.hailian.contract.dto;

import lombok.Data;

import java.util.List;

@Data
public class ContractViewDataTYResultDTO {
    private String xwName;
    List<ContractViewDataTY> list;
}
