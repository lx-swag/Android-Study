package com.lx.contentprovier;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MyProvider extends ContentProvider {
    private DBHelper helper;
    private static UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        uriMatcher.addURI("com.lx.contentprovier.myprovider","/student",1);
        uriMatcher.addURI("com.lx.contentprovier.myprovider","/student/#",2);//#表示匹配任意数字
    }
    @Override
    public boolean onCreate() {
        Log.e("TAG", "onCreate()");
        helper = new DBHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Log.e("TAG", "query()");
        int code = uriMatcher.match(uri);
        SQLiteDatabase db = helper.getReadableDatabase();
        if (code == 1){
            Cursor cursor = db.query("student",projection,selection,selectionArgs,null,null,null);
            return cursor;
        }else if (code == 2){
            long id = ContentUris.parseId(uri);
            Cursor cursor = db.query("student",projection,"_id=?",new String[]{id+""},null,null,null);
            return cursor;
        }else {
            throw new IllegalArgumentException("不合法的uri");
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Log.e("TAG", "insert()");
        SQLiteDatabase db = helper.getReadableDatabase();
        if (uriMatcher.match(uri) == 1){
            long rawId = db.insert("student", null, values);
            db.close();
            return ContentUris.withAppendedId(uri,rawId);
        }else {
            db.close();
            throw new IllegalArgumentException("插入的uri不合法");
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.e("TAG", "delete()");
        SQLiteDatabase db = helper.getReadableDatabase();
        int code = uriMatcher.match(uri);
        if (code == 1){
            int count = db.delete("student", selection, selectionArgs);
            return count;
        }else if (code == 2){
            long id = ContentUris.parseId(uri);
            int count = db.delete("student", "_id=?", new String[]{id + ""});
            return count;
        }else {
            db.close();
            throw new IllegalArgumentException("删除uri不合法");
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.e("TAG", "update()");
        SQLiteDatabase db = helper.getReadableDatabase();
        int code = uriMatcher.match(uri);
        int count = -1;
        if (code == 1){
            count = db.update("student",values,selection,selectionArgs);
        }else if (code == 2){
            long id = ContentUris.parseId(uri);
            count = db.update("student",values,"_id="+id,null);
        }else {
            db.close();
            throw new IllegalArgumentException("更新的uri不合法");
        }
       return count;
    }
}
