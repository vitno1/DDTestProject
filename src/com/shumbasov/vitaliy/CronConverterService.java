package com.shumbasov.vitaliy;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/*3.	Метод getImplementationInfo() - возвращает информацию о реализации интерфейса (ФИО, имя класса реализации, пакет, ссылка на github).
 * 2.	Метод convert() на вход принимает список дат (порядок не важен) формата (“yyyy-MM-dd'T’HH:mm:ss”), на выход строка вида “0 * * * * MON”.
 * 6.	Найти cron удовлетворяющий не меньше половине дат (> 50%), в противном случае выбросить исключение DatesToCronConvertException.
 *  Так же кидать исключение при валидации.. Год в дате не важен.  */
public class CronConverterService implements DatesToCronConverter {
    String cronBySeconds = null;
    String cronByMinutes = null;
    String cronByHours = null;
    int[][] hours = null;
    int[][] minutes = null;
    int[][] seconds = null;
    int[] maxValueAndCoincidencesByHours;
    int[] maxValueAndCoincidencesByMinutes;
    int[] maxValueAndCoincidencesBySeconds;
    int sumOfCoincendeces;



    private int[] convertToMonth(List<LocalDateTime> date) {
        int[] months = new int[date.size()];
        for (int i = 0; i < months.length; i++) {
            months[i] = date.get(i).getMonth().getValue();
        }
        return months;
    }

    private int[] convertToDayOfMonth(List<LocalDateTime> date) {
        int[] dayOfMonth = new int[date.size()];
        for (int i = 0; i < dayOfMonth.length; i++) {
            dayOfMonth[i] = date.get(i).getDayOfMonth();
        }
        return dayOfMonth;
    }

    private int[] convertToHour(List<LocalDateTime> date) {
        int[] hour = new int[date.size()];
        for (int i = 0; i < hour.length; i++) {
            hour[i] = date.get(i).getHour();
        }
        return hour;
    }

    private int[] convertToMinute(List<LocalDateTime> date) {
        int[] minute = new int[date.size()];
        for (int i = 0; i < minute.length; i++) {
            minute[i] = date.get(i).getMinute();
        }
        return minute;
    }

    private int[] convertToSecond(List<LocalDateTime> date) {
        int[] second = new int[date.size()];
        for (int i = 0; i < second.length; i++) {
            second[i] = date.get(i).getSecond();
        }
        return second;
    }

    private int[][] convertToValueAndIndex(int[] array) {
        int[][] result = new int[array.length][2];
        for (int i = 0; i < result.length; i++) {
            result[i][0] = array[i];
            result[i][1] = i;
        }
        return result;

    }

    private int[] maxValueAndCoincidences(int[][] array) {
        int[] result = new int[2];
        int numberOfСoincidences = 0;
        int maxValueOfСoincidences = 0;
        int maxValue = 0;
        for (int i = 0; i < array.length; i++) {
            if (numberOfСoincidences > maxValueOfСoincidences) {
                maxValueOfСoincidences = numberOfСoincidences;
                maxValue = array[i - 1][0];
//                System.out.println("maxValueOfСoincidences " + maxValueOfСoincidences);
//                System.out.println("maxValue " + maxValue);
            }
            numberOfСoincidences = 0;
            for (int j = 0; j < array.length; j++) {
                if (array[i][0] == array[j][0]) {
                    numberOfСoincidences++;
                }
            }
        }
        result[0] = maxValue;
        result[1] = maxValueOfСoincidences;
        return result;
    }

    private int CoincidencesByColumn(int[][] doubleArray, int[] array, int[][] doubleArray1, int[] array1, int[][] doubleArray2, int[] array2) {
        int maxCoincidencesBySecondColumn = 0;
        for (int i = 0; i < doubleArray.length; i++) {
            if (doubleArray[i][0] != array[0]) {
                continue;
            } else if (doubleArray[i][0] == array[0] && doubleArray1[i][0] == array1[0] && doubleArray2[i][0] == array2[0]) {
                if (doubleArray[i][1] == doubleArray1[i][1] && doubleArray[i][1] == doubleArray2[i][1]) {
                    maxCoincidencesBySecondColumn++;
                }
            }
        }
        return maxCoincidencesBySecondColumn;
    }

