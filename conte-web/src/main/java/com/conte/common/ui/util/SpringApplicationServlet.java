//
//package com.conte.common.ui.util;
//
//import java.io.IOException;
//import java.util.Locale;
//
//import javax.servlet.ServletConfig;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletRequestWrapper;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.beans.factory.NoSuchBeanDefinitionException;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.i18n.LocaleContextHolder;
//import org.springframework.web.context.WebApplicationContext;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//import org.springframework.web.context.support.WebApplicationContextUtils;
//import org.springframework.web.servlet.DispatcherServlet;
//import org.springframework.web.servlet.LocaleResolver;
//import org.springframework.web.servlet.i18n.SessionLocaleResolver;
//
//import com.vaadin.Application;
//import com.vaadin.terminal.gwt.server.AbstractApplicationServlet;
//import com.vaadin.ui.Window;
//import java.io.BufferedWriter;
//import javax.servlet.http.HttpSession;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
///**
// * Clases de integracion Vaadin -Spring
// * The Class SpringApplicationServlet.
// */
//public class SpringApplicationServlet extends AbstractApplicationServlet {
//
//    /** The Constant serialVersionUID. */
//    private static final long            serialVersionUID = 1L;
//
//    /** The application context. */
//    private WebApplicationContext        applicationContext;
//
//    /** The application class. */
//    private Class<? extends Application> applicationClass;
//
//    /** The application bean. */
//    private String                       applicationBean;
//
//    /** The locale resolver. */
//    private LocaleResolver               localeResolver;
//
//    private final Logger logger = LoggerFactory.getLogger(getClass());
//
//    @Override
//    protected void writeAjaxPageHtmlVaadinScripts(Window window,
//            String themeName, Application application,
//            final BufferedWriter page, String appUrl, String themeUri,
//            String appId, HttpServletRequest request) throws ServletException,
//            IOException {
//
//      super.writeAjaxPageHtmlVaadinScripts(window, themeName, application, page, appUrl, themeUri, appId, request);
//
//      HttpSession session = request.getSession(false);
//      int timeOut = session == null ? 30000 : session.getMaxInactiveInterval() * 1000; // milliseconds
//      String destinationUrl = "start/login?error=expired";
//      logger.debug("session.getMaxInactiveInterval(): {}", timeOut);
//
//      String js =
//          "<script type='text/javascript' src='VAADIN/javascript/libs/jquery-1.7.2.min.js'></script>\n"
//        + "<script type='text/javascript'>$.noConflict();</script>\n"
//        + "<script type='text/javascript' src='VAADIN/javascript/infovalmer.js'></script>\n"
//        + "<script type='text/javascript'>\n"
//        + " jQuery(function() {\n"
//        + "   var timer = new infovalmer.util.SessionTimer({timeOut: " + timeOut + ", destinationUrl: '" + destinationUrl + "'});\n"
//        + "   timer.start();\n"
//        + " });\n"
//        + "</script>\n";
//
//      page.write(js);
//    }
//
//    /**
//     * @see com.vaadin.terminal.gwt.server.AbstractApplicationServlet#init(javax.servlet.ServletConfig)
//     */
//    @SuppressWarnings("unchecked")
//    @Override
//    public void init(ServletConfig servletConfig) throws ServletException {
//        super.init(servletConfig);        
//        applicationBean = servletConfig.getInitParameter("applicationBean");
//        if (applicationBean == null) {
//            throw new ServletException("ApplicationBean not specified in servlet parameters");
//        }
//        applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletConfig.getServletContext());
//        applicationClass = (Class<? extends Application>) applicationContext.getType(applicationBean);
//        initLocaleResolver(applicationContext);
//    }
//
//    /**
//     * Inits the locale resolver.
//     *
//     * @param context the context
//     */
//    private void initLocaleResolver(ApplicationContext context) {
//        try {
//            this.localeResolver = (LocaleResolver) context.getBean(DispatcherServlet.LOCALE_RESOLVER_BEAN_NAME, LocaleResolver.class);
//        } catch (NoSuchBeanDefinitionException ex) {
//            this.localeResolver = new SessionLocaleResolver();
//        }
//    }
//
//    /**
//     * @see com.vaadin.terminal.gwt.server.AbstractApplicationServlet#service(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
//     */
//    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException,
//        IOException {
//        final Locale locale = localeResolver.resolveLocale(request);
//        LocaleContextHolder.setLocale(locale);
//        ServletRequestAttributes requestAttributes = new ServletRequestAttributes(request);
//        RequestContextHolder.setRequestAttributes(requestAttributes);
//        try {
//            super.service(new HttpServletRequestWrapper(request) {
//                @Override
//                public Locale getLocale() {
//                    return locale;
//                }
//            }, response);
//        } finally {
//            if (!locale.equals(LocaleContextHolder.getLocale())) {
//                localeResolver.setLocale(request, response, LocaleContextHolder.getLocale());
//            }
//            LocaleContextHolder.resetLocaleContext();
//            RequestContextHolder.resetRequestAttributes();
//        }
//    }
//
//    /**
//     * @see com.vaadin.terminal.gwt.server.AbstractApplicationServlet#getNewApplication(javax.servlet.http.HttpServletRequest)
//     */
//    @Override
//    protected Application getNewApplication(HttpServletRequest request) throws ServletException {
//        return (Application) applicationContext.getBean(applicationBean);
//    }
//
//    /**
//     * @see com.vaadin.terminal.gwt.server.AbstractApplicationServlet#getApplicationClass()
//     */
//    @Override
//    protected Class<? extends Application> getApplicationClass() throws ClassNotFoundException {
//        return applicationClass;
//    }
//
//}
