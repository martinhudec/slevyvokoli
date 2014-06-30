package io.discount.app.helpers;

import android.content.Context;
import android.database.MatrixCursor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Jan on 19.3.14.
 */
public class NetworkHelper {
    private static final String[] PRODUCTION_URL = new String[] {
                                                    "http://37.157.193.199:8485"
                                                    };

    private static final String DEVELOP_URL = "http://discount.apiary.io";

    private static final boolean IS_PRODUCTION = true;

    private static NetworkHelper instance = null;
    private static String[] urls = null;
    private String server = null;

    public NetworkHelper() {
        Collections.shuffle(Arrays.asList(PRODUCTION_URL));
        getServer();
    }

    public class NoConnectionException extends Exception {

    }

    public static synchronized NetworkHelper getInstance() {
        if(instance == null) {
            instance = new NetworkHelper();
        }
        return instance;
    }

    public String getServer() {
        if(!IS_PRODUCTION) {
            server = DEVELOP_URL;
        }
        int random = 0;
        if(PRODUCTION_URL.length >  1) {
            random = (int) (Math.random() * PRODUCTION_URL.length);
        }
        if(server == null) {
            server = PRODUCTION_URL[random];
        }
        return server;
    }

    /**
     * Request Something from the backend
     *
     * @param context           The applications context interested in the request. Can be null
     * @param apiURL            The Path to the API Method /auth/login for example
     * @param postedJsonObject  A json object if desired. Pass in null for get requests
     * @param jsonObjectHandler The Response JsonObject Handler dealing with the result
     */
    public void requestBackend(Context context, String apiURL, JsonObject postedJsonObject, FutureCallback jsonObjectHandler) {
        requestBackend(context, apiURL, postedJsonObject, jsonObjectHandler, false);
    }

    /**
     * Request Something from the backend
     *
     * @param context                The applications context interested in the request. Can be null
     * @param apiURL                 The Path to the API Method /auth/login for example
     * @param postedJsonObject       A json object if desired. Pass in null for get requests
     * @param jsonObjectHandler      The Response JsonObject Handler dealing with the result
     * @param addAuthenticationToken Should add the current logged in user token to the request or not
     */
    public void requestBackend(Context context, String apiURL, JsonObject postedJsonObject, FutureCallback jsonObjectHandler, boolean addAuthenticationToken) {
        if (context == null) {
            context = SharedApplication.getInstance().getApplicationContext();
        }

        if (addAuthenticationToken) {
            String userToken = SharedApplication.getInstance().getUserToken();
            if (userToken != null && userToken.length() > 0) {
                apiURL = apiURL + "?token=" + userToken;
            }
        }

        Builders.Any.B requestBuilder = Ion.with(context, this.server + apiURL);
        if (postedJsonObject != null) {
            requestBuilder.setJsonObjectBody(postedJsonObject);
        }

        requestBuilder.asJsonObject().setCallback(jsonObjectHandler);
    }

    public JsonElement requestBackendSynchronously(Context context, String apiURL, JsonObject postedJsonObject, boolean addAuthenticationToken) throws ExecutionException, InterruptedException {
        if (context == null) {
            context = SharedApplication.getInstance().getApplicationContext();
        }
        if (addAuthenticationToken) {
            String userToken = SharedApplication.getInstance().getUserToken();
            if (userToken != null && userToken.length() > 0) {
                apiURL = apiURL + "?token=" + userToken;
            }
        }
        Builders.Any.B requestBuilder = Ion.with(context, this.server + apiURL);

        if (postedJsonObject != null) {
            requestBuilder.setJsonObjectBody(postedJsonObject);
        }
        return requestBuilder.asJsonArray().get();
    }

    public void addJSONObjectToCursor(JSONObject object, MatrixCursor cursor) {
        List<Object> columnValues = new ArrayList<Object>();
        String[] columnNames = cursor.getColumnNames();
        for (String columnName : columnNames) {
            Object value;
            if (columnName.equalsIgnoreCase("id")) { // @TODO: use of DataType
                try {
                    String idString = object.getString(columnName);
                    value = Long.parseLong(idString);
                } catch (JSONException e) {
                    value = "";
                }
            }  else {
                try {
                    value = object.getString(columnName);
                } catch (JSONException e) {
                    value = "";
                }
            }
            columnValues.add(value);
        }
        cursor.addRow(columnValues);
    }
}

