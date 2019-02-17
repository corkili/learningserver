package com.corkili.learningserver.service;

import com.corkili.learningserver.bo.SubmittedCourseWork;
import com.corkili.learningserver.common.ServiceResult;

public interface SubmittedCourseWorkService extends Service<SubmittedCourseWork, com.corkili.learningserver.po.SubmittedCourseWork> {

    ServiceResult deleteSubmittedCourseWork(Long submittedCourseWorkId);

}
