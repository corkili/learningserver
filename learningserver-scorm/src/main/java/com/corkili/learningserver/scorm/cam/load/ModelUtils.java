package com.corkili.learningserver.scorm.cam.load;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

import net.sourceforge.cardme.engine.VCardEngine;
import net.sourceforge.cardme.vcard.VCardImpl;
import net.sourceforge.cardme.vcard.exceptions.VCardParseException;

import com.corkili.learningserver.scorm.cam.model.datatype.AnyURI;
import com.corkili.learningserver.scorm.cam.model.datatype.Decimal;
import com.corkili.learningserver.scorm.cam.model.datatype.ID;
import com.corkili.learningserver.scorm.cam.model.datatype.IDRef;
import com.corkili.learningserver.scorm.cam.model.datatype.NonNegativeInteger;
import com.corkili.learningserver.scorm.cam.model.datatype.Token;
import com.corkili.learningserver.scorm.cam.model.datatype.VCard;
import com.corkili.learningserver.scorm.common.CommonUtils;

public class ModelUtils {

    public static boolean isAnyUriEmpty(AnyURI anyURI) {
        if (anyURI != null) {
            return StringUtils.isBlank(anyURI.getValue());
        } else {
            return true;
        }
    }

    public static boolean isAnyUriFormatCorrect(AnyURI anyURI) {
        if (!isAnyUriEmpty(anyURI)) {
            try {
                URI.create(anyURI.getValue());
                return true;
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
    }

    public static boolean isDecimalEmpty(Decimal decimal) {
        if (decimal != null) {
            return StringUtils.isBlank(decimal.getValue());
        } else {
            return true;
        }
    }

    public static boolean isDecimalInRange(Decimal decimal, double minimum, double maximum, int scale) {
        if (!isDecimalEmpty(decimal)) {
            BigDecimal min = new BigDecimal(minimum).setScale(scale, BigDecimal.ROUND_HALF_UP);
            BigDecimal max = new BigDecimal(maximum).setScale(scale, BigDecimal.ROUND_HALF_UP);
            BigDecimal num = BigDecimal.valueOf(Double.valueOf(decimal.getValue()))
                    .setScale(scale, BigDecimal.ROUND_HALF_UP);
            return min.compareTo(num) <= 0 && num.compareTo(max) <= 0;
        } else {
            return false;
        }
    }

    public static boolean isIDEmpty(ID id) {
        if (id != null) {
            return StringUtils.isBlank(id.getValue());
        } else {
            return true;
        }
    }

    public static boolean isIDRefEmpty(IDRef idRef) {
        if (idRef != null) {
            return StringUtils.isBlank(idRef.getValue());
        } else {
            return true;
        }
    }

    public static boolean isNonNegativeIntegerEmpty(NonNegativeInteger nonNegativeInteger) {
        if (nonNegativeInteger != null) {
            return StringUtils.isBlank(nonNegativeInteger.getValue());
        } else {
            return true;
        }
    }

    public static boolean isNonNegativeIntegerInRange(NonNegativeInteger nonNegativeInteger, int min, int max) {
        if (!isNonNegativeIntegerEmpty(nonNegativeInteger)) {
            return min <= nonNegativeInteger.getIntValue() && nonNegativeInteger.getIntValue() <= max;
        } else {
            return false;
        }
    }

    public static boolean isTokenEmpty(Token token) {
        if (token != null) {
            return StringUtils.isBlank(token.getValue());
        } else {
            return true;
        }
    }

    public static boolean isLegalToken(Token token, String... vocabularyTable) {
        if (!isTokenEmpty(token)) {
            return Arrays.asList(vocabularyTable).contains(token.getValue());
        } else {
            return false;
        }
    }

    public static boolean isLegalVocabulary(String data, String... vocabularyTable) {
        if (StringUtils.isNotBlank(data)) {
            return Arrays.asList(vocabularyTable).contains(data);
        } else {
            return false;
        }
    }

    public static boolean isVCardEmpty(VCard vCard) {
        if (vCard != null) {
            return StringUtils.isBlank(vCard.getValue());
        } else {
            return true;
        }
    }

    public static boolean isLegalVCard(VCard vCard) {
        if (!isVCardEmpty(vCard)) {
            try {
                return ((VCardImpl) new VCardEngine().parse(vCard.getValue())).getErrors().size() == 0;
            } catch (IOException | VCardParseException e) {
                return false;
            }
        } else {
            return false;
        }
    }

    public static boolean isDateTimeFormatCorrect(String dateTime) {
        if (!StringUtils.isBlank(dateTime)) {
            try {
                Instant.parse(dateTime);
                return true;
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
    }

    public static boolean isDurationFormatCorrect(String duration) {
        if (!StringUtils.isBlank(duration)) {
            try {
                Duration.parse(duration);
                return true;
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
    }

    public static boolean isParametersFormatCorrect(String parameters) {
        String testURI = concatURI("http://www.t.org", parameters);
        return isAnyUriFormatCorrect(testURI == null ? null : new AnyURI(testURI));
    }

    public static String concatURI(String uri, String parameters) {
        if (StringUtils.isAnyBlank(uri, parameters)) {
            return null;
        }
        while (parameters.charAt(0) == '?' || parameters.charAt(0) == '&') {
            parameters = parameters.substring(1);
        }
        if (parameters.charAt(0) == '#') {
            if (!uri.contains("#")) {
                uri += parameters;
            }
            return uri;
        }
        if (uri.contains("?")) {
            uri += "&";
        } else {
            uri += "?";
        }
        uri += parameters;
        return uri;
    }

    public static boolean isLegalLanguage(String language) {
        return CommonUtils.isLegalLanguage(language);
    }

}
