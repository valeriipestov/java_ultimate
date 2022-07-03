package web.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
public class Photo implements Serializable {

    private int id;

    private int sol;

    private Camera camera;

    @JsonProperty(value = "img_src")
    private String imageSource;

    @JsonProperty(value = "earth_date")
    private Date earthDate;

    private Rover rover;
}
