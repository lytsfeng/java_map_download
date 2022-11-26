package com.jmd.web.websocket.handler;

import com.alibaba.fastjson2.JSON;
import com.jmd.web.common.WsSendData;
import com.jmd.web.websocket.handler.base.BaseWebSocketHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;

@Component
public class MapWebSocketHandler extends BaseWebSocketHandler {

    public void send(WsSendData data) {
        for (String key : webSocketSessionMap.keySet()) {
            sendMessageToUser(key, new TextMessage(JSON.toJSONString(data)));
        }
    }

}