    private void addSecondToCron() {
        for (int i = 0; i < seconds.length; i++) {
            if (!cronBySeconds.contains(String.valueOf(seconds[i][0]))) {
                if (minutes[i][0] == maxValueAndCoincidencesByMinutes[0] && hours[i][0] == maxValueAndCoincidencesByHours[0]) {
                    cronBySeconds = cronBySeconds + "," + String.valueOf(seconds[i][0]);
                    sumOfCoincendeces++;
                }
            }
        }
    }

    private void addMinuteToCron() {
        for (int i = 0; i < minutes.length; i++) {
            if (!cronByMinutes.contains(String.valueOf(minutes[i][0]))) {
                if (seconds[i][0] == maxValueAndCoincidencesBySeconds[0] && hours[i][0] == maxValueAndCoincidencesByHours[0]) {
                    cronByMinutes = cronByMinutes + "," + String.valueOf(minutes[i][0]);
                    sumOfCoincendeces++;
                }
            }
        }
    }

    private void addHourToCron() {
        for (int i = 0; i < hours.length; i++) {
            if (!cronByHours.contains(String.valueOf(hours[i][0]))) {
                if (seconds[i][0] == maxValueAndCoincidencesBySeconds[0] && minutes[i][0] == maxValueAndCoincidencesByMinutes[0]) {
                    cronByHours = cronByHours + "," + String.valueOf(hours[i][0]);
                    sumOfCoincendeces++;
                }
            }
        }
    }

    private void addToCron() {
        for (int i = 0; i < hours.length; i++) {
            if (!cronBySeconds.contains(String.valueOf(seconds[i][0])) && !cronByMinutes.contains(String.valueOf(minutes[i][0]))
                    && cronByHours.contains(String.valueOf(hours[i][0]))) {
                cronBySeconds = cronBySeconds + "," + String.valueOf(seconds[i][0]);
                cronByMinutes = cronByMinutes + "," + String.valueOf(minutes[i][0]);
                for (int j = 0; j < hours.length; j++) {
                    if (seconds[i][0] == seconds[j][0] && minutes[i][0] == minutes[j][0] && hours[i][0] == hours[j][0]) {
                        sumOfCoincendeces++;
                    }
                    if (sumOfCoincendeces > (double) (hours.length / 2)) {
                        return;
                    }
                }
            }
        }
        for (int i = 0; i < minutes.length; i++) {
            if (!cronBySeconds.contains(String.valueOf(seconds[i][0])) && cronByMinutes.contains(String.valueOf(minutes[i][0]))
                    && !cronByHours.contains(String.valueOf(hours[i][0]))) {
                cronBySeconds = cronBySeconds + "," + String.valueOf(seconds[i][0]);
                cronByHours = cronByHours + "," + String.valueOf(hours[i][0]);
                for (int j = 0; j < minutes.length; j++) {
                    if (seconds[i][0] == seconds[j][0] && minutes[i][0] == minutes[j][0] && hours[i][0] == hours[j][0]) {
                        sumOfCoincendeces++;
                    }
                    if (sumOfCoincendeces > (double) (seconds.length / 2)) {
                        return;
                    }
                }
            }
        }
        for (int i = 0; i < seconds.length; i++) {
            if (cronBySeconds.contains(String.valueOf(seconds[i][0])) && !cronByMinutes.contains(String.valueOf(minutes[i][0]))
                    && !cronByHours.contains(String.valueOf(hours[i][0]))) {
                cronByMinutes = cronByMinutes + "," + String.valueOf(minutes[i][0]);
                cronByHours = cronByHours + "," + String.valueOf(hours[i][0]);
                for (int j = 0; j < seconds.length; j++) {
                    if (seconds[i][0] == seconds[j][0] && minutes[i][0] == minutes[j][0] && hours[i][0] == hours[j][0]) {
                        sumOfCoincendeces++;
                    }
                    if (sumOfCoincendeces > (double) (seconds.length / 2)) {
                        return;
                    }
                }
            }
        }
        for (int i = 0; i < seconds.length; i++) {
            if (!cronBySeconds.contains(String.valueOf(seconds[i][0])) && !cronByMinutes.contains(String.valueOf(minutes[i][0]))
                    && !cronByHours.contains(String.valueOf(hours[i][0]))) {
                cronBySeconds = cronBySeconds + "," + String.valueOf(seconds[i][0]);
                cronByMinutes = cronByMinutes + "," + String.valueOf(minutes[i][0]);
                cronByHours = cronByHours + "," + String.valueOf(hours[i][0]);
                for (int j = 0; j < seconds.length; j++) {
                    if (seconds[i][0] == seconds[j][0] && minutes[i][0] == minutes[j][0] && hours[i][0] == hours[j][0]) {
                        sumOfCoincendeces++;
                    }
                    if (sumOfCoincendeces > (double) (seconds.length / 2)) {
                        return;
                    }
                }
            }

        }
    }


