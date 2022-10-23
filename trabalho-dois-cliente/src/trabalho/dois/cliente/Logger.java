/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package trabalho.dois.cliente;

import java.time.*;
import java.time.format.*;

/**
 *
 * @author otavio
 */
public class Logger {
    public static String getLog(String level, String format, Object... args) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return String.format("%s [%s] | ", dtf.format(now), level.toUpperCase()) + String.format(format, args);
    }

    public static void info(String format, Object... args) {
        String message = "\u001B[32m" + getLog("info", format, args) + "\u001B[0m";
        System.out.println(message);
    }

    public static void debug(String format, Object... args) {
        String message = "\u001B[34m" + getLog("debug", format, args) + "\u001B[0m";
        System.out.println(message);
    }

    public static void error(String format, Object... args) {
        String message = "\u001B[31m" + getLog("error", format, args) + "\u001B[0m";
        System.out.println(message);
    }

    public static void input(String format, Object... args) {
        String message = "\n\u001B[36m" + getLog("input", format, args) + "\u001B[0m";
        System.out.println(message);
    }
}
