package io.github.the28awg.buckler.system.sql;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by the28awg on 20.05.16.
 */
public class Selection {
    private String selection;
    private List<String> args;

    public Selection(String selection) {
        this.selection = selection;
        args = new ArrayList<>();
    }

    public static Selection selection(String selection) {
        return new Selection(selection);
    }

    public String selection() {
        return selection;
    }

    public Selection args(String arg) {
        this.args.add(arg);
        return this;
    }

    public Selection args(int location, String arg) {
        this.args.add(location, arg);
        return this;
    }

    public String[] args() {
        return args.toArray(new String[args.size()]);
    }
}