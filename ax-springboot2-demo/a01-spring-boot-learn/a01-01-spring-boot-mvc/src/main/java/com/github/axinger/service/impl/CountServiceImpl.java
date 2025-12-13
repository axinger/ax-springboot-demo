package com.github.axinger.service.impl;

import com.github.axinger.service.CountService;
import org.springframework.stereotype.Service;

@Service
public class CountServiceImpl implements CountService {

    private int count;

    @Override
    public Integer count() {
        return count++;
    }
}
