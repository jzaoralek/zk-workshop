package com.ness.zkworkshop.web.viewmodel;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.DependsOn;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zul.DefaultTreeModel;
import org.zkoss.zul.DefaultTreeNode;
import org.zkoss.zul.TreeModel;
import org.zkoss.zul.TreeNode;

import com.ness.zkworkshop.web.model.PackageData;
import com.ness.zkworkshop.web.util.PackageDataUtil;

public class TreeViewModel {
	
	private enum TreeListboxMode {
		TREE,
		LISTBOX;
	}

	private TreeModel<TreeNode<PackageData>> treeModel;
	private List<PackageData> treeList;
	private TreeListboxMode mode;

	@Init
	public void init() {
		this.treeModel = new DefaultTreeModel<>(PackageDataUtil.getRoot());
		this.treeList = zkTreeTolist(PackageDataUtil.getRoot());
		this.mode = TreeListboxMode.TREE;
	}
	
	@NotifyChange("mode")
	@Command
	public void switchToTreeModeCmd() {
		this.mode = TreeListboxMode.TREE;
	}
	
	@NotifyChange("mode")
	@Command
	public void switchToListboxModeCmd() {
		this.mode = TreeListboxMode.LISTBOX;
	}
	
	@NotifyChange("treeModel")
	@Command
	public void treeModelReloadCmd() {
		this.treeModel = new DefaultTreeModel<>(PackageDataUtil.getRoot());
	}
	
	@DependsOn("mode")
	public boolean isTreeMode() {
		return this.mode == TreeListboxMode.TREE;
	}
	
	@DependsOn("mode")
	public boolean isListboxMode() {
		return this.mode == TreeListboxMode.LISTBOX;
	}

	public TreeModel<TreeNode<PackageData>> getTreeModel() {
		return treeModel;
	}
	
	public List<PackageData> getTreeList() {
		return treeList;
	}
	
	public TreeListboxMode getMode() {
		return mode;
	}
	
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
	
}
