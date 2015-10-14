/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kanishka.net.commons;

import com.kanishka.net.exception.RequestNotComplete;
import com.kanishka.net.model.FormData;
import com.kanishka.net.model.Header;
import com.kanishka.net.model.HttpStatus;
import com.kanishka.net.model.MultiPartEntity;
import com.kanishka.net.model.RequestMethod;
import com.kanishka.net.model.Response;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * @author kdkanishka@gmail.com
 */
public class BasicHTTPRequestImpl implements HTTPRequest {

    public BasicHTTPRequestImpl() {

    }

    public String downloadRequest(final String url, final RequestMethod requestMethod, final String destDir) throws IOException {
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        HttpURLConnection conn = null;

        String targetFile = null;
        try {

            URL urlObj = new URL(url);
            conn = (HttpURLConnection) urlObj.openConnection();
            conn.setRequestMethod(requestMethod.toString());

            // opens input stream from the HTTP connection
            inputStream = conn.getInputStream();

            // opens an output stream to save into file
            targetFile = destDir + File.separator + UUID.randomUUID().toString() + ".apktmp";
            outputStream = new FileOutputStream(targetFile);

            int bytesRead;
            byte[] buffer = new byte[4096];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } finally {
            if(outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                }
            }
            if(inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }
            if(conn != null) {
                conn.disconnect();
            }
        }
        return targetFile;

    }

    @Override
    public final Response request(final String url,
                                  final List<Header> reqHeaders,
                                  final List<FormData> formData,
                                  final RequestMethod requestMethod,
                                  final List<MultiPartEntity> multiParts
    ) throws RequestNotComplete, IOException {
        HttpStatus httpStatus = new HttpStatus();
        List<Header> respoHeaders = new ArrayList<Header>();
        StringBuilder response = new StringBuilder();
        Response responseWrapper;

        URL urlObj = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
        conn.setRequestMethod(requestMethod.toString());

        setRequestHeaders(reqHeaders, conn);

        //add multipart entities
        if (multiParts != null && multiParts.size() > 0) {
            addMultiparts(multiParts, conn);
        } else {
            //add form data to the request
            addFormData(formData, conn);
        }

        //fetch response headers
        fetchResponseHeaders(respoHeaders, conn);

        //Get Response
        try {
            getResponse(httpStatus, response, conn);
        } catch (Exception e) {
            httpStatus.setStatusCode(conn.getResponseCode());
            httpStatus.setStatusCode(conn.getResponseCode());
            httpStatus.setMessage(conn.getResponseMessage());
            throw new RequestNotComplete("Could not complete the request", e,
                    httpStatus);
        }
        responseWrapper = new Response(httpStatus.getStatusCode(),
                response.toString(), respoHeaders);
        return responseWrapper;
    }

    private void getResponse(HttpStatus httpStatus, StringBuilder response, HttpURLConnection conn) throws IOException {
        InputStream is = conn.getInputStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = rd.readLine()) != null) {
            response.append(line);
            response.append('\r');
        }
        httpStatus.setStatusCode(conn.getResponseCode());
        httpStatus.setStatusCode(conn.getResponseCode());
        httpStatus.setMessage(conn.getResponseMessage());
        is.close();
        rd.close();
        conn.disconnect();
    }

    private void fetchResponseHeaders(List<Header> respoHeaders, HttpURLConnection conn) {
        Map<String, List<String>> rowHeaders = conn.getHeaderFields();
        Iterator<String> keys = rowHeaders.keySet().iterator();
        while (keys.hasNext()) {
            String key = keys.next();
            List<String> vals = rowHeaders.get(key);
            if (vals.size() > 0) {
                respoHeaders.add(new Header(key, vals.get(0)));
            }
        }
    }

    private void addFormData(List<FormData> formData, HttpURLConnection conn) throws IOException {
        if (formData != null && formData.size() > 0) {
            Iterator<FormData> itrFormData = formData.iterator();
            StringBuilder content = new StringBuilder();
            while (itrFormData.hasNext()) {
                FormData formDataObj = itrFormData.next();
                content.append(URLEncoder.encode(formDataObj.getKey(),
                        "UTF-8"));
                content.append("=");
                content.append(URLEncoder.encode(formDataObj.getValue(),
                        "UTF-8"));
                content.append("&");
            }
            if (content.length() > 0) {
                conn.setDoOutput(true);
                //Send request
                DataOutputStream wr =
                        new DataOutputStream(conn.getOutputStream());
                wr.writeBytes(content.toString());
                wr.flush();
                wr.close();
            }
        }
    }

    private void addMultiparts(List<MultiPartEntity> multiParts, HttpURLConnection conn) throws IOException {
        MultipartEntity multipartEntity =
                new MultipartEntity(HttpMultipartMode.STRICT);
        for (MultiPartEntity part : multiParts) {
            multipartEntity.addPart(part.getPartName(), part.getEntity());
        }
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type",
                multipartEntity.getContentType().getValue());

        //try to write to the output stream of the connection
        OutputStream outStream = conn.getOutputStream();
        multipartEntity.writeTo(outStream);
        outStream.close();
    }

    private void setRequestHeaders(List<Header> reqHeaders, HttpURLConnection conn) {
        if (reqHeaders != null && reqHeaders.size() > 0) {
            Iterator<Header> itrHeaders = reqHeaders.iterator();
            while (itrHeaders.hasNext()) {
                Header reqHdr = itrHeaders.next();
                conn.setRequestProperty(reqHdr.getKey(), reqHdr.getValue());
            }
        }
    }
}
