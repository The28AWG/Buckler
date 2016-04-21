package io.github.the28awg.buckler;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;

/**
 * Created by the28awg on 20.04.16.
 */
public class HelperFragment extends Fragment {
    private ProxyInteractionListener proxy = new ProxyInteractionListener(null);

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof InteractionListener) {
            proxy = new ProxyInteractionListener((InteractionListener) context);
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement InteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        proxy.release();
    }

    public InteractionListener interaction() {
        return proxy;
    }

    private static class ProxyInteractionListener implements InteractionListener {

        private final Object lock = new Object();
        private InteractionListener listener;

        public ProxyInteractionListener(InteractionListener listener) {
            this.listener = listener;
        }

        @Override
        public void title(@StringRes int title) {
            synchronized (lock) {
                if (listener != null) {
                    listener.title(title);
                }
            }
        }

        @Override
        public void title(String title) {
            synchronized (lock) {
                if (listener != null) {
                    listener.title(title);
                }
            }
        }

        @Override
        public String title() {
            synchronized (lock) {
                if (listener != null) {
                    return listener.title();
                }
                return "null";
            }
        }

        @Override
        public <T extends HelperFragment> T fragment(Class<T> fClass) {
            synchronized (lock) {
                if (listener != null) {
                    return listener.fragment(fClass);
                }
                return null;
            }
        }

        @Override
        public <T extends HelperFragment> void show(Class<T> fClass) {
            synchronized (lock) {
                if (listener != null) {
                    listener.show(fClass);
                }
            }
        }

        public void release() {
            synchronized (lock) {

            }
            listener = null;
        }
    }
}
