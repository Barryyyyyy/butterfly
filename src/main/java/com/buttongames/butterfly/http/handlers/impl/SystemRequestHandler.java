package com.buttongames.butterfly.http.handlers.impl;

import com.buttongames.butterfly.http.exception.InvalidRequestMethodException;
import com.buttongames.butterfly.http.handlers.BaseRequestHandler;
import com.buttongames.butterfly.util.CardIdUtils;
import com.buttongames.butterfly.xml.XmlUtils;
import com.buttongames.butterfly.xml.builder.KXmlBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;
import spark.Request;
import spark.Response;

/**
 * Handler for any requests that come to the <code>system</code> module.
 * @author skogaby (skogabyskogaby@gmail.com)
 */
@Component
public class SystemRequestHandler extends BaseRequestHandler {

    private final Logger LOG = LogManager.getLogger(SystemRequestHandler.class);

    /**
     * Handles an incoming request for the <code>system</code> module.
     * @param requestBody The XML document of the incoming request.
     * @param request The Spark request
     * @param response The Spark response
     * @return A response object for Spark
     */
    @Override
    public Object handleRequest(final Element requestBody, final Request request, final Response response) {
        final String requestMethod = request.attribute("method");

        if (requestMethod.equals("convcardnumber")) {
            return this.handleConvCardNumberRequest(requestBody, request, response);
        } else {
            throw new InvalidRequestMethodException();
        }
    }

    /**
     * Handles an incoming request for the <code>system.convcardnumber</code> module.
     * @param requestBody The XML document of the incoming request.
     * @param request The Spark request
     * @param response The Spark response
     * @return A response object for Spark
     */
    private Object handleConvCardNumberRequest(final Element requestBody, final Request request, final Response response) {
        final String cardId = XmlUtils.strValueAtPath(requestBody, "/system/data/card_id");
        final String convertedId = CardIdUtils.encodeCardId(cardId);

        // send a response
        final KXmlBuilder builder = KXmlBuilder.create("response")
                .e("system")
                    .s32("result", 0).up()
                    .e("data")
                        .str("card_number", convertedId);

        return this.sendResponse(request, response, builder);
    }
}