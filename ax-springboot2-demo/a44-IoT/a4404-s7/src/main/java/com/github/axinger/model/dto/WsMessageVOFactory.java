package com.github.axinger.model.dto;


import com.github.axinger.model.keys.WsCmdKeys;

public class WsMessageVOFactory {

    public static WsMessageVO<String> successTip(String msg) {
        WsMessageVO<String> vo = new WsMessageVO<>();
        vo.setCode(200);
        vo.setCmd(WsCmdKeys.PLC_TIP_SUCCESS);
        vo.setData(msg);
        return vo;
    }

    public static WsMessageVO<String> errorTip(String msg) {
        WsMessageVO<String> vo = new WsMessageVO<>();
        vo.setCode(200);
        vo.setCmd(WsCmdKeys.PLC_TIP_ERROR);
        vo.setData(msg);
        return vo;
    }
}
