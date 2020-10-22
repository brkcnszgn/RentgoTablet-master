package com.creatifsoftware.filonova.utils;

/**
 * Created by kerembalaban on 2019-07-19 at 21:01.
 */
public class ConnectionUtils {

    public static final ConnectionUtils instance = new ConnectionUtils();
    /* LIVE */
    private static final String CRM_API_LIVE_URL = "http://newmongodb.rentgo.com:6060/api/service/";
    private static final String HGS_API_LIVE_URL = "http://newmongodb.rentgo.com:6060/api/fine/";
    private static final String CRM_API_LIVE_USER_NAME = "serviceclient";
    private static final String CRM_API_LIVE_PASSWORD = "123";
    private static final String BLOB_STORAGE_LIVE_URL = "https://rentgoproductionstorage.blob.core.windows.net/";
    private static final String BLOB_STORAGE_LIVE_CONNECTION_STRING = "DefaultEndpointsProtocol=https;"
            + "AccountName=rentgoproductionstorage;"
            + "AccountKey=d3zPZ07IAZROSuLKQDYHH9jPzrqz8GJMGbbUojhSJONNDPEc8mpyloGZnrOoTDGATqrx+rpyU2k7RWZ7ZiZ5kw==;"
            + "EndpointSuffix=core.windows.net";
    /* DEVELOPMENT */
    private static final String CRM_API_DEV_URL = "http://mongodb.rentgo.com:6060/api/service/";
    private static final String HGS_API_DEV_URL = "http://mongodb.rentgo.com:6060/api/fine/";
    private static final String CRM_API_DEV_USER_NAME = "serviceclient";
    private static final String CRM_API_DEV_PASSWORD = "123";
    private static final String BLOB_STORAGE_DEV_URL = "https://rentgostorage.blob.core.windows.net/";
    private static final String BLOB_STORAGE_DEV_CONNECTION_STRING = "DefaultEndpointsProtocol=https;"
            + "AccountName=rentgostorage;"
            + "AccountKey=w+17Rp3M3WxTs+VAfDnMGkhYVZY/QwLaZCIEWKZtSU2Ld34yz1PCkuYtXqwowkXmAFBdr+x9KgqaWwz5DU6AMA==;"
            + "EndpointSuffix=core.windows.net";

    public String getLiveCrmApiConnectionUrl() {
        return CRM_API_LIVE_URL;
    }

    public String getLiveHgsApiConnectionUrl() {
        return HGS_API_LIVE_URL;
    }

    public String getDevHgsApiConnectionUrl() {
        return HGS_API_DEV_URL;
    }

    public String getLiveBlobStorageConnectionString() {
        return BLOB_STORAGE_LIVE_CONNECTION_STRING;
    }

    public String getLiveCrmApiUsername() {
        return CRM_API_LIVE_USER_NAME;
    }

    public String getLiveCrmApiPassword() {
        return CRM_API_LIVE_PASSWORD;
    }

    public String getLiveBlobStorageUrl() {
        return BLOB_STORAGE_LIVE_URL;
    }

    public String getDevCrmApiConnectionUrl() {
        return CRM_API_DEV_URL;
    }

    public String getDevBlobStorageConnectionString() {
        return BLOB_STORAGE_DEV_CONNECTION_STRING;
    }

    public String getDevCrmApiUsername() {
        return CRM_API_DEV_USER_NAME;
    }

    public String getDevCrmApiPassword() {
        return CRM_API_DEV_PASSWORD;
    }

    public String getDevBlobStorageUrl() {
        return BLOB_STORAGE_DEV_URL;
    }
}
