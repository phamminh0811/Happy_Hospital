package classes.simplified_classes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import classes.Agent;
import classes.Position;

public class SAgent {
	public Position startPos;
	public Position endPos;
	public int id;
	public SAgent(Agent agent) {
		this.startPos = agent.startPos;
		this.endPos = agent.endPos;
		this.id = agent.id;
	}
	@JsonCreator
	public SAgent(@JsonProperty("startPos") Position startPos, @JsonProperty("endPos") Position endPos, @JsonProperty("id") int id) {
		this.startPos = startPos;
		this.endPos = endPos;
		this.id = id;
	}
}
