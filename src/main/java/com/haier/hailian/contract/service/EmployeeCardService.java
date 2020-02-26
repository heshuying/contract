package com.haier.hailian.contract.service;

import com.haier.hailian.contract.dto.LittleXWEmpDTO;

import java.util.List;
import java.util.Map;

public interface EmployeeCardService {
    List<LittleXWEmpDTO> getLittleXWEmp(Map<String,Object> paraMap);
}
