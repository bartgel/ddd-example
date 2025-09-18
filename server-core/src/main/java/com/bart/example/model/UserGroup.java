package com.bart.example.model;

import com.bart.example.infrastructure.json.annotations.JsonObject;
import lombok.Data;

@Data
@JsonObject
public class UserGroup {
    private String usergroup;
    private Long numberOfUsers;
}
