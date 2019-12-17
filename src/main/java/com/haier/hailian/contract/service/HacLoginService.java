package com.haier.hailian.contract.service;

import com.haier.hailian.contract.dto.HacLoginDto;
import com.haier.hailian.contract.dto.R;

/**
 * Created by 19012964 on 2019/12/17.
 */
public interface HacLoginService {
    R login(HacLoginDto loginDto);
}
