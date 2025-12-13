package com.github.axinger.cmp;

import cn.hutool.core.util.ObjUtil;
import com.github.axinger.User;
import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.core.NodeComponent;


@LiteflowComponent(id = "c", name = "返回渠道c")
public class CCmp extends NodeComponent {

    @Override
    public void process() {
        User requestData = getRequestData();

        System.out.println("requestDataC = " + requestData);
        if (ObjUtil.equal(requestData.getName(), "c")) {
            User user = getFirstContextBean();
            user.setName("C");
            this.setIsEnd(true);
        }
    }
}
