package com.ness.zkworkshop.web.viewmodel;

import org.zkoss.bind.annotation.Init;
import org.zkoss.zul.DefaultTreeModel;
import org.zkoss.zul.TreeModel;
import org.zkoss.zul.TreeNode;

import com.ness.zkworkshop.web.model.PackageData;
import com.ness.zkworkshop.web.util.PackageDataUtil;

public class TreeViewModel {

	private TreeModel<TreeNode<PackageData>> treeModel;
	
	@Init
	public void init() {
		this.treeModel = new DefaultTreeModel<>(PackageDataUtil.getRoot());
	}

	public TreeModel<TreeNode<PackageData>> getTreeModel() {
		return treeModel;
	}
	
}
