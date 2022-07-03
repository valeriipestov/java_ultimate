package web.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class Camera implements Serializable {
    private int id;

    private String name;

    @JsonProperty(value = "rover_id")
    private int roverId;

    @JsonProperty(value = "full_name")
    private String fullName;
}
