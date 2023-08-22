package com.dayu.websocket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Message {
    private String toName;
    private Boolean isSystem;
    private String fromName;
    private Object message;

}
