package io.discount.app.providers;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import io.discount.app.helpers.NetworkHelper;

/**
 * Created by Jan on 23.3.14.
 */
public class DiscountRestProvider extends ContentProvider {
    private static final String AUTHORITY = "io.discount.app.DiscountRestProvider";

    public static final int DISCOUNT = 100;
    public static final int DISCOUNT_ID = 101;

    public static final String DISCOUNT_URI = "content://" + DiscountRestProvider.AUTHORITY + "/places";
    public static final String DISCOUNT_ID_URI = "content://" + DiscountRestProvider.AUTHORITY + "/places/";

    protected static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(AUTHORITY, "places", DISCOUNT);
        sURIMatcher.addURI(AUTHORITY, "places/*/*/*", DISCOUNT_ID);
    }

    private HttpClient client = new DefaultHttpClient();
    private Context context;
    private NetworkHelper networkHelper = null;

    public DiscountRestProvider(Context context) {
        this.context = context;
        this.networkHelper = NetworkHelper.getInstance();
    }

    @Override
    public String getType(Uri uri) {
        switch (sURIMatcher.match(uri)) {
            case DISCOUNT:
                return ContentResolver.CURSOR_DIR_BASE_TYPE + "/io.discount.app.discount";
            case DISCOUNT_ID:
                return ContentResolver.CURSOR_DIR_BASE_TYPE + "/io.discount.app.discount_id";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    };

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        StringBuilder baseURL = new StringBuilder("");

        JsonObject selectionJsonObject = new JsonObject();
        switch (sURIMatcher.match(uri)) {
            case DISCOUNT_ID:
                String gps;
                baseURL.append("/places");
                baseURL.append("/" + selectionArgs[0]);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        Log.i("gymradio", baseURL.toString());

        JSONObject object;
        MatrixCursor cursor = new MatrixCursor(projection);

        try {
            JsonElement receivedObject = networkHelper.requestBackendSynchronously(this.context, baseURL.toString(), null, false);
            //Log.i("gymradio", receivedObject.toString());
            if (receivedObject.isJsonArray()) {
                for (JsonElement element : receivedObject.getAsJsonArray()) {
                    //Log.i("gymradio2", element.toString());
                    object = new JSONObject(element.toString());
                    networkHelper.addJSONObjectToCursor(object, cursor);
                }
            } else {
                //Log.i("gymradio3", receivedObject.toString());
                object = new JSONObject(receivedObject.toString());
                networkHelper.addJSONObjectToCursor(object, cursor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException();
    }
}

