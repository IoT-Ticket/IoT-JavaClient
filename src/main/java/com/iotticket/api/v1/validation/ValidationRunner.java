package com.iotticket.api.v1.validation;

import com.iotticket.api.v1.exception.ValidAPIParamException;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationRunner {

    final Map<String, Pattern> PATTERN_CACHE = new HashMap<String, Pattern>(1);

    public void runValidation(Validatable validatable) throws ValidAPIParamException {
        try {
            for (Field f : getFields(validatable.getClass())) {
                f.setAccessible(true);
                APIRequirement req = f.getAnnotation(APIRequirement.class);
                if (req == null) {
                    continue;
                }
                int maxlength = req.maxLength();
                String regexPattern = req.regexPattern();
                boolean isNullable = req.nullable();
                Class<?> type = f.getType();
                if (type.isAssignableFrom(String.class)) {
                    String value = (String) f.get(validatable);
                    checkStringField(value, maxlength, regexPattern, isNullable, f.getName());
                } else if (Collection.class.isAssignableFrom(type)) {
                    Collection collection = (Collection) f.get(validatable);
                    checkCollection(collection, maxlength, isNullable, f.getName());
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    private Collection<Field> getFields(Class clz) {
        List<Field> allFields = new ArrayList<Field>();
        Field[] fields = clz.getDeclaredFields();
        allFields.addAll(Arrays.asList(fields));

        Class superclass = clz.getSuperclass();
        if (superclass != null) {
            allFields.addAll(getFields(superclass));
        }
        return allFields;
    }

    private void checkCollection(Collection collection, int maxlength, boolean isNullable, String fieldName) throws ValidAPIParamException {

        if (isNullable && collection == null) return;

        if (!isNullable) {
            if (collection == null || collection.isEmpty()) {
                String msg = MessageFormat.format("{0} is needed", fieldName);
                throw new ValidAPIParamException(msg);
            }
        }

        if (collection.size() > maxlength) {
            String msg = MessageFormat.format("{0} size exceeds {1}  expected", fieldName, maxlength);
            throw new ValidAPIParamException(msg);
        }


        for (Object o : collection) {
            if (o instanceof Validatable) {
                Validatable vo = (Validatable) o;
                runValidation(vo);
            }
        }

    }

    private void checkStringField(String paramValue, int maxChar, String regexPattern, boolean isNullable, String fieldName) throws ValidAPIParamException {

        if (isNullable && paramValue == null) return;
        if (!isNullable && (paramValue == null || paramValue.isEmpty())) {
            String msg = MessageFormat.format("{0} is needed", fieldName);
            throw new ValidAPIParamException(msg);
        }
        if (maxChar != APIRequirement.UNRESTRICTED && paramValue.length() > maxChar) {
            String msg = MessageFormat.format("{0} attribute exceeds {1} characters expected", fieldName, maxChar);
            throw new ValidAPIParamException(msg);
        }
        if (regexPattern != null && !regexPattern.isEmpty()) {
            Matcher matcher = getPattern(regexPattern).matcher(paramValue);
            boolean match = matcher.matches();
            if (!match) {
                String msg = MessageFormat.format("{0} attribute value {1} is unacceptable. Check documentation", fieldName, paramValue);
                throw new ValidAPIParamException(msg);
            }
        }

    }

    private Pattern getPattern(String regexPattern) {
        if (!PATTERN_CACHE.containsKey(regexPattern)) {
            Pattern pattern = Pattern.compile(regexPattern);
            PATTERN_CACHE.put(regexPattern, pattern);
        }
        return PATTERN_CACHE.get(regexPattern);

    }
}


