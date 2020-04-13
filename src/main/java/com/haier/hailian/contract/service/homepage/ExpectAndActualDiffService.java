package com.haier.hailian.contract.service.homepage;


import com.haier.hailian.contract.dto.homepage.ExpectAndActualDiffDto;

import java.util.Map;

public interface ExpectAndActualDiffService {

    Map<String , Object> getChainGrabNum(ExpectAndActualDiffDto expectAndActualDiffDto);

}
