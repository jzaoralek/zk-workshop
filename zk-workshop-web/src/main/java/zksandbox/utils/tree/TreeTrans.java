package zksandbox.utils.tree;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zul.DefaultTreeNode;
import org.zkoss.zul.TreeNode;

import zksandbox.model.tree.HierNode;


public final class TreeTrans {

	private TreeTrans() {}


	/** Prevadi ZK treee strukturu na list dle pruchodu do hlouky.
	 *
	 * @param zkRoot
	 * @param hiStruct
	 * @return
	 */
	public static <T_ELM> List<T_ELM> zkTreeTolist(DefaultTreeNode<T_ELM> zkRoot) {
		if (zkRoot == null) {
			throw new IllegalArgumentException("zkRoot is null");
		}
		List<T_ELM> ret = new ArrayList<T_ELM>(64);
		zkNodeToList(zkRoot.getChildren(), ret);
		return ret;
	}


	private static <T_ELM> void zkNodeToList(List<TreeNode<T_ELM>> zkNodes, List<T_ELM> ret) {
		if (zkNodes == null || zkNodes.isEmpty()) {
			return;
		}
		for (TreeNode<T_ELM> zkNode: zkNodes) {
			ret.add(zkNode.getData());
			if (!zkNode.isLeaf()) {
				zkNodeToList(zkNode.getChildren(), ret);
			}
		}
	}


	private static <T_ELM> List<HierNode<T_ELM>> transZkNode(int level,
																HierNode<T_ELM> parent,
																List<TreeNode<T_ELM>> zkNodes) {
		if (zkNodes == null || zkNodes.isEmpty()) {
			return null;
		}
		List<HierNode<T_ELM>> ret = new ArrayList<HierNode<T_ELM>>(zkNodes.size());
		int ord = 1;
		for (TreeNode<T_ELM> zkNode: zkNodes) {
			HierNode<T_ELM> hn = new HierNode<T_ELM>(level, ord, zkNode.getData(), parent);
			ret.add(hn);
			if (!zkNode.isLeaf()) {
				hn.setChildren(transZkNode(level + 1, hn, zkNode.getChildren()));
			}
			ord++;
		}
		return ret;
	}

}
