package com.example.scheduleapp.structure;

public class Constant {
    public static final int SUCCESS = 200;
    public static final int ERR = -1;
    public static final int ERR_NULL = 1;

    //join
    public static final int DUP_ID = 2;
    public static final int DUP_USER = 3;
    public static final int ERR_CHK_ID = 4;

    //login
    public static final int ERR_LOG_ID = 1;
    public static final int ERR_LOG_PW = 2;

    //find
    public static final int NO_DATA = 2;

    //schedule
    public static final String DOT_COLOR = "#72AF01";
    public static final String SELECT_DAY_COLOR = "#505050";
    public static final int CODE_MODIFY = 0;

    public static final String FLAG_ADD = "addSchedule";
    public static final String FLAG_MODIFY = "modifySchedule";
    public static final String FLAG_ADD_GROUP = "addGroupSch";

    public static final int ADD_SUCCESS = 0;
    public static final int MOD_SUCCESS = 1;
    public static final int DELETE_SUCCESS = 2;

    public static final int FROM_USER_SCHEDULE = 0;
    public static final int FROM_GROUP_SCHEDULE = 1;

    //social
    public static final int TO_GROUP = 0;
    public static final int TO_FRIEND = 1;

    public static final int FOR_USER = 0;
    public static final int FOR_GROUP = 1;

    public static final int ADD_GROUP = 0;

    public static final int CODE_ISADDED = 0;
    public static final int CODE_NOTADDED = 1;

    //show_schedule
    public static final int ID_MODIFY = 0;
    public static final int ID_DELETE = 1;

    public static final int CODE_ADD = 0;
    public static final int CODE_ISCHANGED = 1;
    public static final int CODE_NOTCHANGED = 2;
}
