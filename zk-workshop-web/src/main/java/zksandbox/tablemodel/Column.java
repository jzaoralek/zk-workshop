package zksandbox.tablemodel;

public class Column {

	private String label;
	private String width;
	private String hflex;

	public Column() {
	}

	public Column(String label, String width, String hflex) {
		this.label = label;
		this.width = width;
		this.hflex = hflex;
	}

	/**
	 * 
	 * @param label
	 * @param width
	 * @return
	 */
	public static Column fixColumn(String label, String width) {
		return new Column(label, width, null);
	}

	/**
	 * 
	 * @param label
	 * @param hflex
	 * @return
	 */
	public static Column hflexColumn(String label, String hflex) {
		return new Column(label, null, hflex);
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHflex() {
		return hflex;
	}

	public void setHflex(String hflex) {
		this.hflex = hflex;
	}

}
