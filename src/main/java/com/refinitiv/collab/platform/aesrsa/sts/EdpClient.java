package com.refinitiv.collab.platform.aesrsa.sts;

/*
 *  Copyright (c) 2019 by Refinitiv. All rights reserved.
 *
 *  No portion of this software in any form may be used or
 *  reproduced in any manner without written consent from
 *  Refinitiv
 */

//import com.refinitiv.analytics.reportingservice.config.SysConfig;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
//import org.aeonbits.owner.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HTTP client for requesting EDP API
 */
public class EdpClient {

    private static final Logger logger = LoggerFactory.getLogger(EdpClient.class);
    //    private static final SysConfig config = ConfigFactory.create(SysConfig.class);
    private static final String ACCEPT = "Accept";
    private static final String CONTENTTYPE = "ContentType";
    private static final String AUTHORIZATION = "Authorization";
    private static final String JSONTYPE = "application/json";

    private static final String APPID = "ff12cd3431f746d0b60d6df351db1ad230d1ee75";

    private static final String TOKEN_REQUEST_TEMPLATE =
            "grant_type=password"
                    + "&username=%s"
                    + "&password=%s"
                    + "&scope=trapi.messenger"
                    + "&client_id=%s"
                    + "&takeExclusiveSignOnControl=true";


    private static final String TOKEN_REFRESH_TEMPLATE =
            "grant_type=refresh_token"
                    + "&username=%s"
                    + "&refresh_token=%s"
                    + "&scope=trapi.messenger"
                    + "&client_id=%s";

    private final OkHttpClient httpClient;

    public EdpClient() {
//        final int timeout = config.getEdpApiTimeOut();
        final int timeout = 60;

        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(timeout, TimeUnit.SECONDS)
                .writeTimeout(timeout, TimeUnit.SECONDS)
                .readTimeout(timeout, TimeUnit.SECONDS)
                .build();
    }

    /**
     * Request EDP authentication token
     *
     * @param userName String
     * @param password String
     * @param appId    String
     * @return String
     * @throws IOException
     */
    public String requestToken(String userName, String password, String appId, String tokenUrl) throws IOException {
//        final String tokenUrl = config.getEdpApiUrl() + config.getEndpointOfAuthToken();
        RequestBody tokenBody = RequestBody.create(
                MediaType.parse("application/x-www-form-urlencoded; charset=utf-8"),
                String.format(TOKEN_REQUEST_TEMPLATE, userName, password, appId));

        Request request = new Request.Builder()
                .url(tokenUrl)
                .addHeader(ACCEPT, JSONTYPE)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .post(tokenBody)
                .build();

        return this.callRequest(request);
    }

    public String refreshToken(String userName, String refToken, String appId, String tokenUrl) throws IOException {
//        final String tokenUrl = config.getEdpApiUrl() + config.getEndpointOfAuthToken();
        RequestBody tokenBody = RequestBody.create(
                MediaType.parse("application/x-www-form-urlencoded; charset=utf-8"),
                String.format(TOKEN_REFRESH_TEMPLATE, userName, refToken, appId));

        Request request = new Request.Builder()
                .url(tokenUrl)
                .addHeader(ACCEPT, JSONTYPE)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .post(tokenBody)
                .build();

        return this.callRequest(request);
    }

    public String webInvoke(String stsToken, String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .addHeader(ACCEPT, JSONTYPE)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + stsToken)
                .get()
                .build();

        return this.callRequest(request);
    }

