package com.github.axinger;

import io.reactivex.rxjava3.core.Observable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
public class RxController {

    @GetMapping("/numbers")
    public Observable<Integer> getNumbers() {
        return Observable.interval(1, TimeUnit.SECONDS)
                .map(tick -> tick.intValue() + 1)
                .take(10);
    }
}
