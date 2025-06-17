package zksandbox.model.tree;

public class StringTreeElement implements TreeElement {

	private long id;
	private boolean moved;

	@Override
	public void setMoved(boolean moved) {
		this.moved = moved;
	}

	@Override
	public boolean isMoved() {
		return moved;
	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public void setId(long id) {
		this.id = id;
	}
}