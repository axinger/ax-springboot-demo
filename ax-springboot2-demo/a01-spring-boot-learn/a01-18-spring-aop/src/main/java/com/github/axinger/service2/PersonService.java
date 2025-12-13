package com.github.axinger.service2;

import com.github.axinger.annotation.ApiVersion;

@ApiVersion
public interface PersonService {

    // @ApiVersion
    // 在接口中,不会被apo
    String add(String a, String b);

    String add2(String a, String b);
}
