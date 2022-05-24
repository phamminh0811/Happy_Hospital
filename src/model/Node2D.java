package model;

import config.Constant;

public class Node2D {
	final double lambda = 0.4;
	public double x, y;
	public Node2D nodeN, nodeS, nodeW, nodeE;
	public double w_edge_N = 1 << 53 - 1;
	public double w_edge_S = 1 << 53 - 1;
	public double w_edge_W = 1 << 53 - 1;
	public double w_edge_E = 1 << 53 - 1;

	private double w = 0;
	private double u = 0;

	public StateOfNode2D state;
	public double p_random, t_min, t_max;

	public Node2D nodeVN, nodeVS, nodeVW, nodeVE;
	public double w_edge_VN = 1 << 53 - 1;
	public double w_edge_VS = 1 << 53 - 1;
	public double w_edge_VW = 1 << 53 - 1;
	public double w_edge_VE = 1 << 53 - 1;

	public boolean isVirtualNode = false;
	private double _weight = 0;

	public Node2D(double x, double y, boolean isVirtualNode, StateOfNode2D state, double p_random, double t_min,
			double t_max) {
		this.x = x;
		this.y = y;
		this.state = state;
		this.p_random = p_random;
		this.t_min = t_min;
		this.t_max = t_max;
		this.isVirtualNode = isVirtualNode;
	}
	public Node2D(double x, double y) {
		this(x, y, false, StateOfNode2D.NOT_ALLOW, 0.05, 2000, 3000);
	}
	public double getW() {
		if (Constant.MODE() == Constant.ModeOfPathPlanning.FRANSEN)
			return this.w;
		return this._weight;
	}

	public double getWeight() {
		return this._weight;
	}

	public void setWeight(double value) {
		this._weight = value;
	}

	public void setNeighbor(Node2D node) {
		if (this.x + 1 == node.x && this.y == node.y) {
			this.nodeE = node;
			this.w_edge_E = 1;
		} else if (this.x == node.x && this.y + 1 == node.y) {
			this.nodeS = node;
			this.w_edge_S = 1;
		} else if (this.x - 1 == node.x && this.y == node.y) {
			this.nodeW = node;
			this.w_edge_W = 1;
		} else if (this.x == node.x && this.y - 1 == node.y) {
			this.nodeN = node;
			this.w_edge_N = 1;
		}
	}

	public void setState(StateOfNode2D state) {
		this.state = state;
	}

	public boolean equal(Node2D node) {
		if (node.isVirtualNode != this.isVirtualNode)
			return false;
		return this.x == node.x && this.y == node.y;
	}

	public boolean madeOf(Node2D node) {
		return this.equal(node);
	}

	public void setU(double u) {
		this.u = Math.floor(u);
		this.updateW();
	}

	public void updateW() {
		this.w = (1 - lambda) * this.w + lambda * this.u;
	}
}
