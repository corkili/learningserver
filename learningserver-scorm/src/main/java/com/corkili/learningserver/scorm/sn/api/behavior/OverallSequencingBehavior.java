package com.corkili.learningserver.scorm.sn.api.behavior;

import com.sun.istack.internal.NotNull;

import com.corkili.learningserver.scorm.sn.api.behavior.result.DeliveryBehaviorResult;
import com.corkili.learningserver.scorm.sn.api.behavior.result.NavigationBehaviorResult;
import com.corkili.learningserver.scorm.sn.api.behavior.result.OverallSequencingResult;
import com.corkili.learningserver.scorm.sn.api.behavior.result.SequencingBehaviorResult;
import com.corkili.learningserver.scorm.sn.api.behavior.result.TerminationBehaviorResult;
import com.corkili.learningserver.scorm.sn.api.request.DeliveryRequest;
import com.corkili.learningserver.scorm.sn.api.request.NavigationRequest;
import com.corkili.learningserver.scorm.sn.api.request.SequencingRequest;
import com.corkili.learningserver.scorm.sn.api.request.TerminationRequest;

public class OverallSequencingBehavior {

    /**
     * Overall Sequencing Process [OP.1]
     *
     * Reference:
     *   Content Delivery Environment Process DB.2
     *   Delivery Request Process DB.1.1
     *   Navigation Request Process NB.2.1
     *   Sequencing Request Process SB.2.12
     *   Termination Request Process TB.2.3
     *
     * @see DeliveryBehavior#processContentDeliveryEnvironment(DeliveryRequest) DB.2
     * @see DeliveryBehavior#processDeliveryRequest(DeliveryRequest)  DB.1.1
     * @see NavigationBehavior#processNavigationRequest(NavigationRequest) NB.2.1
     * @see SequencingBehavior#processSequencingRequest(SequencingRequest) SB.2.12
     * @see TerminationBehavior#processTerminationRequest(TerminationRequest) TB.2.3
     */
    public static OverallSequencingResult overallSequencing(@NotNull NavigationRequest navigationRequest) {
        // 1.1
        NavigationBehaviorResult navigationBehaviorResult = NavigationBehavior.processNavigationRequest(navigationRequest);
        // 1.2
        if (!navigationBehaviorResult.isValidNavigationRequest()) {
            // 1.2.1 & 1.2.2
            // Behavior not specified.
            return new OverallSequencingResult(false)
                    .setException(navigationBehaviorResult.getException());
        }

        SequencingRequest sequencingRequest = navigationBehaviorResult.getSequencingRequest();

        // 1.3
        // If the current activity is active, end the attempt on the current activity.
        if (navigationBehaviorResult.getTerminationRequest() != null) {
            navigationBehaviorResult.getTerminationRequest().setTargetActivityTree(navigationRequest.getTargetActivityTree());
            // 1.3.1
            TerminationBehaviorResult terminationBehaviorResult = TerminationBehavior.processTerminationRequest(
                    navigationBehaviorResult.getTerminationRequest());
            // 1.3.2
            if (!terminationBehaviorResult.isValidTerminationRequest()) {
                // 1.3.2.1 & 1.3.2.2
                // Behavior not specified.
                return new OverallSequencingResult(false)
                        .setException(terminationBehaviorResult.getException());
            }
            // 1.3.3
            if (terminationBehaviorResult.getSequencingRequest() != null) {
                // 1.3.3.1
                // There can only be one pending sequencing request.
                // Use the one returned by the termination request process, if it exists.
                sequencingRequest = terminationBehaviorResult.getSequencingRequest();
            }
        }

        // 1.4
        DeliveryRequest deliveryRequest = null;
        if (sequencingRequest != null) {
            // 1.4.1
            SequencingBehaviorResult sequencingBehaviorResult = SequencingBehavior.processSequencingRequest(sequencingRequest);
            // 1.4.2
            if (!sequencingBehaviorResult.isValidSequencingRequest()) {
                // 1.4.2.1 & 1.4.2.2
                // Behavior not specified
                return new OverallSequencingResult(false)
                        .setException(sequencingBehaviorResult.getException());
            }
            // 1.4.3
            if (sequencingBehaviorResult.getEndSequencingSession() != null) {
                // 1.4.3.1
                // Exiting from the root of the activity tree ends the sequencing session;
                // return control to the LTS
                return new OverallSequencingResult(true, true)
                        .setEndSequencingSession(sequencingBehaviorResult.getEndSequencingSession());
            }
            // 1.4.4
            if (sequencingBehaviorResult.getDeliveryRequest() == null) {
                // 1.4.4.1
                return new OverallSequencingResult(true);
            }
            // 1.4.5
            // delivery request is for the activity identified by the Sequencing Request Process
            deliveryRequest = sequencingBehaviorResult.getDeliveryRequest();
        }

        // 1.5
        if (deliveryRequest != null) {
            // 1.5.1
            DeliveryBehaviorResult deliveryBehaviorResult = DeliveryBehavior.processDeliveryRequest(deliveryRequest);
            // 1.5.2
            if (!deliveryBehaviorResult.isValidDeliveryRequest()) {
                // 1.5.2.1 & 1.5.2.2
                // Behavior not specified
                return new OverallSequencingResult(false)
                        .setException(deliveryBehaviorResult.getException());
            }
            // 1.5.3
            deliveryBehaviorResult = DeliveryBehavior.processContentDeliveryEnvironment(deliveryRequest);
            if (deliveryBehaviorResult.getException() != null) {
                return new OverallSequencingResult(false)
                        .setException(deliveryBehaviorResult.getException());
            }
        }

        return new OverallSequencingResult(true);
    }

}
