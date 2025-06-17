package zksandbox.model.tree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/** Uzel hierarchie.
 *
 * @param <T_ELM>
 * @see Hier
 */
public class HierNode<T_ELM> implements Serializable {

	private static final long serialVersionUID = 1L;


	private int level;

	private int ord;

	private T_ELM value;

	private HierNode<T_ELM> parent;

	private List<HierNode<T_ELM>> children;

	public HierNode(int level, int ord, T_ELM value, HierNode<T_ELM> parent) {
		this.level = level;
		this.ord = ord;
		this.value = value;
		this.parent = parent;
	}

	/**
	 * Hlada vsetky listy v hierarchii
	 * @param ogn
	 * @param found
	 * @return
	 */
	public List<T_ELM> findLeafValues(HierNode<T_ELM> ogn, List<T_ELM> found) {

		if (ogn.isLeaf()) {
			found.add(ogn.getValue());
			return found;
		}

		for (HierNode<T_ELM> node : ogn.getChildren()) {
			found = findLeafValues(node, found);
		}

		return found.isEmpty() ? null : found;
	}

	public boolean isLeaf() {
		return (children == null || children.isEmpty());
	}

	public boolean isRoot() {
		return (parent == null);
	}

	public void addAfterXbrl(HierNode<T_ELM> hn) {
		if (children == null) {
			children = new ArrayList<>();
		}
		children.add(hn);
	}

	public void moveNodeXbrl() {
		this.level++;
		if (children == null) {
			return;
		}
		for (HierNode<T_ELM> node : children) {
			node.moveNodeXbrl();
		}
	}

	public void cleanOrd(int i) {
		this.ord = i;
		if (children == null) {
			return;
		}
		int j = 0;
		for (HierNode<T_ELM> node : children) {
			i++;
			j++;
			node.cleanOrd(j);
		}
	}


	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getOrd() {
		return ord;
	}

	public void setOrd(int ord) {
		this.ord = ord;
	}

	public T_ELM getValue() {
		return value;
	}

	public void setValue(T_ELM value) {
		this.value = value;
	}

	public HierNode<T_ELM> getParent() {
		return parent;
	}

	public void setParent(HierNode<T_ELM> parent) {
		this.parent = parent;
	}

	public List<HierNode<T_ELM>> getChildren() {
		return children;
	}

	public void setChildren(List<HierNode<T_ELM>> children) {
		this.children = children;
	}


	@Override
	public String toString() {
		return "HierNode [level=" + level + ", ord=" + ord + ", value=" + value
				+ ", childrenCnt=" + (children == null ? 0 : children.size())
				+ ", parent=" + parent;
	}

}
