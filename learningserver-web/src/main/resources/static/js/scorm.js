
function API_1484_11() {

}

/**
 * @return {string}
 */
API_1484_11.prototype.Initialize = function(parameter) {
    var params = {
        methodName: 'initialize',
        parameter1: '' + parameter
    };
    var res = 'false';
    var errCode = 101;
    $.ajax({
        async: false,
        type: "post",
        url: "lmsRuntimeAPI",
        data: JSON.stringify(params),
        dataType: "json",
        contentType: 'application/json',
        success: function (data) {
            if (data.result == 'true') {
                res = data.returnValue;
                errCode = data.lastError;
            } else {
                res = 'false';
                errCode = 102;
            }
        },
        error: function (data) {
            res = 'false';
            errCode = 102;
        }
    });
    this.LastError = errCode;
    return res;
};

/**
 * @return {string}
 */
API_1484_11.prototype.Terminate = function(parameter) {
    var params = {
        methodName: 'terminate',
        parameter1: '' + parameter
    };
    var res = 'false';
    var errCode = 101;
    $.ajax({
        async: false,
        type: "post",
        url: "lmsRuntimeAPI",
        data: JSON.stringify(params),
        dataType: "json",
        contentType: 'application/json',
        success: function (data) {
            if (data.result == 'true') {
                res = data.returnValue;
                errCode = data.lastError;
            } else {
                res = 'false';
                errCode = 111;
            }
        },
        error: function (data) {
            res = 'false';
            errCode = 111;
        }
    });
    this.LastError = errCode;
    return res;
};

/**
 * @return {string}
 */
API_1484_11.prototype.GetValue = function(element) {
    var params = {
        methodName: 'getValue',
        parameter1: '' + element
    };
    var res = '';
    var errCode = 0;
    $.ajax({
        async: false,
        type: "post",
        url: "lmsRuntimeAPI",
        data: JSON.stringify(params),
        dataType: "json",
        contentType: 'application/json',
        success: function (data) {
            if (data.result == 'true') {
                res = data.returnValue;
                errCode = data.lastError;
            } else {
                res = '';
                errCode = 301;
            }
        },
        error: function (data) {
            res = '';
            errCode = 301;
        }
    });
    this.LastError = errCode;
    return res;
};

/**
 * @return {string}
 */
API_1484_11.prototype.SetValue = function(element, value) {
    var params = {
        methodName: 'setValue',
        parameter1: '' + element,
        parameter2: '' + value
    };
    var res = 'false';
    var errCode = 101;
    $.ajax({
        async: false,
        type: "post",
        url: "lmsRuntimeAPI",
        data: JSON.stringify(params),
        dataType: "json",
        contentType: 'application/json',
        success: function (data) {
            if (data.result == 'true') {
                res = data.returnValue;
                errCode = data.lastError;
            } else {
                res = 'false';
                errCode = 351;
            }
        },
        error: function (data) {
            res = 'false';
            errCode = 351;
        }
    });
    this.LastError = errCode;
    return res;
};

/**
 * @return {string}
 */
API_1484_11.prototype.Commit = function(parameter) {
    var params = {
        methodName: 'commit',
        parameter1: '' + parameter
    };
    var res = 'true';
    var errCode = 0;
    $.ajax({
        async: false,
        type: "post",
        url: "lmsRuntimeAPI",
        data: JSON.stringify(params),
        dataType: "json",
        contentType: 'application/json',
        success: function (data) {
            if (data.result == 'true') {
                res = data.returnValue;
                errCode = data.lastError;
            } else {
                res = 'false';
                errCode = 391;
            }
        },
        error: function (data) {
            res = 'false';
            errCode = 391;
        }
    });
    this.LastError = errCode;
    return res;
};

/**
 * @return {number}
 */
API_1484_11.prototype.GetLastError = function() {
    if (this.LastError === undefined) {
        var params = {
            methodName: 'getLastError'
        };
        var res = 0;
        $.ajax({
            async: false,
            type: "post",
            url: "lmsRuntimeAPI",
            data: JSON.stringify(params),
            dataType: "json",
            contentType: 'application/json',
            success: function (data) {
                if (data.result == 'true') {
                    res = data.returnValue;
                } else {
                    res = 101;
                }
            },
            error: function (data) {
                res = 101;
            }
        });
        this.LastError = res;
    }
    return this.LastError;
};

API_1484_11.prototype.GetErrorString = function(errorCode) {
    var msg = this.ScormErrorMsg[errorCode];
    if (msg === undefined || msg === "") {
        msg = "Undefined error code - " + errorCode;
    }
    return msg;
};

API_1484_11.prototype.GetDiagnostic = function(errorCode) {
    var msg = this.ScormErrorMsg[errorCode];
    if (msg === undefined || msg === "") {
        msg = "Undefined error code - " + errorCode;
    }
    return msg;
};

API_1484_11.prototype.LastError = undefined;

API_1484_11.prototype.ScormErrorMsg = {
    0: 'No Error',
    101: 'General Exception',
    102: 'General Initialization Failure',
    103: 'Already Initialized',
    104: 'Content Instance Terminated',
    111: 'General Termination Failure',
    112: 'Termination Before Initialization',
    113: 'Termination After Termination',
    122: 'Retrieve Data Before Initialization',
    123: 'Retrieve Data After Termination',
    132: 'Store Data Before Initialization',
    133: 'Store Data After Termination',
    142: 'Commit Before Initialization',
    143: 'Commit After Termination',
    201: 'General Argument Error',
    301: 'General Get Failure',
    351: 'General Set Failure',
    391: 'General Commit Failure',
    401: 'Undefined Data Model Element',
    402: 'Unimplemented Data Model Element',
    403: 'Data Model Element Value Not Initialized',
    404: 'DataModel Element Is Read Only',
    405: 'Data Model Element Is Write Only',
    406: 'Data Model Element Type Mismatch',
    407: 'Data Model Element Value Out Of Range',
    408: 'Data Model Dependency Not Established'
};

window.API_1484_11 = new API_1484_11();



