package com.corkili.learningserver.scorm.rte.model.error;

public enum ScormError {
    E_0(0, "No ScormError"),
    E_101(101, "General Exception"),
    E_102(102, "General Initialization Failure"),
    E_103(103, "Already Initialized"),
    E_104(105, "Content Instance Terminated"),
    E_111(111, "General Termination Failure"),
    E_112(112, "Termination Before Initialization"),
    E_113(113, "Termination After Termination"),
    E_122(122, "Retrieve Data Before Initialization"),
    E_123(123, "Retrieve Data After Termination"),
    E_132(132, "Store Data Before Initialization"),
    E_133(133, "Store Data After Termination"),
    E_142(142, "Commit Before Initialization"),
    E_143(143, "Commit After Termination"),
    E_201(201, "General Argument ScormError"),
    E_301(301, "General Get Failure"),
    E_351(351, "General Set Failure"),
    E_391(391, "General Commit Failure"),
    E_401(401, "Undefined Data Model Element"),
    E_402(402, "Unimplemented Data Model Element"),
    E_403(403, "Data Model Element Value Not Initialized"),
    E_404(404, "DataModel Element Is Read Only"),
    E_405(405, "Data Model Element Is Write Only"),
    E_406(406, "Data Model Element Type Mismatch"),
    E_407(407, "Data Model Element Value Out Of Range"),
    E_408(408, "Data Model Dependency Not Established");

    private int code;

    private String msg;

    ScormError(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public static ScormError getByCode(int c) {
        return ScormError.valueOf("E_" + c);
    }

}
