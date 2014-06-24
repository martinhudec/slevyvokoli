package io.discount.app.helpers;

import android.content.ContentValues;
import org.json.JSONException;
import org.json.JSONObject;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jan on 23.3.14.
 */
public class ContentValuesToJsonObject<Db> {
    private Db db;
    private List<Field> dbFields = new ArrayList<Field>();

    public void JsonToObject() {
        // get static db fields
        Field[] declaredFields = this.db.getClass().getDeclaredFields();
        List<Field> staticFields = new ArrayList<Field>();
        for (Field field : declaredFields) {
            if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                staticFields.add(field);
            }
        }
    }

    public JSONObject toObject(ContentValues values) {
        JSONObject object = new JSONObject();
        try {
            for(Field field : dbFields) {
                object.put(field.getName(), values.get(field.getName()));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    public void setDb(Db db) {
        this.db = db;
    }

    public Db getDb() {
        return db;
    }
}
