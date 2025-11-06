package com.pvc.backend.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class RouteParam {
    private int index;
    private String name;
    private Class<?> type;
    private boolean isRequired;
}
