package io.discount.app.db;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import io.discount.app.helpers.DataType;

/**
 * Created by Jan on 23.3.14.
 */
public class DiscountDatabase extends SQLiteOpenHelper {
    private static final String TAG = "DiscountDatabase";

    private static final String DATABASE_NAME = "discount.db";
    private static final int DATABASE_VERSION = 1;

    public DiscountDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.i(TAG, "DB construct");
    }


    public class Tables {
        public class Discount {
            final static String TABLE = "discount";

            public class Columns {
                public final static String ID = "id";
                public final static String NAME = "name";
                public final static String TYPE = "type";
                public final static String ADDRESS = "address";
                public final static String LAT = "latitude";
                public final static String LNG = "longitude";
            }

            public class Types {
                final DataType ID = DataType.LONG;
                final DataType NAME = DataType.STRING;
            }
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "creating database");
        // nothing to do
        // create tables
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            Log.d(TAG, "onUpgrade() from " + oldVersion + " to " + newVersion);
            // nothing to do
            // drop tables
            // create tables
            onCreate(db);
        }

    }


}
