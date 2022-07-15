package classes;

import static config.Constant.*;

import java.io.Serializable;

import config.Constant;

public class Node2D implements Serializable{
	private static final long serialVersionUID = 1L;
	final double lambda = 0.4;
	public int x;
	public int y;
	public Node2D nodeW = null;
	public Node2D nodeN = null;
	public Node2D nodeS = null;
	public Node2D nodeE = null;
	public int w_edge_W = Integer.MAX_VALUE;
	public int w_edge_N = Integer.MAX_VALUE;
	public int w_edge_S = Integer.MAX_VALUE;
	public int w_edge_E = Integer.MAX_VALUE;
	private double w = 0;
	private int u = 0;
	public StateOfNode2D state;
	public double p_random;
	public int t_min;
	public int t_max;

	public Node2D nodeVW = null;
	public Node2D nodeVN = null;
	public Node2D nodeVS = null;
	public Node2D nodeVE = null;

	public int w_edge_VW = Integer.MAX_VALUE;
	public int w_edge_VN = Integer.MAX_VALUE;
	public int w_edge_VS = Integer.MAX_VALUE;
	public int w_edge_VE = Integer.MAX_VALUE;

	public boolean isVirtualNode = false;
	private double _weight = 0;

	public Node2D(int x, int y, boolean isVirtualNode, StateOfNode2D state, double p_random, int t_min, int t_max) {
		this.x = x;
		this.y = y;
		this.state = state;
		this.p_random = p_random;
		this.t_min = t_min;
		this.t_max = t_max;
		this.isVirtualNode = isVirtualNode;
	}

	public Node2D(int x, int y) {
		this.x = x;
		this.y = y;
		this.isVirtualNode = false;
		this.state = StateOfNode2D.NOT_ALLOW;
		this.p_random = 0.05;
		this.t_min = 2000;
		this.t_max = 3000;
	}

	public Node2D(int x, int y, boolean isVirtualNode) {
		this.x = x;
		this.y = y;
		this.isVirtualNode = isVirtualNode;
		this.state = StateOfNode2D.NOT_ALLOW;
		this.p_random = 0.05;
		this.t_min = 2000;
		this.t_max = 3000;
	}

	public double getW() {
		if (Constant.MODE() == ModeOfPathPlanning.FRANSEN)
			return this.w;
		else
			return this._weight;
	}

	public double getWeight() {
		return this._weight;
	}

	public void setWeight(double value) {
		this._weight = value;
	}

	public void setNeighbor(Node2D node) {
		if (node == null)
			return;
		if (node.isVirtualNode) {
			if (this.x + 1 == node.x && this.y == node.y) {
				this.nodeVE = node;
				this.w_edge_VE = 1;
			} else if (this.x == node.x && this.y + 1 == node.y) {
				this.nodeVS = node;
				this.w_edge_VS = 1;
			} else if (this.x - 1 == node.x && this.y == node.y) {
				this.nodeVW = node;
				this.w_edge_VW = 1;
			} else if (this.x == node.x && this.y - 1 == node.y) {
				this.nodeVN = node;
				this.w_edge_VN = 1;
			}
			return;
		}
		this.setRealNeighbor(node);
	}

	private void setRealNeighbor(Node2D node) {
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
		this.u = (int) Math.floor(u);
		this.updateW();
	}

	public void updateW() {
		this.w = (1 - lambda) * this.w + lambda * this.u;
	}

	@Override
	public String toString() {
		return "Node2D [lambda=" + lambda + ", x=" + x + ", y=" + y + ", nodeW=" + nodeW + ", nodeN=" + nodeN
				+ ", nodeS=" + nodeS + ", nodeE=" + nodeE + ", w_edge_W=" + w_edge_W + ", w_edge_N=" + w_edge_N
				+ ", w_edge_S=" + w_edge_S + ", w_edge_E=" + w_edge_E + ", w=" + w + ", u=" + u + ", state=" + state
				+ ", p_random=" + p_random + ", t_min=" + t_min + ", t_max=" + t_max + ", nodeVW=" + nodeVW
				+ ", nodeVN=" + nodeVN + ", nodeVS=" + nodeVS + ", nodeVE=" + nodeVE + ", w_edge_VW=" + w_edge_VW
				+ ", w_edge_VN=" + w_edge_VN + ", w_edge_VS=" + w_edge_VS + ", w_edge_VE=" + w_edge_VE
				+ ", isVirtualNode=" + isVirtualNode + ", _weight=" + _weight + "]";
	}

}
