package com.corkili.learningserver.scorm.rte.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.corkili.learningserver.scorm.common.CommonUtils;
import com.corkili.learningserver.scorm.rte.model.Interactions.Instance;
import com.corkili.learningserver.scorm.rte.model.Pattern.PerformanceObject.Step;
import com.corkili.learningserver.scorm.rte.model.datatype.AbstractTerminalDataType;
import com.corkili.learningserver.scorm.rte.model.datatype.LocalizedString;
import com.corkili.learningserver.scorm.rte.model.datatype.Real7;
import com.corkili.learningserver.scorm.rte.model.error.ScormError;
import com.corkili.learningserver.scorm.rte.model.result.ScormResult;

public class Pattern extends AbstractTerminalDataType {

    private static final String CASE_TRUE = "{case_matters=true}";
    private static final String CASE_FALSE = "{case_matters=false}";
    private static final String ORDER_TRUE = "{order_matters=true}";
    private static final String ORDER_FALSE = "{order_matters=false}";

    private Object patternObject;
    private String pattern;

    private Interactions.Instance superSuperContainer;

    public Pattern(Instance superSuperContainer) {
        this.superSuperContainer = superSuperContainer;
    }

    @Override
    public ScormResult set(String value) {
        ScormResult scormResult = super.handleSet(this, value);
        if (!scormResult.getError().equals(ScormError.E_0)) {
            return scormResult;
        }
        boolean isUnique = true;
        for (CorrectResponses.Instance instance : superSuperContainer.getCorrectResponses().getInstances()) {
            if (StringUtils.equals(instance.getPattern().getPattern(), value)) {
                isUnique = false;
                break;
            }
        }
        if (!isUnique) {
            return new ScormResult("false", ScormError.E_351);
        }
        if (!set0(value)) {
            return new ScormResult("false", ScormError.E_406);
        }
        return scormResult;
    }

    @Override
    public ScormResult get() {
        ScormResult scormResult = super.handleGet(this);
        if (!scormResult.getError().equals(ScormError.E_0)) {
            return scormResult;
        }
        return scormResult.setReturnValue(pattern);
    }

    private boolean set0(String value) {
        String type = superSuperContainer.getType().getValue();
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
        this.pattern = value;
        this.patternObject = value;
        return true;
    }

    private boolean setForNumeric(String value) {
        String[] ans = value.split("\\[:]");
        NumericObject numericObject = new NumericObject();
        if (ans.length > 2) {
            return false;
        } else if (ans.length == 1) {
            Real7 m = new Real7(ans[0]);
            if (m.getValue() == null) {
                return false;
            }
            if (value.startsWith("[:]")) {
                numericObject.max = m;
            } else {
                numericObject.min = m;
            }
        } else if (ans.length == 2) {
            Real7 min = new Real7(ans[0]);
            Real7 max = new Real7(ans[1]);
            if (min.getValue() == null || max.getValue() == null) {
                return false;
            }
            numericObject.min = min;
            numericObject.max = max;
        }
        this.patternObject = numericObject;
        this.pattern = value;
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
        this.pattern = value;
        this.patternObject = sequencing;
        return true;
    }