//    /**
//     * Request credentials for access AWS Service
//     *
//     * @param token
//     * @param endpoint
//     * @param bucketName
//     * @param region
//     * @param filePath   String sample value: "708/rept-srv-06ab76560a8ebbc3d6b0f3ebf96dc1241560155225654"
//     * @param jobId
//     * @return
//     * @throws IOException
//     */
//    public String requestCredentials(String token, String endpoint, String region, String bucketName,
//                                     String jobInfoFilePath) throws IOException {
//        final String ENDPOINT_PARAM = "endpoint";
//        final String BUCKETNAME_PARAM = "bucketName";
//        final String REGION_PARAM = "region";
//        final String FILEPATH_PARAM = "filePath";
//        final String apiUrl = config.getEdpApiUrl() + config.getEndpointOfAuthCredentials()
//                + "/?" + ENDPOINT_PARAM + "=" + endpoint
//                + "&" + BUCKETNAME_PARAM + "=" + bucketName
//                + "&" + REGION_PARAM + "=" + region
//                + "&" + FILEPATH_PARAM + "=" + jobInfoFilePath;
//
//        Request request = new Request.Builder()
//                .url(apiUrl)
//                .addHeader(ACCEPT, JSONTYPE)
//                .addHeader(AUTHORIZATION, token)
//                .get()
//                .build();
//
//        return this.callRequest(request);
//    }
//
//    /**
//     * Request user specific upload location in AWS S3 bucket
//     *
//     * @param token
//     * @return
//     * @throws IOException
//     */
//    public String requestUploadLocation(String reportType, String token) throws IOException {
//        final String apiUrl =
//                config.getEdpApiUrl() + config.getEndpointOfAnaltyicsReporting() + "/" + reportType
//                        .toLowerCase() + "/upload-location";
//
//        Request request = new Request.Builder()
//                .url(apiUrl)
//                .addHeader(ACCEPT, JSONTYPE)
//                .addHeader(AUTHORIZATION, token)
//                .post(RequestBody.create(null, ""))
//                .build();
//
//        return this.callRequest(request);
//    }
//
//    /**
//     * Issue HTTP POST request to AWS API Gateway for Run Job command
//     *
//     * @param token
//     * @param jobJsonBody
//     * @return
//     * @throws IOException
//     */
//    public String createJob(String reportType, String token, String jobJsonBody) throws IOException {
//        final String apiUrl =
//                config.getEdpApiUrl() + config.getEndpointOfAnaltyicsReporting() + "/" + reportType
//                        .toLowerCase() + "/jobs";
//
//        RequestBody jsonBody = RequestBody
//                .create(MediaType.parse("application/json; charset=utf-8"), jobJsonBody);
//
//        Request request = new Request.Builder()
//                .url(apiUrl)
//                .addHeader(CONTENTTYPE, JSONTYPE)
//                .addHeader(AUTHORIZATION, token)
//                .post(jsonBody)
//                .build();
//
//        return this.callRequest(request);
//    }
//
//    /**
//     * Issue HTTP GET request to AWS API gateway for Query Job Status command
//     *
//     * @param token
//     * @param jobId
//     * @return
//     * @throws IOException
//     */
//    public String requestJobStatus(String reportType, String token, String jobId) throws IOException {
//        final String apiUrl =
//                config.getEdpApiUrl() + config.getEndpointOfAnaltyicsReporting() + "/" + reportType
//                        .toLowerCase() + "/jobs/" + jobId;
//
//        Request request = new Request.Builder()
//                .url(apiUrl)
//                .addHeader(AUTHORIZATION, token)
//                .addHeader(CONTENTTYPE, JSONTYPE)
//                .get()
//                .build();
//
//        return this.callRequest(request);
//    }
//
//    /**
//     * Request report file location from CFS
//     *
//     * @param token   String
//     * @param cfsPath String
//     * @return
//     * @throws IOException
//     */
//    public String requestCFSReportLocation(String token, String cfsPath) throws IOException {
//        final String apiUrl = config.getEdpApiUrl() + cfsPath;
//
//        Request request = new Request.Builder()
//                .url(apiUrl)
//                .addHeader(ACCEPT, JSONTYPE)
//                .addHeader(AUTHORIZATION, token)
//                .get()
//                .build();
//
//        return this.callRequest(request);
//    }

    private String callRequest(Request request) throws IOException {
        int numRetries = 3;
        long sleepTime = 2000;
        Response response = null;
        while (numRetries > 0) {
            numRetries--;
            response = this.httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                break;
            } else {
                String responseBody = response.body().string();
                logger.error("Unexpected response: {}", responseBody);
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException ignored) {
                }
            }
        }
        if (numRetries <= 0) {
            throw new IOException("Exceeds max retries with Unexpected response: " + response);
        }
        return response.body().string();
    }

    public void close() {
        this.httpClient.dispatcher().executorService().shutdown();
        this.httpClient.connectionPool().evictAll();
    }
}

