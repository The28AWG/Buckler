package io.github.the28awg.buckler.system.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Parcel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import io.github.the28awg.buckler.system.AssetUtils;
import io.github.the28awg.buckler.system.CommonHelper;

/**
 * Created by the28awg on 23.04.16.
 */
public class Sql {

    private static final Logger logger = LoggerFactory.getLogger(Sql.class);
    private static final String SQL_DIR = "sql";
    private static final String CREATE_FILE = "create.sql";
    private static final String UPGRADE_FILE_PREFIX = "upgrade-";
    private static final String UPGRADE_FILE_SUFFIX = ".sql";

    private Helper helper;
    private AtomicInteger mWritableOpenCounter = new AtomicInteger();
    // доступная для чтения и записи база, более медленная
    private SQLiteDatabase writableDatabase;
    private AtomicInteger mReadableOpenCounter = new AtomicInteger();
    // более быстрая база, но доступная только для чтения
    private SQLiteDatabase readableDatabase;

    private Sql() {

    }

    public static Sql sql() {
        return Holder.SQL;
    }

    public static ContentValues to(Map<String, Object> attributes) {
        CommonHelper.assertNotNull("attributes", attributes);
        Parcel parcel = Parcel.obtain();
        parcel.writeMap(attributes);
        parcel.setDataPosition(0);
        return ContentValues.CREATOR.createFromParcel(parcel);
    }

    public static Map<String, Object> from(Cursor cursor) {
        CommonHelper.assertNotNull("cursor", cursor);
        Map<String, Object> attributes = new HashMap<>();
        for (String name : cursor.getColumnNames()) {
            attributes.put(name, cursor.getString(cursor.getColumnIndex(name)));
        }
        return attributes;
    }

    public void initialize(Context context, Database database) {
        if (this.helper == null) {
            this.helper = new Helper(context, database);
        }
    }

    public SQLiteDatabase writableDatabase() {
        if (mWritableOpenCounter.incrementAndGet() == 1) {
            writableDatabase = helper.getWritableDatabase();
        }
        if (!writableDatabase.isOpen()) {
            writableDatabase = helper.getWritableDatabase();
        }
        return writableDatabase;
    }

    public SQLiteDatabase readableDatabase() {
        if (mReadableOpenCounter.incrementAndGet() == 1) {
            readableDatabase = helper.getReadableDatabase();
        }
        if (!readableDatabase.isOpen()) {
            readableDatabase = helper.getReadableDatabase();
        }
        return readableDatabase;
    }

    public void closeWritable() {
        if (mWritableOpenCounter.decrementAndGet() == 0) {
            if (writableDatabase != null && writableDatabase.isOpen()) {
                writableDatabase.close();
                writableDatabase = null;
            }
        }
    }

    public void closeReadable() {
        if (mReadableOpenCounter.decrementAndGet() == 0) {
            if (readableDatabase != null && readableDatabase.isOpen()) {
                readableDatabase.close();
                readableDatabase = null;
            }
        }
    }

    public Database database() {
        return helper.database;
    }

    private static class Holder {
        private static final Sql SQL = new Sql();
    }

    private class Helper extends SQLiteOpenHelper {

        private Database database;
        private Context context;

        public Helper(Context context, Database database) {
            super(context, database.name(), null, database.version());
            this.context = context;
            this.database = database;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                logger.info("create database");
                execSqlFile(CREATE_FILE, db);
            } catch (IOException exception) {
                throw new RuntimeException("Database creation failed", exception);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                logger.info("upgrade database from {} to {}", oldVersion, newVersion);
                for (String sqlFile : AssetUtils.list(SQL_DIR, this.context.getAssets())) {
                    if (sqlFile.startsWith(UPGRADE_FILE_PREFIX)) {
                        int fileVersion = Integer.parseInt(sqlFile.substring(UPGRADE_FILE_PREFIX.length(), sqlFile.length() - UPGRADE_FILE_SUFFIX.length()));
                        if (fileVersion > oldVersion && fileVersion <= newVersion) {
                            execSqlFile(sqlFile, db);
                        }
                    }
                }
            } catch (IOException exception) {
                throw new RuntimeException("Database upgrade failed", exception);
            }
        }

        protected void execSqlFile(String sqlFile, SQLiteDatabase db) throws SQLException, IOException {
            logger.info("  exec sql file: {}", sqlFile);
            for (String sqlInstruction : SqlParser.parseSqlFile(SQL_DIR + "/" + sqlFile, this.context.getAssets())) {
                logger.trace("    sql: {}", sqlInstruction);
                db.execSQL(sqlInstruction);
            }
        }
    }
}
