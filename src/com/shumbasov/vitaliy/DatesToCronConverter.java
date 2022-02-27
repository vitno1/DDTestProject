package com.shumbasov.vitaliy;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
/*Interface DatesToCronConverter - 2 methods convert , getImplementationInfo
* Class CronConverterService
* конструктор по умолчанию , реализация интерфейса DatesToCronConverter с 2-мя методами
* getImplementationInfo - нужно вернуть либо объект в котором будут поля с описанием ФИО, гитахаба и тд, либо вернуть строку, которая содержит всё описание   */

public interface DatesToCronConverter {
    String convert(List<LocalDateTime> date) throws CronConverterService.DatesToCronConvertException;
    String getImplementationInfo();
}
