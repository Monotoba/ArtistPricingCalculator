package com.example.randy.artistpricingcalculator;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;

/** Isolates only database names without depending on the removed android.test API. */
final class DatabaseTestContext extends ContextWrapper {
    private final String prefix;

    DatabaseTestContext(Context base, String prefix) {
        super(base);
        this.prefix = prefix;
    }

    @Override
    public File getDatabasePath(String name) {
        return getBaseContext().getDatabasePath(prefix + name);
    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode,
                                               SQLiteDatabase.CursorFactory factory) {
        return getBaseContext().openOrCreateDatabase(prefix + name, mode, factory);
    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode,
                                               SQLiteDatabase.CursorFactory factory,
                                               DatabaseErrorHandler errorHandler) {
        return getBaseContext().openOrCreateDatabase(prefix + name, mode, factory, errorHandler);
    }

    @Override
    public boolean deleteDatabase(String name) {
        return getBaseContext().deleteDatabase(prefix + name);
    }
}
