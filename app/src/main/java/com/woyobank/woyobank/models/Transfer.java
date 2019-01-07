package com.woyobank.woyobank.models;

import java.util.HashMap;
import java.util.Map;

public class Transfer {

    public String userId;
    public String targetId;
    public String transactionId;

    public Transfer() {

    }

    public Transfer(String userId, String targetId, String transactionId) {
        this.userId = userId;
        this.targetId = targetId;
        this.transactionId = transactionId;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("userId", userId);
        result.put("targetId", targetId);
        result.put("transactionId", transactionId);

        return result;
    }
}
