package com.bart.example.model;

import com.bart.example.infrastructure.json.annotations.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@JsonObject
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String name;
    private Integer age;
}
