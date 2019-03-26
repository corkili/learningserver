package com.corkili.learningserver.service;

import com.corkili.learningserver.bo.Scorm;
import com.corkili.learningserver.common.ServiceResult;

public interface ScormService extends Service<Scorm, com.corkili.learningserver.po.Scorm> {

    ServiceResult importScorm(Scorm scorm, byte[] scormZipData);

    ServiceResult deleteScorm(Long scormId);

}