    public CronConverterService() {

    }

    @Override
    public String convert(List<LocalDateTime> date) throws DatesToCronConvertException {
        int[][] months = convertToValueAndIndex(convertToMonth(date));
        int[][] dayOfMonths = convertToValueAndIndex(convertToDayOfMonth(date));
        hours = convertToValueAndIndex(convertToHour(date));
        minutes = convertToValueAndIndex(convertToMinute(date));
        seconds = convertToValueAndIndex(convertToSecond(date));
        int[] maxValueAndCoincidencesByMonths = maxValueAndCoincidences(months);
        int[] maxValueAndCoincidencesByDayOfMonths = maxValueAndCoincidences(dayOfMonths);
        maxValueAndCoincidencesByHours = maxValueAndCoincidences(hours);
        maxValueAndCoincidencesByMinutes = maxValueAndCoincidences(minutes);
        maxValueAndCoincidencesBySeconds = maxValueAndCoincidences(seconds);
        cronBySeconds = String.valueOf(maxValueAndCoincidencesBySeconds[0]);
        cronByMinutes = String.valueOf(maxValueAndCoincidencesByMinutes[0]);
        cronByHours = String.valueOf(maxValueAndCoincidencesByHours[0]);

        sumOfCoincendeces = CoincidencesByColumn(seconds, maxValueAndCoincidencesBySeconds, minutes, maxValueAndCoincidencesByMinutes,
                hours, maxValueAndCoincidencesByHours);
        addSecondToCron();
        if (sumOfCoincendeces > (double) (date.size() / 2)) {
            return cronBySeconds + " " + cronByMinutes + " " + cronByHours + " * * *";
        }
        addMinuteToCron();
        if (sumOfCoincendeces > (double) (date.size() / 2)) {
            return cronBySeconds + " " + cronByMinutes + " " + cronByHours + " * * *";
        }
        addHourToCron();
        if (sumOfCoincendeces > (double) (date.size() / 2)) {
            return cronBySeconds + " " + cronByMinutes + " " + cronByHours + " * * *";
        }
        addToCron();
        if((sumOfCoincendeces < (double) (date.size() / 2))){
            throw new DatesToCronConvertException();
        }
        return cronBySeconds + " " + cronByMinutes + " " + cronByHours + " * * *";

    }


    @Override
    public String getImplementationInfo() {
       // 3.	Метод getImplementationInfo() - возвращает информацию о реализации интерфейса (ФИО, имя класса реализации, пакет, ссылка на github).
       return "Шумбасов Виталий Сергеевич " +
               "CronConverterService.java " +
               "~\\DDTestProject\\src\\com\\shumbasov\\vitaliy" +
               "https://github.com/vitno1/DDTestProject.git";

    }

    class DatesToCronConvertException extends Throwable {
    }
}

