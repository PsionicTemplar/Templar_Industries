package me.PsionicTemplar.templarIndustries.Util;

import java.util.ArrayList;
import java.util.List;

import me.PsionicTemplar.templarIndustries.Blocks.TemplarBlock;

public class TemplarTree<T> {
	private Node<T> root;

	public TemplarTree(T rootData, TemplarBlock blockType) {
		root = new Node<T>();
		root.data = rootData;
		root.children = new ArrayList<Node<T>>();
		root.blockType = blockType;
	}

	public static class Node<T> {
		private T data;
		private Node<T> parent;
		private TemplarBlock blockType;
		private List<Node<T>> children;
		
		public List<Node<T>> getChildren(){
			return this.children;
		}

		public T getData() {
			return data;
		}

		public Node<T> getParent() {
			return parent;
		}

		public TemplarBlock getBlockType() {
			return blockType;
		}
		
		public void addChild(T rootData, TemplarBlock blockType) {
			Node<T> temp = new Node<T>();
			temp.data = rootData;
			temp.children = new ArrayList<Node<T>>();
			temp.blockType = blockType;
			temp.parent = this;
			this.children.add(temp);
		}
	}
	
	public Node<T> getNodeInstance(){
		return root;
	}
}
