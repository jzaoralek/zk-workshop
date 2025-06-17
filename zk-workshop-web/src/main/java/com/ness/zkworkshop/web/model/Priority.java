package com.ness.zkworkshop.web.model;

/**
 * Enum for Todo
 */
public enum Priority {
	HIGH(0, "Vysoká"), MEDIUM(1, "Střední"), LOW(2, "Nízká");

	private int priority;
	private String label;

	private Priority(int priority, String label) {
		this.priority = priority;
		this.label = label;
	}

	public int getPriority() {
		return priority;
	}

	public String getLabel() {
		return label;
	}
}