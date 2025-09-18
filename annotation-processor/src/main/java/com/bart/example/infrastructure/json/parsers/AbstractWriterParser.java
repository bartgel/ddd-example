package com.bart.example.infrastructure.json.parsers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.function.Consumer;

public abstract class AbstractWriterParser {
    protected static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    protected static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;


    protected void appendToJson(StringBuilder json, String field, boolean isNull, Consumer<StringBuilder> appendThisField) {
        json.append("\"").append(field).append("\":");
        if (isNull) {
            json.append("null");
        } else {
            appendThisField.accept(json);
        }
    }

    protected void parseField(String value, StringBuilder json) {
        json.append("\"").append(escapeJson(value)).append("\"");
    }

    protected void parseField(Boolean value, StringBuilder json) {
        json.append(value);
    }

    protected void parseField(Double value, StringBuilder json) {
        json.append(value);
    }

    protected void parseField(Long value, StringBuilder json) {
        json.append(value);
    }

    protected void parseField(Integer value, StringBuilder json) {
        json.append(value);
    }

    protected void parseField(Date value, StringBuilder json) {
        json.append(LocalDate.ofEpochDay(value.getTime()).format(DATE_FORMATTER));
    }

    protected void parseField(LocalDate value, StringBuilder json) {
        json.append(value.format(DATE_FORMATTER));
    }

    protected void parseField(LocalDateTime value, StringBuilder json) {
        json.append(value.format(DATETIME_FORMATTER));
    }

    protected String escapeJson(String input) {
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
