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
		root.depth = 0;
	}
	
	public void removeNode(T rootData) {
		Node<T> node = this.getNodeInstance();
		removeNodeR(rootData, node);
	}
	
	private void removeNodeR(T rootData, Node<T> node) {
		if(node.getData().equals(rootData)) {
			if(node.getParent() == null) {
				return;
			}else {
				node.getParent().getChildren().remove(node);
				return;
			}
		}else {
			for(Node<T> n : node.getChildren()) {
				removeNodeR(rootData, n);
			}
		}
	}
	
	public Node<T> findNode(T rootData) {
		Node<T> node = this.getNodeInstance();
		return findNodeR(rootData, node);
	}
	
	private Node<T> findNodeR(T rootData, Node<T> node) {
		System.out.println(rootData);
		if(node.getData().equals(rootData)) {
			return node;
		}else {
			for(Node<T> n : node.getChildren()) {
				Node<T> found = findNodeR(rootData, n);
				if(found == null) {
					return null;
				}
				return found;
			}
			return null;
		}
	}

	public static class Node<T> {
		private T data;
		private Node<T> parent;
		private TemplarBlock blockType;
		private List<Node<T>> children;
		private int depth;
		
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
		
		public int getDepth() {
			return this.depth;
		}
		
		public void addChild(T rootData, TemplarBlock blockType) {
			Node<T> temp = new Node<T>();
			temp.data = rootData;
			temp.children = new ArrayList<Node<T>>();
			temp.blockType = blockType;
			temp.parent = this;
			this.children.add(temp);
			temp.depth = this.depth + 1;
		}
		
		public void removeChild(Node<T> node){
			if(this.children.contains(node)){
				this.children.remove(node);
			}
		}
	}
	
	public Node<T> getNodeInstance(){
		return root;
	}
}
