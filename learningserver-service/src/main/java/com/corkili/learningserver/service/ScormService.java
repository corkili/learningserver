package com.corkili.learningserver.service;

import com.corkili.learningserver.bo.Scorm;
import com.corkili.learningserver.common.ServiceResult;
import com.corkili.learningserver.scorm.sn.api.event.NavigationEvent;

public interface ScormService extends Service<Scorm, com.corkili.learningserver.po.Scorm> {

    ServiceResult importScorm(Scorm scorm, byte[] scormZipData);

    ServiceResult deleteScorm(Long scormId);

    ServiceResult queryCatalog(Long scormId);

    ServiceResult processNavigationEvent(NavigationEvent navigationEvent, Long userId, Long scormId, String level1CatalogItemId);

}
