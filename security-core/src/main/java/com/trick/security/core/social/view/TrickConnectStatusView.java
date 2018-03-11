package com.trick.security.core.social.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("connect/status")
public class TrickConnectStatusView extends AbstractView {

    private final ObjectMapper objectMapper;

    @Autowired
    public TrickConnectStatusView(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, List<Connection<?>>> connections = (Map<String, List<Connection<?>>>) model.get("connectionMap");
        Map<String, Boolean> result = new HashMap<>();
        for (Map.Entry<String, List<Connection<?>>> entry : connections.entrySet()) {
            if (CollectionUtils.isNotEmpty(entry.getValue())) {
                result.put(entry.getKey(), true);
            }
        }
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}
