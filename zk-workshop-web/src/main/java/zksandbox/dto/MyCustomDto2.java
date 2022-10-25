package zksandbox.dto;

import java.util.ArrayList;
import java.util.List;

public class MyCustomDto2 {

	private List<String> columsData;

	public MyCustomDto2(int columsDataSize) {
		columsData = new ArrayList<>(columsDataSize);
		for (int i = 0; i < columsDataSize; i++) {
			columsData.add(null);
		}
	}

	public void setColumnData(int colIndex, String value) {
		columsData.set(colIndex, value);
	}

	public String getColumnData(int colIndex) {
		return columsData.get(colIndex);
	}

}
