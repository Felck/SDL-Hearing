
public class Waypoint {
	public int x, y;
	public int priority; // -1 for no priority level
	public int minTime, maxTime;
	
	public Waypoint(int x, int y, int priority, int minTime, int maxTime) {
		this.x = x;
		this.y = y;
		this.priority = priority;
		this.minTime = minTime;
		this.maxTime = maxTime;
	}
	
	public Waypoint(int x, int y, int priority) {
		this(x, y, priority, -1, -1);
	}

	public Waypoint(int x, int y) {
		this(x, y, -1, -1, -1);
	}
	
	public String toString() {
		return "("+x+","+y+")";
	}
}
