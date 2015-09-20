/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kanishka.net.commons;

import com.kanishka.net.exception.RequestNotComplete;
import com.kanishka.net.model.FormData;
import com.kanishka.net.model.Header;
import com.kanishka.net.model.MultiPartEntity;
import com.kanishka.net.model.RequestMethod;
import com.kanishka.net.model.Response;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.List;

/**
 * @author kdkanishka@gmail.com
 */
public interface HTTPRequest {

    Response request(String urlStr, List<Header> reqHeaders,
                     List<FormData> formData,
                     RequestMethod requestMethod,
                     List<MultiPartEntity> multiParts) throws
            RequestNotComplete, IOException;

    void downloadRequest(final String url, final RequestMethod requestMethod, final String destFile) throws IOException;
}
