package com.corkili.learningserver.scorm.rte.model;

import com.corkili.learningserver.scorm.rte.model.annotation.Meta;
import com.corkili.learningserver.scorm.rte.model.datatype.GeneralDataType;

public class Nav implements GeneralDataType {

    @Meta("request")
    private Request request;

    @Meta("request_valid")
    private RequestValid requestValid;

    public Nav() {
        this.request = new Request();
        this.requestValid = new RequestValid();
    }

    public Request getRequest() {
        return request;
    }

    public Nav setRequest(Request request) {
        this.request = request;
        return this;
    }

    public RequestValid getRequestValid() {
        return requestValid;
    }

    public Nav setRequestValid(RequestValid requestValid) {
        this.requestValid = requestValid;
        return this;
    }
}