    private boolean setForPerformance(String value) {
        PerformanceObject performanceObject = new PerformanceObject();
        String tmp = value;
        boolean flag = false;
        if (tmp.startsWith(ORDER_TRUE)) {
            performanceObject.orderMatters = true;
            flag = true;
        } else if (tmp.startsWith(ORDER_FALSE)) {
            performanceObject.orderMatters = false;
            flag = true;
        }
        if (flag) {
            tmp = tmp.substring(0, tmp.indexOf("}") + 1);
        }
        String[] steps = tmp.split("\\[,]");
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
            if (!content[1].contains("[:]")) {
                stepObject.answerType = "literal";
                stepObject.answerObject = content[1];
            } else {
                String[] ans = content[1].split("\\[:]");
                NumericObject numericObject = new NumericObject();
                if (ans.length > 2) {
                    return false;
                } else if (ans.length == 1) {
                    Real7 m = new Real7(ans[0]);
                    if (m.getValue() == null) {
                        return false;
                    }
                    if (content[1].startsWith("[:]")) {
                        numericObject.max = m;
                    } else {
                        numericObject.min = m;
                    }
                } else if (ans.length == 2) {
                    Real7 min = new Real7(ans[0]);
                    Real7 max = new Real7(ans[1]);
                    if (min.getValue() == null || max.getValue() == null) {
                        return false;
                    }
                    numericObject.min = min;
                    numericObject.max = max;
                }
                stepObject.answerType = "numeric";
                stepObject.answerObject = numericObject;
            }
            performanceObject.steps.add(stepObject);
        }
        this.patternObject = performanceObject;
        this.pattern = value;
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
        this.pattern = value;
        this.patternObject = matchingObject;
        return true;
    }

    private boolean setForLikert(String value) {
        if (CommonUtils.isLegalURI(value)) {
            this.patternObject = value;
            this.pattern = value;
            return true;
        } else {
            return false;
        }
    }

    private boolean setForLongFillIn(String value) {
        LongFillInObject longFillInObject = new LongFillInObject();
        String tmp = value;
        boolean flag = false;
        if (tmp.startsWith(CASE_TRUE)) {
            longFillInObject.caseMatters = true;
            flag = true;
        } else if (tmp.startsWith(CASE_FALSE)) {
            longFillInObject.caseMatters = false;
            flag = true;
        }
        if (flag) {
            tmp = tmp.substring(0, tmp.indexOf("}") + 1);
        }
        LocalizedString matchText = new LocalizedString(tmp);
        if (matchText.getValue() != null) {
            longFillInObject.matchText = matchText;
            this.patternObject = longFillInObject;
            this.pattern = value;
            return true;
        } else {
            return false;
        }
    }

    private boolean setForFillIn(String value) {
        FillInObject fillInObject = new FillInObject();
        String tmp = value;
        boolean flag = false;
        if (tmp.startsWith(CASE_TRUE)) {
            fillInObject.caseMatters = true;
            flag = true;
        } else if (tmp.startsWith(CASE_FALSE)) {
            fillInObject.caseMatters = false;
            flag = true;
        } else if (tmp.startsWith(ORDER_TRUE)) {
            fillInObject.orderMatters = true;
            flag = true;
        } else if (tmp.startsWith(ORDER_FALSE)) {
            fillInObject.orderMatters = false;
            flag = true;
        }
        if (flag) {
            tmp = tmp.substring(0, tmp.indexOf("}") + 1);
            flag = false;
            if (tmp.startsWith(CASE_TRUE)) {
                fillInObject.caseMatters = true;
                flag = true;
            } else if (tmp.startsWith(CASE_FALSE)) {
                fillInObject.caseMatters = false;
                flag = true;
            } else if (tmp.startsWith(ORDER_TRUE)) {
                fillInObject.orderMatters = true;
                flag = true;
            } else if (tmp.startsWith(ORDER_FALSE)) {
                fillInObject.orderMatters = false;
                flag = true;
            }
            if (flag) {
                tmp = tmp.substring(0, tmp.indexOf("}") + 1);
            }
        }
        String[] matchTexts = tmp.split("\\[,]");
        flag = true;
        for (String matchText : matchTexts) {
            LocalizedString mt = new LocalizedString(matchText);
            if (mt.getValue() == null) {
                flag = false;
                break;
            }
            fillInObject.matchTextList.add(mt);
        }
        if (flag) {
            this.patternObject = fillInObject;
            this.pattern = value;
            return true;
        } else {
            return false;
        }
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
            this.patternObject = shortIDList;
            this.pattern = value;
            return true;
        } else {
            return false;
        }
    }

    private boolean setForTrueFalse(String value) {
        if (StringUtils.equals(value, "true") || StringUtils.equals(value, "false")) {
            this.patternObject = value;
            this.pattern = value;
            return true;
        } else {
            return false;
        }
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        set0(pattern);
    }

    public Object getPatternObject() {
        return patternObject;
    }

    public static class FillInObject {
        boolean caseMatters;
        boolean orderMatters;
        private List<LocalizedString> matchTextList;

        public FillInObject() {
            caseMatters = false;
            orderMatters = true;
            matchTextList = new ArrayList<>();
        }

        public boolean isCaseMatters() {
            return caseMatters;
        }

        public void setCaseMatters(boolean caseMatters) {
            this.caseMatters = caseMatters;
        }

        public boolean isOrderMatters() {
            return orderMatters;
        }

        public void setOrderMatters(boolean orderMatters) {
            this.orderMatters = orderMatters;
        }

        public List<LocalizedString> getMatchTextList() {
            return matchTextList;
        }

        public void setMatchTextList(List<LocalizedString> matchTextList) {
            this.matchTextList = matchTextList;
        }
    }

    public static class LongFillInObject {
        boolean caseMatters;
        private LocalizedString matchText;

        public LongFillInObject() {
            caseMatters = false;
        }

        public boolean isCaseMatters() {
            return caseMatters;
        }

        public void setCaseMatters(boolean caseMatters) {
            this.caseMatters = caseMatters;
        }

        public LocalizedString getMatchText() {
            return matchText;
        }

        public void setMatchText(LocalizedString matchText) {
            this.matchText = matchText;
        }
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

    public static class PerformanceObject {
        private boolean orderMatters;
        private List<Step> steps;

        public PerformanceObject() {
            orderMatters = true;
            steps = new ArrayList<>();
        }

        public boolean isOrderMatters() {
            return orderMatters;
        }

        public void setOrderMatters(boolean orderMatters) {
            this.orderMatters = orderMatters;
        }

        public List<Step> getSteps() {
            return steps;
        }

        public void setSteps(List<Step> steps) {
            this.steps = steps;
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

    public static class NumericObject {
        private Real7 min;
        private Real7 max;

        public Real7 getMin() {
            return min;
        }

        public void setMin(Real7 min) {
            this.min = min;
        }

        public Real7 getMax() {
            return max;
        }

        public void setMax(Real7 max) {
            this.max = max;
        }
    }

}
