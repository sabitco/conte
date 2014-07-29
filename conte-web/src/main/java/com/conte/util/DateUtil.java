/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.conte.util;

import com.conte.exception.SistemaException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Wrapper class for date-string formatting facilities.
 *
 * @author Roger Padilla C.
 * @author Carlos Uribe [curibe@quasarbi.com]
 */
public class DateUtil {

  public static final String YYYY_MM_DD = "yyyy-MM-dd";
  public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
  public static final String HUMAN_FULL = "EEEE d.MMMMM.yyyy hh:mm:ss a";
  private Map<String, DateFormat> dateFormatMap = new HashMap<String, DateFormat>();
  private Logger logger = LoggerFactory.getLogger(getClass());

  // Private constructor prevents instantiation from other classes
  private DateUtil() {
    dateFormatMap.put(YYYY_MM_DD, new SimpleDateFormat(YYYY_MM_DD));
    dateFormatMap.put(YYYY_MM_DD_HH_MM_SS, new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS));
    dateFormatMap.put(HUMAN_FULL, new SimpleDateFormat(HUMAN_FULL));
  }

  public String currentDateAsString() {
    return formatDateAsString(new Date());
  }

  /**
   * Format the current time as {@link String}.
   *
   * @return a date-time string formatted as yyyy-MM-dd HH:mm:ss.
   * @see formatDateTimeAsString(Date)
   */
  public String currentDateAsDateTimeString() {
    return formatDateTimeAsString(Calendar.getInstance().getTime());
  }

  /**
   * Format the specified time as {@link String}.
   *
   * @param fecha a {@link Date} object.
   * @return a date-time string formatted as yyyy-MM-dd HH:mm:ss.
   */
  public String formatDateTimeAsString(Date fecha) {
    return dateFormatMap.get(YYYY_MM_DD_HH_MM_SS).format(fecha);
  }

  /**
   * Format the specified calendar object as {@link String}.
   *
   * @param fecha a {@link Calendar} object.
   * @return a string formatted as EEEE d.MMMMM.yyyy hh:mm:ss a.
   * @see formatAsHumanFull(Date)
   */
  public String formatAsHumanFull(Calendar fecha) {
    return formatAsHumanFull(fecha.getTime());
  }

  /**
   * Format the specified date object as {@link String}.
   *
   * @param fecha a {@link Date} object.
   * @return a string formatted as EEEE d.MMMMM.yyyy hh:mm:ss a.
   */
  public String formatAsHumanFull(Date fecha) {
    return dateFormatMap.get(HUMAN_FULL).format(fecha);
  }

  /**
   * Format the specified date object as {@link String}.
   *
   * @param date a {@link Date} object.
   * @return a string formatted as yyyy-MM-dd.
   * @see parseDate(Date, String)
   */
  public String formatDateAsString(Date date) {
    return parseDate(date, YYYY_MM_DD);
  }

  /**
   * Format a calendar object into a specified pattern or format.
   *
   * @param calendar a {@link Calendar} object.
   * @param pattern date format used for string conversion.
   * @return a pattern-based formatted {@link String}.
   * @see parseDate(Date, String)
   */
  public String parseDate(Calendar calendar, String pattern) {
    return parseDate(calendar.getTime(), pattern);
  }

  /**
   * Format a date object into a specified pattern or format.
   *
   * @param date a {@link Date} object.
   * @param pattern date format used for string conversion.
   * @return a pattern-based formatted {@link String}.
   */
  public String parseDate(Date date, String pattern) {
    DateFormat dateFormat;
    if ((dateFormat = dateFormatMap.get(pattern)) == null) {
      dateFormat = new SimpleDateFormat(pattern);
    }
    return dateFormat.format(date);
  }

  /**
   * Parse a date string building a {@link Date} object.
   *
   * @param date a {@link String} object containing a date.
   * @return a new {@link Date} parsed object.
   */
  public Date parseDate(String date) {
    try {
      return dateFormatMap.get(YYYY_MM_DD).parse(date);
    } catch (ParseException ex) {
      logger.error(ex.getMessage());
      throw new SistemaException(ex.getMessage(), ex);
    }
  }
  
  public Date parseDateTime(String date) {
    try {
      return dateFormatMap.get(YYYY_MM_DD_HH_MM_SS).parse(date);
    } catch (ParseException ex) {
      logger.error(ex.getMessage());
      throw new SistemaException(ex.getMessage(), ex);
    }
  }

  /**
   * Parse a date string building a {@link Date} object.
   *
   * @param date a {@link String} object containing a date.
   * @return a new {@link Date} parsed object.
   */
  public java.sql.Date parseAsDateSql(String date) {
    if (date == null || "0000-00-00".equals(date) || date.equals("")) {
      return null;
    }
    return java.sql.Date.valueOf(date);
  }

  public String parseAsTimeStamp(String timestamp) {
    if (timestamp != null) {
      if (timestamp.trim().isEmpty()) {
        return "1970-01-01:00:00:00.000000";
      }
      if (timestamp.startsWith("0001")) {
        timestamp = "1970" + timestamp.substring(4, timestamp.length());
      }
    }
    return timestamp;
  }

  /**
   * SingletonHolder is loaded on the first execution of Singleton.getInstance() or the first access to SingletonHolder.INSTANCE, not before.
   */
  private static class SingletonHolder {

    public static final DateUtil INSTANCE = new DateUtil();
  }

  /**
   * Get the current instance of DateUtil facility.
   *
   * @return a {@link DateUtil} singleton object.
   */
  public static DateUtil getInstance() {
    return SingletonHolder.INSTANCE;
  }
}