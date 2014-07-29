///*
// * Licensed under the Apache License, Version 2.0 (the "License"); you may not
// * use this file except in compliance with the License. You may obtain a copy of
// * the License at
// *
// * http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
// * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
// * License for the specific language governing permissions and limitations under
// * the License.
// */
//package com.conte.common.ui.util;
//
//import java.util.Locale;
//
//import javax.servlet.ServletContext;
//import javax.servlet.http.HttpServletRequest;
//
//import org.springframework.context.ApplicationContext;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//import org.springframework.web.context.support.WebApplicationContextUtils;
//
//import com.vaadin.Application;
//import com.vaadin.terminal.gwt.server.WebApplicationContext;
//import com.vaadin.terminal.gwt.server.WebBrowser;
//
///**
// * Clases de integracion Vaadin -Spring The Class ApplicationHelper.
// */
//public class ApplicationHelper {
//
//  /**
//   * Gets the application context.
//   *
//   * @return the application context
//   */
//  public static ApplicationContext getApplicationContext() {
//    return getApplicationContext(getCurrentApplication());
//  }
//
//  /*
//   * For Terracotta. ThreadLocal is JVM specific so we can't use method getCurrentApplication(). See tc-config.xml <on-load> elements.
//   */
//  /**
//   * Gets the application context.
//   *
//   * @param app the app
//   *
//   * @return the application context
//   */
//  public static ApplicationContext getApplicationContext(Application app) {
//    ServletContext sc = ((WebApplicationContext) app.getContext()).getHttpSession().getServletContext();
//    return WebApplicationContextUtils.getWebApplicationContext(sc);
//  }
//
//  public static ServletContext getServletContext() {
//    return ((WebApplicationContext) getCurrentApplication().getContext()).getHttpSession().getServletContext();
//  }
//
//  /**
//   * Gets the browser.
//   *
//   * @return the browser
//   */
//  public static WebBrowser getBrowser() {
//    Application app = getCurrentApplication();
//    return ((WebApplicationContext) app.getContext()).getBrowser();
//  }
//
//  /**
//   * Gets the request.
//   *
//   * @return the request
//   */
//  public static HttpServletRequest getRequest() {
//    ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//    if (attrs != null) {
//      return attrs.getRequest();
//    }
//    return null;
//  }
//
//  /**
//   * Gets the current application.
//   *
//   * @return the current application
//   */
//  public static Application getCurrentApplication() {
//    return ApplicationHolder.getApplication();
//  }
//
//  /**
//   * Gets the current locale.
//   *
//   * @return the current locale
//   */
//  public static Locale getCurrentLocale() {
//    return getCurrentApplication().getLocale();
//  }
//
//  /**
//   * Gets the message.
//   *
//   * @param key the key
//   *
//   * @return the message
//   */
//  public static String getMessage(String key) {
//    return getVaadinApplicationObjectSupport().getMessage(key);
//  }
//
//  /**
//   * Gets the message.
//   *
//   * @param key the key
//   * @param args the args
//   *
//   * @return the message
//   */
//  public static String getMessage(String key, String... args) {
//    return getVaadinApplicationObjectSupport().getMessage(key, args);
//  }
//
//  /**
//   * Gets the vaadin application object support.
//   *
//   * @return the vaadin application object support
//   */
//  public static VaadinApplicationObjectSupport getVaadinApplicationObjectSupport() {
//    String names[] = getApplicationContext().getBeanNamesForType(VaadinApplicationObjectSupport.class);
//    if (names != null && names.length == 1) {
//      return (VaadinApplicationObjectSupport) getApplicationContext().getBean(names[0]);
//    }
//    return null;
//  }
//}
