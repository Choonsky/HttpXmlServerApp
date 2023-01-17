package org.nemirovsky.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.ZonedDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Creation {
    @JsonProperty("Epoch")
    private long epoch;
    @JsonProperty("Date")
    ZonedDateTime date;
}
