package com.corkili.learningserver.scorm.rte.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.corkili.learningserver.scorm.common.CommonUtils;
import com.corkili.learningserver.scorm.rte.model.Interactions.Instance;
import com.corkili.learningserver.scorm.rte.model.datatype.AbstractTerminalDataType;
import com.corkili.learningserver.scorm.rte.model.datatype.LocalizedString;
import com.corkili.learningserver.scorm.rte.model.datatype.Real7;
import com.corkili.learningserver.scorm.rte.model.error.ScormError;
import com.corkili.learningserver.scorm.rte.model.result.ScormResult;

// TODO 4.2.9.2 page 133
public class LearnerResponse extends AbstractTerminalDataType {

    private Object learnerResponseObject;
    private String learnerResponse;

    private Interactions.Instance container;

    public LearnerResponse(Instance container) {
        this.container = container;
    }

    @Override
    public ScormResult set(String value) {
        ScormResult scormResult = super.handleSet(this, value);
        if (!scormResult.getError().equals(ScormError.E_0)) {
            return scormResult;
        }
        if (!set0(value)) {
            return new ScormResult("false", ScormError.E_406);
        }
        this.learnerResponse = value;
        return scormResult;
    }

    @Override
    public ScormResult get() {
        ScormResult scormResult = super.handleGet(this);
        if (!scormResult.getError().equals(ScormError.E_0)) {
            return scormResult;
        }
        return scormResult.setReturnValue(learnerResponse);
    }

    private boolean set0(String value) {
        String type = container.getType().getValue();
        if (StringUtils.isBlank(type)) {
            return true;
        }
        if (StringUtils.isBlank(value)) {
            return false;
        }
        switch (type) {
            case "true_false":
                return setForTrueFalse(value);
            case "multiple_choice":
                return setForMultipleChoice(value);
            case "fill_in":
                return setForFillIn(value);
            case "long_fill_in":
                return setForLongFillIn(value);
            case "likert":
                return setForLikert(value);
            case "matching":
                return setForMatching(value);
            case "performance":
                return setForPerformance(value);
            case "sequencing":
                return setForSequencing(value);
            case "numeric":
                return setForNumeric(value);
            case "other":
                return setForOther(value);
        }
        return true;
    }

    private boolean setForOther(String value) {
        this.learnerResponse = value;
        this.learnerResponseObject = value;
        return true;
    }

    private boolean setForNumeric(String value) {
        Real7 numericObject = new Real7(value);

        if (numericObject.getValue() == null) {
            return false;
        }

        this.learnerResponseObject = numericObject;
        this.learnerResponse = value;
        return true;
    }

    private boolean setForSequencing(String value) {
        String[] records = value.split("\\[,]");
        List<String> sequencing = new ArrayList<>(records.length);
        for (String record : records) {
            if (!CommonUtils.isLegalURI(record)) {
                return false;
            }
            sequencing.add(record);
        }
        this.learnerResponse = value;
        this.learnerResponseObject = sequencing;
        return true;
    }

    private boolean setForPerformance(String value) {
        List<Step> performanceObject = new ArrayList<>();
        String[] steps = value.split("\\[,]");
        for (String step : steps) {
            String[] content = step.split("\\[\\.]");
            if (content.length != 2) {
                return false;
            }
            if (!CommonUtils.isLegalURI(content[0])) {
                return false;
            }
            Step stepObject = new Step();
            stepObject.name = content[0];
            stepObject.answer = content[1];

            Real7 ans = new Real7(content[1]);

            if (ans.getValue() == null) {
                stepObject.answerType = "literal";
                stepObject.answerObject = content[1];
            } else {
                stepObject.answerType = "numeric";
                stepObject.answerObject = ans;
            }

            performanceObject.add(stepObject);
        }
        this.learnerResponseObject = performanceObject;
        this.learnerResponse = value;
        return true;
    }

    private boolean setForMatching(String value) {
        MatchingObject matchingObject = new MatchingObject();
        String[] records = value.split("\\[,]");
        for (String record : records) {
            String[] content = record.split("\\[\\.]");
            if (content.length != 2) {
                return false;
            }
            if (!CommonUtils.isLegalURI(content[0]) || !CommonUtils.isLegalURI(content[1])) {
                return false;
            }
            matchingObject.records.add(content);
        }
        this.learnerResponse = value;
        this.learnerResponseObject = matchingObject;
        return true;
    }

    private boolean setForLikert(String value) {
        if (CommonUtils.isLegalURI(value)) {
            this.learnerResponseObject = value;
            this.learnerResponse = value;
            return true;
        } else {
            return false;
        }
    }

    private boolean setForLongFillIn(String value) {
        LocalizedString matchText = new LocalizedString(value);
        if (matchText.getValue() != null) {
            this.learnerResponseObject = matchText;
            this.learnerResponse = value;
            return true;
        } else {
            return false;
        }
    }

    private boolean setForFillIn(String value) {
        String[] matchTexts = value.split("\\[,]");
        List<LocalizedString> fillInObject = new ArrayList<>();
        for (String matchText : matchTexts) {
            LocalizedString mt = new LocalizedString(matchText);
            if (mt.getValue() == null) {
                return false;
            }
            fillInObject.add(mt);
        }
        this.learnerResponseObject = fillInObject;
        this.learnerResponse = value;
        return true;
    }

    private boolean setForMultipleChoice(String value) {
        String[] content = value.split("\\[,]");
        boolean success = true;
        List<String> shortIDList = new ArrayList<>(content.length);
        for (String s : content) {
            if (!CommonUtils.isLegalURI(s)) {
                success = false;
                break;
            }
            shortIDList.add(s);
        }
        if (success) {
            this.learnerResponseObject = shortIDList;
            this.learnerResponse = value;
            return true;
        } else {
            return false;
        }
    }

    private boolean setForTrueFalse(String value) {
        if (StringUtils.equals(value, "true") || StringUtils.equals(value, "false")) {
            this.learnerResponseObject = value;
            this.learnerResponse = value;
            return true;
        } else {
            return false;
        }
    }

    public String getLearnerResponse() {
        return learnerResponse;
    }

    public void setLearnerResponse(String learnerResponse) {
        set0(learnerResponse);
    }

    public Object getLearnerResponseObject() {
        return learnerResponseObject;
    }

    public static class MatchingObject {
        List<String[]> records;

        public MatchingObject() {
            records = new ArrayList<>();
        }

        public List<String[]> getRecords() {
            return records;
        }

        public void setRecords(List<String[]> records) {
            this.records = records;
        }
    }

    public static class Step {
        private String name;
        private String answerType;
        private Object answerObject;
        private String answer;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAnswerType() {
            return answerType;
        }

        public void setAnswerType(String answerType) {
            this.answerType = answerType;
        }

        public Object getAnswerObject() {
            return answerObject;
        }

        public void setAnswerObject(Object answerObject) {
            this.answerObject = answerObject;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }
    }


}
