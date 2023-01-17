package org.nemirovsky.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Data {
    @JsonProperty("Method")
    private Method method;
    @JsonProperty("Process")
    private Process process;
    @JsonProperty("Layer")
    private String layer;
    @JsonProperty("Creation")
    private Creation creation;
    @JsonProperty("Type")
    private String type;
}

