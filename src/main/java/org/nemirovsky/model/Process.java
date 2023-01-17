package org.nemirovsky.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Process {
    @JsonProperty("Name")
    public String name;
    @JsonProperty("Id")
    public long id;
    @JsonProperty("Start")
    public Start start;
}
