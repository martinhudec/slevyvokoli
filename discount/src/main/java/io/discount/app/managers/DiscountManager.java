package io.discount.app.managers;

import android.content.ContentProvider;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import io.discount.app.db.DiscountDatabase;
import io.discount.app.models.Discount;
import io.discount.app.providers.DiscountRestProvider;

/**
 * Created by Jan on 23.3.14.
 */
// @TODO: singleton?
public class DiscountManager implements IDiscountManager {
    private ContentProvider contentProvider = null;

    public DiscountManager(Context context) {
        this.contentProvider = new DiscountRestProvider(context);
    }

    @Override
    public Discount get() {
        Discount discount = null;
        Uri uri = Uri.parse(DiscountRestProvider.DISCOUNT_URI);
        String[] projection = new String[]{
                DiscountDatabase.Tables.Discount.Columns.ID ,
                DiscountDatabase.Tables.Discount.Columns.NAME
        };
        String selection = null;
        String[] selectionArgs = new String[]{};
        String sortOrder = "";

        Cursor result = this.contentProvider.query(uri, projection, selection, selectionArgs, sortOrder);
        if (result != null) {
            if (result.moveToFirst()) {
                discount = new Discount();
                discount.setName(result.getString(result.getColumnIndex(DiscountDatabase.Tables.Discount.Columns.NAME)));
            }
        }
        result.close();
        return discount;
    }

    @Override
    public ArrayList<Discount> getByGPS(String gps) {
        Uri uri = Uri.parse(DiscountRestProvider.DISCOUNT_ID_URI);
        String[] projection = new String[]{
                DiscountDatabase.Tables.Discount.Columns.ID,
                DiscountDatabase.Tables.Discount.Columns.NAME,
                DiscountDatabase.Tables.Discount.Columns.ADDRESS,
                DiscountDatabase.Tables.Discount.Columns.LAT,
                DiscountDatabase.Tables.Discount.Columns.LNG,
                DiscountDatabase.Tables.Discount.Columns.TYPE
        };

        String selection =  DiscountDatabase.Tables.Discount.Columns.ID + "='" + gps + "'";
        String[] selectionArgs = null;
        String sortOrder = "";

        Cursor result = this.contentProvider.query(uri, projection, selection, selectionArgs, sortOrder);
        ArrayList<Discount> list = new ArrayList<Discount>();
        if (result != null) {
            if (result.moveToFirst()) {
                do {
                    try {
                        Discount discount = new Discount();
                        discount.setId(result.getLong(result.getColumnIndex(DiscountDatabase.Tables.Discount.Columns.ID)));
                        discount.setName(result.getString(result.getColumnIndex(DiscountDatabase.Tables.Discount.Columns.NAME)));
                        discount.setAddress(result.getString(result.getColumnIndex(DiscountDatabase.Tables.Discount.Columns.ADDRESS)));
                        discount.setLongitude(Double.valueOf(result.getString(result.getColumnIndex(DiscountDatabase.Tables.Discount.Columns.LNG))));
                        discount.setLatitude(Double.valueOf(result.getString(result.getColumnIndex(DiscountDatabase.Tables.Discount.Columns.LAT))));
                        discount.setType(result.getString(result.getColumnIndex(DiscountDatabase.Tables.Discount.Columns.TYPE)));
                        list.add(discount);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } while (result.moveToNext());
            }
        }
        if (result != null) {
            result.close();
        }
        return list;
    }

    @Override
    public ArrayList<Discount> getCollection() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void update(Discount discount) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(Discount discount) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void create(Discount discount) {
        throw new UnsupportedOperationException();
    }
}
