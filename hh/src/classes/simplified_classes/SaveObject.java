package classes.simplified_classes;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SaveObject {
	public SAgv agv;
	public ArrayList<SAgent> agents;
	@JsonCreator
	public SaveObject(@JsonProperty("agv") SAgv agv, @JsonProperty("agents") ArrayList<SAgent> agents) {
		this.agv = agv;
		this.agents = agents;
	}
}
