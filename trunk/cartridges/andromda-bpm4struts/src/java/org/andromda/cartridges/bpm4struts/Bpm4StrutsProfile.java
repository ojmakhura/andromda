package org.andromda.cartridges.bpm4struts;

public class Bpm4StrutsProfile
{
    /* ----------------- Stereotypes -------------------- */

    public static final String STEREOTYPE_VIEW = "FrontEndView";
    public static final String STEREOTYPE_EVENT = "FrontEndEvent";
    public static final String STEREOTYPE_USER = "FrontEndUser";
    public static final String STEREOTYPE_USECASE = "FrontEndUseCase";
    public static final String STEREOTYPE_APPLICATION = "FrontEndApplication";
    public static final String STEREOTYPE_CONTROLLER = "FrontEndController";
    public static final String STEREOTYPE_EXCEPTION = "FrontEndException";

    /* ----------------- Tagged Values -------------------- */

    public static final String TAGGED_VALUE_ACTION_TYPE = "@struts.action.type";
    public static final String TAGGED_VALUE_ACTION_RESETTABLE = "@struts.action.resettable";
    public static final String TAGGED_VALUE_ACTION_SUCCES_MESSAGE = "@struts.action.success.message";
    public static final String TAGGED_VALUE_INPUT_REQUIRED = "@struts.view.field.required";
    public static final String TAGGED_VALUE_INPUT_READONLY = "@struts.view.field.readonly";
    public static final String TAGGED_VALUE_INPUT_FORMAT = "@struts.view.field.format";
    public static final String TAGGED_VALUE_INPUT_TYPE = "@struts.view.field.type";
    public static final String TAGGED_VALUE_INPUT_VALIDWHEN = "@struts.view.field.validwhen";
    public static final String TAGGED_VALUE_TABLE_COLUMNS = "@struts.view.table.columns";
    public static final String TAGGED_VALUE_TABLE_MAXROWS = "@struts.view.table.maxrows";
    public static final String TAGGED_VALUE_TABLE_EXPORTABLE = "@struts.view.table.exportable";
    public static final String TAGGED_VALUE_TABLE_SORTABLE = "@struts.view.table.sortable";
    public static final String TAGGED_VALUE_EXCEPTION_TYPE = "@struts.exception.type";
    public static final String TAGGED_VALUE_CONTROLLER_USE_CASE = "@struts.controller.usecase";
    public static final String TAGGED_VALUE_USECASE_ACTIVITY = "@struts.usecase.activity";

    /* ----------------- Default Values ------------------- */
    public static final String TAGGED_VALUE_INPUT_DEFAULT_REQUIRED = "true";
    public static final String TAGGED_VALUE_EXCEPTION_DEFAULT_TYPE = "java.lang.Exception";

    public static final String TAGGED_VALUE_ACTION_TYPE_HYPERLINK = "hyperlink";
    public static final String TAGGED_VALUE_ACTION_TYPE_FORM = "form";
    public static final String TAGGED_VALUE_ACTION_DEFAULT_TYPE = TAGGED_VALUE_ACTION_TYPE_FORM;

    public static final String TAGGED_VALUE_INPUT_TYPE_TEXT = "text";
    public static final String TAGGED_VALUE_INPUT_TYPE_TEXTAREA = "textarea";
    public static final String TAGGED_VALUE_INPUT_TYPE_RADIO = "radio";
    public static final String TAGGED_VALUE_INPUT_TYPE_CHECKBOX = "checkbox";
    public static final String TAGGED_VALUE_INPUT_TYPE_HIDDEN = "hidden";
    public static final String TAGGED_VALUE_INPUT_TYPE_SELECT = "select";
    public static final String TAGGED_VALUE_INPUT_TYPE_PASSWORD = "password";
    public static final int TAGGED_VALUE_INPUT_TYPE_OPTION_DEFAULT_COUNT = 3;

    public static final int TAGGED_VALUE_TABLE_MAXROWS_DEFAULT_COUNT = 15;
    public static final boolean TAGGED_VALUE_TABLE_SORTING_DEFAULT_VALUE = true;

    /* --------------- Runtime properties ---------------- */
    public static final boolean ENABLE_CACHE = true;

    private Bpm4StrutsProfile()
    {
    }
}
