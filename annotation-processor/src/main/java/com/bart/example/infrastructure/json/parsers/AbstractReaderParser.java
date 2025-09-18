package com.bart.example.infrastructure.json.parsers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.BiConsumer;

public class AbstractReaderParser {
    protected static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    protected static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    protected static final String SELECT_FIRST_AND_LAST_QUOTE = "^\\\"|\\\"$";


    protected <T> T fromJson(T obj, String jsonString, BiConsumer<String, String> fieldSetter) {
        String content = jsonString.trim();
        if (content.startsWith("{") && content.endsWith("}")) {
            content = content.substring(1, content.length() - 1).trim();
        }
        if (content.isEmpty()) return obj;
        String[] pairs = splitJsonPairs(content);
        for (String pair : pairs) {
            if (pair.trim().isEmpty()) continue;
            String[] keyValue = splitKeyValue(pair);
            if (keyValue.length == 2) {
                String key = unescapeJson(keyValue[0].replaceAll(SELECT_FIRST_AND_LAST_QUOTE, "")).trim();
                String value = keyValue[1].trim();
                fieldSetter.accept(key, value);
            }
        }

        return obj;
    }

   public boolean isNull(String jsonString) {
       return jsonString == null || jsonString.trim().isEmpty() || jsonString.equals("null");
   }

   public String toSTRING(String value) {
       return unescapeJson(value.replaceAll(SELECT_FIRST_AND_LAST_QUOTE, ""));
   }
    public int toINTEGER(String value) {
        return Integer.parseInt(value);
    }
    public double toDOUBLE(String value) {
        return Double.parseDouble(value);
    }
    public long toLONG(String value) {
        return Long.parseLong(value);
    }
    public boolean toBOOLEAN(String value) {
        return Boolean.parseBoolean(value);
    }
    public Date toDATE(String value) {
        return new Date(Date.parse(value.replaceAll(SELECT_FIRST_AND_LAST_QUOTE, "")));
    }
    public LocalDate toLOCAL_DATE(String value) {
        return LocalDate.parse(value.replaceAll(SELECT_FIRST_AND_LAST_QUOTE, ""),DATE_FORMATTER);
    }
    public LocalDateTime toLOCAL_DATE_TIME(String value) {
        return LocalDateTime.parse(value.replaceAll(SELECT_FIRST_AND_LAST_QUOTE, ""),DATETIME_FORMATTER);
    }
    public LocalDate toLIST(String value) {
       // Not yet support for lists
        return null;
    }

   public static String[] splitJsonPairs(String jsonContent) {
       List<String> pairs = new ArrayList<>();
       int braceDepth = 0;
       int bracketDepth = 0;
       boolean inString = false;
       int start = 0;
       for (int i = 0; i < jsonContent.length(); i++) {
           char c = jsonContent.charAt(i);
           if (c == '\"' && (i == 0 || jsonContent.charAt(i - 1) != '\\')) {
               inString = !inString;
           } else if (!inString) {
               if (c == '{') braceDepth++;
               else if (c == '}') braceDepth--;
               else if (c == '[') bracketDepth++;
               else if (c == ']') bracketDepth--;
               else if (c == ',' && braceDepth == 0 && bracketDepth == 0) {
                   pairs.add(jsonContent.substring(start, i).trim());
                   start = i + 1;
               }
           }
       }
       if (start < jsonContent.length()) {
           pairs.add(jsonContent.substring(start).trim());
       }
       return pairs.toArray(new String[0]);
   }

   public String[] splitKeyValue(String pair) {
       boolean inString = false;
       for (int i = 0; i < pair.length(); i++) {
           char c = pair.charAt(i);
           if (c == '\"' && (i == 0 || pair.charAt(i - 1) != '\\')) {
               inString = !inString;
           } else if (!inString && c == ':')
               return new String[]{
                       pair.substring(0, i).trim(),
                       pair.substring(i + 1).trim()
               };
       }
       return new String[]{pair};
   }

    public String unescapeJson(String input) {
        if (input == null) return null;
        return input.replace("\"", "\\\"")
                .replace("\\", "\\\\")
                .replace("\b", "\\b")
                .replace("\f", "\\f")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
