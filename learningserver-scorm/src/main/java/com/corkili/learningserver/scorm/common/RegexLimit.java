package com.corkili.learningserver.scorm.common;

import java.util.regex.Pattern;

public class RegexLimit implements Limit<String> {

    private Pattern pattern;

    public RegexLimit(String regex) {
        pattern = Pattern.compile(regex);
    }

    @Override
    public boolean conform(String data) {
        return pattern.matcher(data).matches();
    }
}
