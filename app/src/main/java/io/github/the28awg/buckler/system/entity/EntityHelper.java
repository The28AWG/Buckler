package io.github.the28awg.buckler.system.entity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.github.the28awg.buckler.system.CommonHelper;
import io.github.the28awg.buckler.system.sql.Selection;
import io.github.the28awg.buckler.system.sql.Sql;

/**
 * Created by the28awg on 23.04.16.
 */
public class EntityHelper {

    private static final Logger logger = LoggerFactory.getLogger(EntityHelper.class);

    private static final Map<String, Constructor<? extends Entity>> constructorsCache = new ConcurrentHashMap<>();

    public static <T extends Entity> T entity(final Map<String, Object> attributes, final Class<T> clazz) {
        CommonHelper.assertNotNull("clazz", clazz);

        logger.debug("Building Entity based on {}", clazz);
        try {
            final Constructor<T> constructor = getConstructor(clazz);
            final T entity = constructor.newInstance();
            entity.addAttributes(attributes);
            logger.debug("Entity built: {}", entity);
            return entity;
        } catch (final Exception e) {
            logger.error("Cannot build instance", e);
        }
        return null;
    }

    public static <T extends Entity> T entity(final Class<T> clazz) {
        CommonHelper.assertNotNull("clazz", clazz);

        logger.debug("Building Entity based on {}", clazz);
        try {
            final Constructor<T> constructor = getConstructor(clazz);
            final T entity = constructor.newInstance();
            logger.debug("Entity built: {}", entity);
            return entity;
        } catch (final Exception e) {
            logger.error("Cannot build instance", e);
        }
        return null;
    }

    private static <T extends Entity> Constructor<T> getConstructor(final Class<T> clazz) throws Exception {
        Constructor<? extends Entity> constructor = constructorsCache.get(clazz.getName());
        if (constructor == null) {
            synchronized (constructorsCache) {
                constructor = constructorsCache.get(clazz.getName());
                if (constructor == null) {
                    constructor = clazz.getDeclaredConstructor();
                    constructorsCache.put(clazz.getName(), constructor);
                }
            }
        }
        return (Constructor<T>) constructor;
    }

    public static <T extends Entity> void insert(T entity) {
        if (entity.isNew()) {
            long id = Sql.sql().writableDatabase().insert(entity.definition().name(), null, Sql.to(entity.getAttributes()));
            Sql.sql().closeWritable();
            if (id == -1) {
                throw new RuntimeException(entity.toString());
            }
            entity.id(id);
        } else {
            throw new RuntimeException("not new entity: " + entity.toString());
        }
    }

    public static <T extends Entity> void insertTransaction(List<T> entity) {
        SQLiteDatabase database = Sql.sql().writableDatabase();
        database.beginTransaction();
        try {
            for (T tmp : entity) {
                if (tmp.isNew()) {
                    long id = database.insert(tmp.definition().name(), null, Sql.to(tmp.getAttributes()));
                    if (id == -1) {
                        throw new RuntimeException(tmp.toString());
                    }
                    tmp.id(id);
                } else {
                    throw new RuntimeException("not new entity: " + tmp.toString());
                }
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
            Sql.sql().closeWritable();
        }
    }

    @SafeVarargs
    public static <T extends Entity> void insertTransaction(T... entity) {
        SQLiteDatabase database = Sql.sql().writableDatabase();
        database.beginTransaction();
        try {
            for (T tmp : entity) {
                if (tmp.isNew()) {
                    long id = database.insert(tmp.definition().name(), null, Sql.to(tmp.getAttributes()));
                    if (id == -1) {
                        throw new RuntimeException(tmp.toString());
                    }
                    tmp.id(id);
                } else {
                    throw new RuntimeException("not new entity: " + tmp.toString());
                }
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
            Sql.sql().closeWritable();
        }
    }

    public static <T extends Entity> List<T> select(Selection selection, Class<T> entityClass) {
        List<T> entity = new ArrayList<>();

        T tmp = EntityHelper.entity(entityClass);
        if (tmp != null) {
            List<String> attributes = tmp.definition().attributes();
            Cursor cursor = Sql.sql().readableDatabase().query(tmp.definition().name(), attributes.toArray(new String[attributes.size()]), selection.selection(), selection.args(), null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    boolean next;
                    do {
                        if (tmp != null) {
                            tmp.addAttributes(Sql.from(cursor));
                            if (!tmp.isNew()) {
                                entity.add(tmp);
                            }
                        }
                        next = cursor.moveToNext();
                        if (next) {
                            tmp = EntityHelper.entity(entityClass);
                        }
                    } while (next);
                }
                cursor.close();
            }
            Sql.sql().closeReadable();
        }
        return entity;
    }
}