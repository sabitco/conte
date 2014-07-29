package com.conte.common.ui.util;

import com.vaadin.Application;

/**
 * Clases de integracion Vaadin -Spring
 * The Class ApplicationHolder.
 */
public class ApplicationHolder {

    /** The app. */
    private static ThreadLocal<Application> app = new ThreadLocal<Application>();

    /**
     * Sets the application.
     *
     * @param application the new application
     */
    public static void setApplication(Application application) {
        app.set(application);
    }

    /**
     * Reset application.
     */
    public static void resetApplication() {
        app.remove();
    }

    /**
     * Gets the application.
     *
     * @return the application
     */
    public static Application getApplication() {
        return app.get();
    }
}
