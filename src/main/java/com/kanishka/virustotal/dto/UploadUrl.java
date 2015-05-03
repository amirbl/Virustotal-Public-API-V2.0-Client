/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kanishka.virustotal.dto;

import com.google.gson.annotations.SerializedName;

/**
 * @author kdkanishka@gmail.com
 */
public class UploadUrl {

    @SerializedName("upload_url")
    private String uploadUrl;

    public UploadUrl() {
    }

    /**
     * @return unique id to identify the scan
     */
    public String getUploadUrl() {
        return uploadUrl;
    }

    public void setUploadUrl(String url) {
        this.uploadUrl = url;
    }

}
