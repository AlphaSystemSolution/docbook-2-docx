package com.alphasystem.docbook.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author sali
 */
public abstract class HandlerFactory<H extends Handler> {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    Map<String, H> handlers = Collections.synchronizedMap(new HashMap<>());

    public void registerHandler(String key, H handler) {
        if (handler == null || key == null) {
            return;
        }
        // service handler loads service from the resources of the client jar then it comes to docbook-2-docx
        // we would like to honour handler(s) from the client first, so that client can override default handler(s)
        final H h = handlers.get(key);
        if (h == null) {
            logger.info("Loading handler: \"{}={}\"", key, handler.getClass().getName());
            handlers.put(key, handler);
        }
    }

    public abstract H getHandler(String key);
}
