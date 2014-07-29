//package com.conte.common.ui.util;
//
//import java.util.Locale;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.context.i18n.LocaleContextHolder;
//
//import com.vaadin.Application;
//import com.vaadin.terminal.gwt.server.HttpServletRequestListener;
//
///**
// * Clases de integracion Vaadin -Spring
// * The BaseApplication class to extend.
// */
//public abstract class BaseApplication extends Application implements HttpServletRequestListener {
//
//    /** The Constant serialVersionUID. */
//    private static final long serialVersionUID = 1L;
//
//    /**
//     * @see com.vaadin.Application#getLocale()
//     */
//    @Override
//    public Locale getLocale() {
//        return LocaleContextHolder.getLocale();
//    }
//
//    /**
//     * @see com.vaadin.Application#setLocale(java.util.Locale)
//     */
//    @Override
//    public void setLocale(Locale locale) {
//        LocaleContextHolder.setLocale(locale);
//    }
//
//    /**
//     * @see com.vaadin.terminal.gwt.server.HttpServletRequestListener#onRequestStart(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
//     */
//    public final void onRequestStart(HttpServletRequest request, HttpServletResponse response) {
//        ApplicationHolder.setApplication(this);
//        requestStart(request, response);
//    }
//
//    /**
//     * @see com.vaadin.terminal.gwt.server.HttpServletRequestListener#onRequestEnd(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
//     */
//    public final void onRequestEnd(HttpServletRequest request, HttpServletResponse response) {
//        try {
//            requestEnd(request, response);
//        } finally {
//            ApplicationHolder.resetApplication();
//        }
//    }
//
//    /**
//     * Request end.
//     *
//     * @param request the request
//     * @param response the response
//     */
//    public void requestEnd(HttpServletRequest request, HttpServletResponse response) {
//    }
//
//    /**
//     * Request start.
//     *
//     * @param request the request
//     * @param response the response
//     */
//    public void requestStart(HttpServletRequest request, HttpServletResponse response) {
//    }
//
//}
