package no.nrpa.plugin.sort;

public class HeaderLine {
	private String key;
	private String description;
	private String value;

	public HeaderLine(String line) {
		this.key = null;
		this.description = null;
		this.value = null;
		this.key = line.substring(0, 9).trim();
		int lastSeparatorIndex = line.lastIndexOf(":");
		this.description = line.substring(10, lastSeparatorIndex).trim();
		this.value = line.substring(lastSeparatorIndex + 1).trim();
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		if (this.value == null) {
			return "";
		}
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static void main(String[] args1) {
	}
}
