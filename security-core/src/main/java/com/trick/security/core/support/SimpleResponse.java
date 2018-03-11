package com.trick.security.core.support;

public class SimpleResponse {

    public SimpleResponse(String content) {
        this.content = content;
    }

    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
