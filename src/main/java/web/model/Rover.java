package web.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
public class Rover implements Serializable {

    private int id;

    private String name;

    @JsonProperty(value = "landing_date")
    private Date landingDate;

    @JsonProperty(value = "launch_date")
    private Date launchDate;

    private String status;
}
