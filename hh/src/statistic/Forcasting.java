package statistic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import config.Constant;
import config.Performance;

public class Forcasting {
	public Map<Integer, Set<WaitingDuration>> waitingAutoAgv;
	private Set<Integer> doneAutoAgv;
	public double averageAverageWaitingTime = 0;

	public Forcasting() {
		this.waitingAutoAgv = new HashMap<Integer, Set<WaitingDuration>>();
		this.doneAutoAgv = new HashSet<Integer>();
	}

	public void rememberDoneAutoAgv(int id) {
		if (this.doneAutoAgv == null) {
			this.doneAutoAgv = new HashSet<Integer>();
		}
		if (!this.doneAutoAgv.contains(id)) {
			this.doneAutoAgv.add(id);
		}
	}

	public void removeAutoAgv(int id) {
		if (this.waitingAutoAgv == null) {
			return;
		}
		if (this.waitingAutoAgv.size() == 0) {
			return;
		}
		if (this.waitingAutoAgv.containsKey(id)) {
			this.waitingAutoAgv.remove(id);
		}
	}

	public void removeDuration(int id) {
		if (this.waitingAutoAgv == null) {
			return;
		}
		if (this.waitingAutoAgv.containsKey(id)) {
			double now = Math.floor(Performance.now() / 1000);
			Stack<WaitingDuration> arr = new Stack<WaitingDuration>();
			if (this.waitingAutoAgv.get(id) != null) {
				for (WaitingDuration item : waitingAutoAgv.get(id)) {
					if (item.end != -1 && item.end < now - Constant.DELTA_T()) {
						arr.push(item);
					}
				}
			}
			for (WaitingDuration item : arr) {
				if (this.waitingAutoAgv.get(id) != null)
					this.waitingAutoAgv.get(id).remove(item);
			}
			if (this.waitingAutoAgv.get(id).size() == 0) {

				if (this.doneAutoAgv.contains(id)) {

					this.waitingAutoAgv.remove(id);
					this.doneAutoAgv.remove(id);
				}
			}
			arr = new Stack<>();
		}
	}

	public void addDuration(int id, WaitingDuration duration) {
		if (this.waitingAutoAgv == null) {
			this.waitingAutoAgv = new HashMap<Integer, Set<WaitingDuration>>();
		}
		if (!this.waitingAutoAgv.containsKey(id)) {
			this.waitingAutoAgv.remove(id);
			this.waitingAutoAgv.put(id, new HashSet<WaitingDuration>());
		}
		Set<WaitingDuration> m = this.waitingAutoAgv.get(id);
		m.add(duration);
		this.waitingAutoAgv.remove(id);
		this.waitingAutoAgv.put(id, m);
	}

	public void updateDuration(int id, int begin, int end) {
		if (this.waitingAutoAgv == null) {
			return;
		}
		if (this.waitingAutoAgv.get(id) != null) {
			for (WaitingDuration item : this.waitingAutoAgv.get(id)) {
				if (item.begin == begin) {
					item.end = end;
					item.duration = item.end - item.begin;
				}
			}
		}
	}

	public double totalAverageWaitingTime() {
		double result = 0;
		if (this.waitingAutoAgv == null) {
			this.waitingAutoAgv = new HashMap<Integer, Set<WaitingDuration>>();
			return 0;
		}
		if (this.waitingAutoAgv.size() == 0) {
			return 0;
		}

		double now = Math.floor(Performance.now() / 1000);
		for (Map.Entry<Integer, Set<WaitingDuration>> entry : this.waitingAutoAgv.entrySet()) {
			Set<WaitingDuration> value = entry.getValue();
			int key = entry.getKey();
			double average = 0;
			int count = 0;
			this.removeDuration(key);
			for (WaitingDuration item : value) {
				count++;
				if (item.end == -1) {
					average += now - item.begin;
				} else {
					average += item.duration;
				}
			}
			if (count == 0) {
				average = 0;
			} else {
				average = average / count;
			}
			result += average;
		}
		result = Math.floor(result * 100) / 100;
		return result;
	}
	public void calculate() {
        double total = this.totalAverageWaitingTime();
        int numAutoAgv = this.waitingAutoAgv.size();
        //let result : number = 0;
        if(numAutoAgv != 0) this.averageAverageWaitingTime = total/numAutoAgv;
        this.averageAverageWaitingTime = Math.floor(this.averageAverageWaitingTime*100)/100;
    }
}
