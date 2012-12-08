/**
 * 
 */
package se.illuminati.rssiminer;

/**
 * @author Vilhelm von Ehrenheim
 * @date	2012-01-17
 *	Data object for RSSI data
 */
public class DataPoint {
	int 	level;
	String 	BSSID;
	String 	SSID;
	long	time;
	
	
	public DataPoint(long time, int level, String BSSID, String SSID){
		this.level = level;
		this.time = time;
		this.BSSID = BSSID;
		this.SSID = SSID;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("TIME: ").append(time).append("\n");
		sb.append("SSID: ").append(SSID).append("\n");
		sb.append("BSSID: ").append(BSSID).append("\n");
		sb.append("RSSI: ").append(level).append(" [dBm]\n\n");
		
		return sb.toString();
	}
	
	public String toCSVString() {
		StringBuilder sb = new StringBuilder();
		sb.append(time).append(",");
		sb.append(SSID).append(",");
		sb.append(BSSID).append(",");
		sb.append(level).append("\n");
		
		return sb.toString();	
	}
}
