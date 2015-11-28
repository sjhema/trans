package com.primovision.lutransport.model.menu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.primovision.lutransport.model.BaseModel;
import com.primovision.lutransport.model.BusinessObject;

/**
 * Helper class for menu related operations
 * 
 * @author rakesh
 * 
 */
public class MenuHelper {

	public static MenuTree<BusinessObject> getMenuTree(
			List<BusinessObject> businessObjects) {

		Node<BusinessObject> rootElement = new Node<BusinessObject>();
		MenuTree<BusinessObject> menuTree = new MenuTree<BusinessObject>();
		menuTree.setRootElement(rootElement);
		Node<BusinessObject> menuItemNode = null;
		Map<String, Node<BusinessObject>> parentNodes = new HashMap<String, Node<BusinessObject>>();
		try {
			int count = 0;
			for (BusinessObject businessObject : businessObjects) {
				if (count == 0)
					menuItemNode = rootElement;
				else
					menuItemNode = new Node<BusinessObject>();
				long menuId = businessObject.getId();
				menuItemNode.setData(businessObject);
				parentNodes.put("" + menuId, menuItemNode);
				if (count != 0) {
					Node<BusinessObject> parentNode = parentNodes.get(""
							+ businessObject.getParent().getId());
					parentNode.addChild(menuItemNode);
				}
				count++;
			}
			parentNodes.clear();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return menuTree;
	}

	/**
	 * Method to find all the menus at the specified level from the menu tree
	 * 
	 * @param menuTree
	 * @param level
	 * @return
	 */
	public static List<BusinessObject> getMenuForLevel(MenuTree menuTree,
			int level) {
		List<BusinessObject> menuItemList = new ArrayList<BusinessObject>();
		if (menuTree != null) {
			List<Node<BaseModel>> allMenus = menuTree.toList();
			BusinessObject menuItem = null;
			for (int i = 0; i < allMenus.size(); i++) {
				menuItem = (BusinessObject) allMenus.get(i).getData();
				if (menuItem != null && menuItem.getObjectLevel() == level)
					menuItemList.add(menuItem);
			}
		}
		Collections.sort(menuItemList);
		return menuItemList;
	}

	public static Node<BusinessObject> getParentNode(
			MenuTree<BusinessObject> menuTree, long parentId) {
		if (menuTree != null) {
			Node<BusinessObject> rootNode = menuTree.getRootElement();
			if (rootNode.getData().getId().longValue() == parentId) {
				return rootNode;
			} else {
				return traverseTree(rootNode, parentId);
			}
		}
		return null;
	}

	private static Node<BusinessObject> traverseTree(
			Node<BusinessObject> rootNode, Long parentId) {

		if (rootNode.getChildren() != null) {
			Node<BusinessObject> tempNode = null;
			for (Node<BusinessObject> node : rootNode.getChildren()) {
				if (node.getData().getId().longValue() == parentId.longValue()) {
					return node;
				} else {
					tempNode = traverseTree(node, parentId);
					if (tempNode != null)
						return tempNode;
				}
			}
		}
		return null;
	}

	/**
	 * Method to find all the menus at the specified level from the menu tree
	 * 
	 * @param menuTree
	 * @param level
	 * @return
	 */
	public static List<BusinessObject> getMenuUpToLevel(MenuTree menuTree,
			int level) {
		List<BusinessObject> menuItemList = new ArrayList<BusinessObject>();
		if (menuTree != null) {
			List<Node<BaseModel>> allMenus = menuTree.toList();
			BusinessObject menuItem = null;
			for (int i = 0; i < allMenus.size(); i++) {
				menuItem = (BusinessObject) allMenus.get(i).getData();
				if (menuItem != null && menuItem.getObjectLevel() < level)
					menuItemList.add(menuItem);
			}
		}
		Collections.sort(menuItemList);
		return menuItemList;
	}

	/**
	 * Method to find all the menus at the specified level from the menu tree
	 * 
	 * @param menuTree
	 * @param level
	 * @return
	 */
	public static List<BusinessObject> getMenuForParent(MenuTree menuTree,
			long parent) {
		List<BusinessObject> menuItemList = new ArrayList<BusinessObject>();
		List<Node<BaseModel>> allMenus = menuTree.toList();
		BusinessObject menuItem = null;
		for (int i = 1; i < allMenus.size(); i++) {
			menuItem = (BusinessObject) allMenus.get(i).getData();
			if (menuItem != null && menuItem.getParent().getId() == parent) {
				menuItemList.add(menuItem);
			}
		}
		Collections.sort(menuItemList);
		return menuItemList;
	}

	/**
	 * Method to get menuItem for the businessObject
	 * 
	 * @param menuTree
	 * @param level
	 * @return
	 */
	public static BusinessObject getMenuNode(MenuTree menuTree, int menuObject) {
		List<Node<BaseModel>> allMenus = menuTree.toList();
		BusinessObject menuItem = null;
		for (int i = 0; i < allMenus.size(); i++) {
			menuItem = (BusinessObject) allMenus.get(i).getData();
			if (menuItem != null && menuItem.getId() == menuObject) {
				return menuItem;
			}
		}
		return null;
	}

	/**
	 * Method to check if a particular menu item is seleted or not given the
	 * object id being accessed
	 * 
	 * @param menuTree
	 * @param objId
	 * @return
	 */
	public static boolean isSelected(MenuTree menuTree, int checkObjId,
			int selectedObjId) {
		List<Node<BaseModel>> allMenus = menuTree.toList();
		BusinessObject menuItem = null;
		String hierarchyElement = "/" + checkObjId + "/";
		for (int i = 0; i < allMenus.size(); i++) {
			menuItem = (BusinessObject) allMenus.get(i).getData();
			if (menuItem != null && menuItem.getId() == selectedObjId) {
				break;
			}
		}
		if (menuItem != null
				&& menuItem.getObjectHierarchy().indexOf(hierarchyElement) != -1)
			return true;
		else
			return false;
	}
}
