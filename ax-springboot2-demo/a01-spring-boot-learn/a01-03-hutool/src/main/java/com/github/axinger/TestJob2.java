package com.github.axinger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TestJob2 {

    public void run() {
        System.out.println("TestJob2 0/10 * * * * ?   每10秒一次");
    }
}
