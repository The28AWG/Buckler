package io.github.the28awg.buckler;

import android.support.annotation.StringRes;

/**
 * Created by the28awg on 20.04.16.
 */
public interface InteractionListener {

    void title(@StringRes int title);

    void title(String title);

    String title();

    <T extends HelperFragment> T fragment(Class<T> fClass);

    <T extends HelperFragment> void show(Class<T> fClass);
}
