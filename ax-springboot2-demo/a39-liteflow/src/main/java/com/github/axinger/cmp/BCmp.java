package com.github.axinger.cmp;

import cn.hutool.core.util.ObjUtil;
import com.github.axinger.User;
import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.annotation.LiteflowFact;
import com.yomahub.liteflow.annotation.LiteflowMethod;
import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.enums.LiteFlowMethodEnum;
import com.yomahub.liteflow.enums.NodeTypeEnum;


//@Component("b")
//public class BCmp extends NodeBooleanComponent {
//    @Override
//    public boolean processBoolean() throws Exception {
//
//        String name = getContextBean(String.class);
//
//        if (ObjectUtils.nullSafeEquals(name, "b")) {
//            return true;
//        }
//
//        return false;
//    }
//}

//@LiteflowComponent(id = "b", name = "返回渠道b")
//public class BCmp extends NodeComponent {
//
//    @Override
//    public void process() {
//
//        String requestData = getRequestData();
//
//        System.out.println("requestDataB = " + requestData);
//        if (ObjUtil.equal(requestData, "b")) {
//            User user = getContextBean(User.class);
//            user.setName("B");
//            this.setIsEnd(true);
//        }
//    }
//}

@LiteflowComponent
public class BCmp {

    @LiteflowMethod(nodeId = "b", nodeName = "返回渠道b", value = LiteFlowMethodEnum.PROCESS, nodeType = NodeTypeEnum.COMMON)
    public void processB(NodeComponent bindCmp, @LiteflowFact("user") User user) {
        User requestData = bindCmp.getRequestData();

        System.out.println("requestDataB = " + requestData);
        if (ObjUtil.equal(requestData.getName(), "b")) {
            user.setName("B");
            bindCmp.setIsEnd(true);
        }

//        throw new RuntimeException("异常===============");
    }
}


