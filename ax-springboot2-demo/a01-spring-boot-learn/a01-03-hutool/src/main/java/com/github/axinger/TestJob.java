package com.github.axinger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TestJob {
    public void run() {
        System.out.println("TestJob 0/5 * * * * ?   每5秒一次");
    }
}
