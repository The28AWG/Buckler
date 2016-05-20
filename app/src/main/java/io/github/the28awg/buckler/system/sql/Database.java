package io.github.the28awg.buckler.system.sql;

import io.github.the28awg.buckler.system.CommonHelper;

/**
 * Created by the28awg on 23.04.16.
 */
public class Database {
    private String name;
    private Integer version = 1;

    private Database(String name, Integer version) {
        this.name = name;
        this.version = version;
    }

    private Database(String name) {
        this.name = name;
    }

    public static Database database(String name) {
        CommonHelper.assertNotNull("name", name);
        return new Database(name);
    }

    public static Database database(String name, Integer version) {
        CommonHelper.assertNotNull("name", name);
        CommonHelper.assertNotNull("version", version);
        CommonHelper.assertTrue(version > 0, "need version > 0");
        return new Database(name, version);
    }

    public String name() {
        return name;
    }

    public Integer version() {
        return version;
    }

    public Database version(Integer version) {
        CommonHelper.assertNotNull("version", version);
        CommonHelper.assertTrue(version > 0, "need version > 0");
        this.version = version;
        return this;
    }
}
