package ru.sakhalinenergy.alarmtripsettings.views.DataSourceDialog.AutomaticSourceCreationDialog;


/**
 * Events enumeration for current package and sub-packages views.
 *
 * @author Denis Udovenko
 * @version 1.0.1
 */
public enum ViewEvent 
{
    CHANGE_PLANT_SELECTION,
    CHANGE_TAG_FORMAT_SELECTION,
    SELECT_PATH_TO_SPREADSHEET_TEXT_FIELD_BUTTON_CLICK,
    RUN_SPREADSHEET_PARSING_BUTTON_CLICK,     
    SELECT_SCADA_DATABASE_PATH_BUTTON_CLICK,
    RUN_SCADA_DATABASE_PARSING_BUTTON_CLICK,
    SET_PATH_TO_BACKUP_FOLDER_BUTTON_CLICK,
    SELECT_DCS_EXPORT_FILE_PATH_BUTTON_CLICK,
    RUN_HONEYWELL_DCS_EXPORT_PARSING_BUTTON_CLICK,
    RUN_BACKUP_PARSING_BUTTON_CLICK,
    DIALOG_CLOSING
}// ViewEvent
