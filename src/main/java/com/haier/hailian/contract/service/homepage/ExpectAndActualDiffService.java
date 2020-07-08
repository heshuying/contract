package com.haier.hailian.contract.service.homepage;


import com.haier.hailian.contract.dto.homepage.ExpectAndActualDiffDto;

import java.util.List;
import java.util.Map;

public interface ExpectAndActualDiffService {

    Map<String , Object> getChainGrabInfo(ExpectAndActualDiffDto expectAndActualDiffDto);

    Map<String , Object> getGrabInfo(ExpectAndActualDiffDto expectAndActualDiffDto);

    Map<String , Object> getTYGrabInfo(ExpectAndActualDiffDto expectAndActualDiffDto);

    List<Map<String, Object>> grabStarMap(ExpectAndActualDiffDto expectAndActualDiffDto);

}
