package classes.simplified_classes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import classes.Agv;

public class SAgv {
	public double x;
	public double y;
	public SAgv(Agv agv) {
		this.x = agv.getTranslateX();
		this.y = agv.getTranslateY();
	}
	@JsonCreator
	public SAgv(@JsonProperty("x") double x, @JsonProperty("y") double y) {
		this.x = x;
		this.y = y;
	}
}
