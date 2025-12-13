package com.github.axinger.cmp;

import cn.hutool.core.util.ObjUtil;
import com.github.axinger.User;
import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.core.NodeComponent;

@LiteflowComponent(id = "a", name = "返回渠道a")
public class ACmp extends NodeComponent {

    @Override
    public void process() {
        //do your business


        User requestData = getRequestData();


        System.out.println("requestData = " + requestData);

        if (ObjUtil.equal(requestData.getName(), "a")) {
            User user = getContextBean(User.class);
            user.setName("A");
            this.setIsEnd(true);
        }


    }
}
