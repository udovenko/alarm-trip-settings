package ru.sakhalinenergy.alarmtripsettings.models.logic.source;


/**
 * Events enumeration for current package classes.
 *
 * @author Denis Udovenko
 * @vrsion 1.0.3
 */
public enum SourceEvent 
{
    THREAD_PROGRESS,
    THREAD_WARNING,
    THREAD_ERROR,
    BOOK_CONNECTED,
    TAGS_READ,
    STATIONS_READ,
    FILE_PATH_UPDATED,
    PLANT_CODE_SET,
    TAG_SET_UPDATED,
    ADD_TAG_ERROR,
    SOURCE_REMOVED,
    SOURCE_SAVED,
    SOURCE_INITIALIZED
}// SourceEvent
