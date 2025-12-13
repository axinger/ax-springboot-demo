package com.github.axinger.controller;


import com.github.axinger.model.dto.DateVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xing
 */
@RestController
@RequestMapping("/number")
public class DateVOController {
    /*
{
  "date" : "2025-01-01",
  "date1" : "2025-02",
  "date2" : "2025-02-02 00:00:00"
}

{
"date": "2025-01-01 00:00:00",
"date2": "2025-02-02T00:00:00",
"date1": "2025-02-01 00:00:00"
}
     */
    @PostMapping("/1")
    public Object test1(@RequestBody DateVO model) {
        Map<String, Object> map = new HashMap<>(16);
//        map.put("amount", model.getTotal()+"");
//        map.put("percent", model.getPercent());
//        map.put("money", model.getMoney());
        map.put("date", model.getDate());
        map.put("date1", model.getDate1());
        map.put("date2", model.getDate2());
        return map;
    }


}
