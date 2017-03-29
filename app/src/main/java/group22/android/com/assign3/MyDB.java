package group22.android.com.assign3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;

import static group22.android.com.assign3.GlobalConstants.Accel_X;
import static group22.android.com.assign3.GlobalConstants.Accel_Y;
import static group22.android.com.assign3.GlobalConstants.Accel_Z;
import static group22.android.com.assign3.GlobalConstants.ActivityLabel;
import static group22.android.com.assign3.GlobalConstants.Activity_type;
import static group22.android.com.assign3.GlobalConstants.CREATE_TABLE;
import static group22.android.com.assign3.GlobalConstants.DELETE_TABLE;
import static group22.android.com.assign3.GlobalConstants.TableName;
import static group22.android.com.assign3.GlobalConstants.valueHolder;

/**
 * Created by Lakshmisagar on 3/22/2017.
 */

public class MyDB extends SQLiteOpenHelper {

    private static String TAG = MyDB.class.getName().toString();
    static Context context;
    public MyDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable();
        Log.d(TAG,"onCreate()");
        Log.d("CREATE_TABLE : ", CREATE_TABLE.toString());
        db.execSQL(CREATE_TABLE.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DELETE_TABLE);
        onCreate(db);
    }

    public static void createTable() {
        for(int i=0;i<50;i++){
            CREATE_TABLE.append(Accel_X + i + " REAL," + Accel_Y + i + " REAL, " + Accel_Z + i + " REAL, ");
        }
        CREATE_TABLE.append(ActivityLabel +" TEXT );");
    }
    public static void insertIntoDB(SQLiteDatabase db) {

        ContentValues values = new ContentValues();
        for (int i = 0; i < 50; i++) {
            values.put(Accel_X + i, valueHolder.get(i).getX());
            values.put(Accel_Y+ i, valueHolder.get(i).getY());
            values.put(Accel_Z+ i, valueHolder.get(i).getZ());
        }
        values.put(ActivityLabel, Activity_type);
        long newRowId = db.insert(TableName, null, values);
        if(newRowId>60)
        Toast.makeText(context.getApplicationContext(),"DB reached its limit "+newRowId+ " Please press Delete",Toast.LENGTH_LONG).show();
        Log.d(TAG+ "INSERT", " Value : " + newRowId);
    }

    public static void getDBData(SQLiteDatabase db){
        String mQuery = "SELECT  * FROM " + TableName;
        Cursor cursor = db.rawQuery(mQuery, null);
        Log.d(TAG,"COUNT DB"+cursor.getCount());
        String mFile =  context.getApplicationContext().getFilesDir()+"/"+ Activity_type+".txt";
        try {
            FileWriter fileWriter = new FileWriter(mFile, false);
            if (cursor.moveToFirst()) {
                int rowCount=0;
                do {
                    String tempActivity = cursor.getString(cursor.getColumnIndex(ActivityLabel)).trim();
                    StringBuilder mActivityRow = new StringBuilder();
                    if(tempActivity.equals(Activity_type)) {
                        rowCount++;
                        int j = 0;
                        for (int i = 0; i < 50; i++) {
                            String tempx = (++j) + ":" + cursor.getDouble(cursor.getColumnIndex(Accel_X + i));
                            String tempy = (++j) + ":" + cursor.getDouble(cursor.getColumnIndex(Accel_Y + i));
                            String tempz = (++j) + ":" + cursor.getDouble(cursor.getColumnIndex(Accel_Z + i));
                            mActivityRow.append(" " + tempx + " " + " " + tempy + " " + tempz);
                        }
                        Log.d(TAG,mActivityRow.toString());
                        fileWriter.write(mActivityRow.toString());
                        fileWriter.write("\n");
                    }
                } while (cursor.moveToNext());
                Toast.makeText(context.getApplicationContext(), rowCount, Toast.LENGTH_SHORT).show();
            }
            cursor.close();
            fileWriter.close();
        } catch(Exception e){
            Log.d(TAG,"Error "+e.toString());
        }
    }

    public static void getAllDataToFile(SQLiteDatabase db){
        String mQuery = "SELECT  * FROM " + TableName;
        Cursor cursor = db.rawQuery(mQuery, null);
        Log.d(TAG,"COUNT DB"+cursor.getCount());
        String mFile =  Environment.getExternalStorageDirectory()+"/Download/training_set.txt";
        File file = new File(mFile);
        file.delete();
        try {
            FileWriter fileWriter = new FileWriter(mFile, false);
            if (cursor.moveToFirst()) {
                int rowCount=0;
                do {
                    String tempActivity = cursor.getString(cursor.getColumnIndex(ActivityLabel)).trim();
                    StringBuilder mActivityRow = new StringBuilder();
                    if(tempActivity.equals("Walking")) {
                        mActivityRow.append("1");
                    }else if(tempActivity.equals("Running")) {
                        mActivityRow.append("2");
                    }else if(tempActivity.equals("Eating")) {
                        mActivityRow.append("3");
                    }
                        rowCount++;
                        int j = 0;

                        for (int i = 0; i < 50; i++) {
                            String tempx = (++j) + ":" + cursor.getDouble(cursor.getColumnIndex(Accel_X + i));
                            String tempy = (++j) + ":" + cursor.getDouble(cursor.getColumnIndex(Accel_Y + i));
                            String tempz = (++j) + ":" + cursor.getDouble(cursor.getColumnIndex(Accel_Z + i));
                            mActivityRow.append(" " + tempx + " " + " " + tempy + " " + tempz);
                        }
                        Log.d(TAG,mActivityRow.toString());
                        fileWriter.write(mActivityRow.toString());
                        fileWriter.write("\n");
                    //}
                } while (cursor.moveToNext());
                Toast.makeText(context.getApplicationContext(), rowCount, Toast.LENGTH_SHORT).show();
            }
            cursor.close();
            fileWriter.close();
        } catch(Exception e){
            Log.d(TAG,"Error "+e.toString());
        }
    }
}
