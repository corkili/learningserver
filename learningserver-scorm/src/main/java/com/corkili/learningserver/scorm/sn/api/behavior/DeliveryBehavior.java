package com.corkili.learningserver.scorm.sn.api.behavior;

import com.corkili.learningserver.scorm.sn.api.behavior.result.DeliveryBehaviorResult;
import com.corkili.learningserver.scorm.sn.api.request.DeliveryRequest;

public class DeliveryBehavior {

    /**
     * Delivery Request Process [DB.1.1]
     *
     * For a delivery request; returns the validity of the delivery request; may return an exception code.
     *
     * Reference:
     *   Check Activity Process UP.5
     *
     */
    public static DeliveryBehaviorResult processDeliveryRequest(DeliveryRequest deliveryRequest) {
        return new DeliveryBehaviorResult();
    }

    /**
     * Content Delivery Environment Process [DB.2]
     *
     * For a delivery request; may return an exception code.
     *
     * Reference:
     *   Activity Progress Status TM.1.2.1
     *   Activity Attempt Count TM.1.2.1
     *   Activity is Active AM.1.1
     *   Activity is Suspended AM.1.1
     *   Attempt Absolute Duration TM.1.2.2
     *   Attempt Experienced Duration TM.1.2.2
     *   Attempt Progress Information TM.1.2.2
     *   Clear Suspended Activity Subprocess DB.2.1
     *   Current Activity AM.1.2
     *   Objective Progress Information TM.1.1
     *   Suspended Activity AM.1.2
     *   Terminate Descendent Attempts Process UP.4
     *   Tracked SM.11
     *
     */
    public static DeliveryBehaviorResult processContentDeliveryEnvironment(DeliveryRequest deliveryRequest) {
        return new DeliveryBehaviorResult();
    }

}
