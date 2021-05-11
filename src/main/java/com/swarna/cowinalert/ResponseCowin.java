
package com.swarna.cowinalert;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "centers"
})
@Generated("jsonschema2pojo")
public class ResponseCowin {

    @JsonProperty("centers")
    private List<Center> centers = null;

    /**
     * No args constructor for use in serialization
     * 
     */
    public ResponseCowin() {
    }

    /**
     * 
     * @param centers
     */
    public ResponseCowin(List<Center> centers) {
        super();
        this.centers = centers;
    }

    @JsonProperty("centers")
    public List<Center> getCenters() {
        return centers;
    }

    @JsonProperty("centers")
    public void setCenters(List<Center> centers) {
        this.centers = centers;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(ResponseCowin.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("centers");
        sb.append('=');
        sb.append(((this.centers == null)?"<null>":this.centers));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
