package com.corkili.learningserver.scorm.sn.api.behavior;

import com.corkili.learningserver.scorm.sn.api.behavior.result.SequencingBehaviorResult;
import com.corkili.learningserver.scorm.sn.api.request.SequencingRequest;

public class SequencingBehavior {

    /**
     * Sequencing Request Process [SB.2.12]
     *
     * For a sequencing request; validates the sequencing request; my return a delivery request; may indicate
     * control be returned to the LTS; may return an exception code.
     *
     * Reference:
     *   Choice Sequencing Request Process SB.2.9
     *   Continue Sequencing Request Process SB.2.7
     *   Exit Sequencing Request Process SB.2.11
     *   Previous Sequencing Request Process SB.2.8
     *   Resume All Sequencing Request Process SB.2.6
     *   Retry Sequencing Request Process SB.2.10
     *   Start Sequencing Request Process SB.2.5
     *
     */
    public static SequencingBehaviorResult processSequencingRequest(SequencingRequest sequencingRequest) {
        return new SequencingBehaviorResult();
    }

}
