package com.haier.hailian.contract.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Data
public class ContractViewDataTYResultDTO {
    private String xwName;
    private int diff = 0;
    List<TargetConfigDTO> targetList;

    public static void main(String[] args) {
        List<ContractViewDataTYResultDTO> list = new ArrayList<>();
        ContractViewDataTYResultDTO data = new ContractViewDataTYResultDTO();
        data.setDiff(1);
        list.add(data);
        ContractViewDataTYResultDTO data2 = new ContractViewDataTYResultDTO();
        data2.setDiff(2);
        list.add(data2);
        ContractViewDataTYResultDTO data3 = new ContractViewDataTYResultDTO();
        data3.setDiff(2);
        list.add(data3);
        ContractViewDataTYResultDTO data4 = new ContractViewDataTYResultDTO();
        data4.setDiff(3);
        list.add(data4);
        Collections.sort(list, new Comparator<ContractViewDataTYResultDTO>() {
            @Override
            public int compare(ContractViewDataTYResultDTO o1, ContractViewDataTYResultDTO o2) {
                return o2.getDiff() - o1.getDiff();
            }
        });
        System.out.println(list);
    }
}
