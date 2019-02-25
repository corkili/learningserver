package com.corkili.learningserver.bo;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.corkili.learningserver.common.ServiceUtils;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ExamQuestion implements BusinessObject {

    public ExamQuestion() {
        score = new HashMap<>();
    }

    private Long id;

    private Date createTime;

    private Date updateTime;

    private Integer index;

    private Long belongExamId;

    private Long questionId;

    /**
     * if questionType is MultipleFilling,
     * use ; divide each index/score pair, use , divide index and score in each pair
     */
    private Map<Integer, Double> score;

    public void setScore(String scoreStr) {
        if (StringUtils.isBlank(scoreStr)) {
            return;
        }
        Map<Integer, Double> scoreMap = new HashMap<>();
        if (scoreStr.contains(",")) {
            List<String> pairList = ServiceUtils.string2List(scoreStr, Pattern.compile(";"));
            for (String pair : pairList) {
                String[] tmp = pair.split(",");
                scoreMap.put(Integer.valueOf(tmp[0]), Double.valueOf(tmp[1]));
            }
        } else if (!scoreStr.contains(",") && !scoreStr.contains(";")) {
            scoreMap.put(-1, Double.valueOf(scoreStr));
        }
        this.score = scoreMap;
    }

    public String getScoreStr() {
        if (score.isEmpty()) {
            return "";
        }
        if (score.containsKey(-1)) {
            return String.valueOf(score.get(-1));
        }
        List<String> pairList = new LinkedList<>();
        for (Entry<Integer, Double> entry : score.entrySet()) {
            pairList.add(entry.getKey().toString() + "," + entry.getValue().toString());
        }
        return ServiceUtils.list2String(pairList, ";");
    }

    public void setScore(double score) {
        Map<Integer, Double> scoreMap = new HashMap<>();
        scoreMap.put(-1, score);
        this.score = scoreMap;
    }

    public ExamQuestion putScore(int index, double score) {
        if (!this.score.containsKey(-1)) {
            this.score.put(index, score);
        }
        return this;
    }

    public Double getScore() {
        return this.score.get(-1);
    }

    public Double getScore(int index) {
        return this.score.get(index);
    }

    public Map<Integer, Double> getScoreMap() {
        return this.score;
    }
}
