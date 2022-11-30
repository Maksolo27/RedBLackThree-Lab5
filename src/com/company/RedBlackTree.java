package com.company;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class RedBlackTree<T extends Comparable<T>> {

    enum NodeColor {
        RED,
        BLACK,
        NONE
    }

    public class Node {


        private T value;

        private NodeColor color;

        private Node parent;

        private Node left;

        private Node right;

        public Node() {
            value = null;
            color = NodeColor.NONE;
            parent = null;
            left = null;
            right = null;
        }

        public Node(T value, NodeColor color) {
            this.value = value;
            this.color = color;
            parent = nil;
            left = nil;
            right = nil;
        }
        public Node(Node node) {
            value = node.value;
            color = node.color;
            parent = node.parent;
            left = node.left;
            right = node.right;
        }

        public boolean isFree() {
            return value == null || value == nil;
        }

        public boolean isLeftFree() {
            return left == null || left == nil;
        }

        public boolean isRightFree() {
            return right == null || right == nil;
        }

        public boolean isParentFree() {
            return parent == null || parent == nil;
        }

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }

        public Node getParent() {
            return parent;
        }

        public void setParent(Node node) {
            parent = node;
        }

        public Node getLeft() {
            return left;
        }

        public void setLeft(Node node) {
            left = node;
            if(left != null && left != nil) left.parent = this;
        }

        public Node getRight() {
            return right;
        }

        public void setRight(Node node) {
            right = node;
            if(right != null && right != nil) right.parent = this;
        }

        public boolean isBlack() {
            return color == NodeColor.BLACK;
        }

        public void makeBlack() {
            color = NodeColor.BLACK;
        }

        public boolean isRed() {
            return color == NodeColor.RED;
        }

        public void makeRed() {
            color = NodeColor.RED;
        }

        public NodeColor getColor() {
            return color;
        }

        public void setColor(NodeColor color) {
            this.color = color;
        }

        public Node getGrandfather() {
            if(parent != null && parent != nil)
                return parent.parent;
            return null;
        }


        public Node getUncle() {
            Node grand = getGrandfather();
            if(grand != null)
            {
                if(grand.left == parent)
                    return grand.right;
                else if(grand.right == parent)
                    return grand.left;
            }
            return null;
        }

        public Node getSuccessor()
        {
            Node temp = null;
            Node node = this;
            if(!node.isRightFree()) {
                temp = node.getRight();
                while(!temp.isLeftFree())
                    temp = temp.getLeft();
                return temp;
            }
            temp = node.getParent();
            while(temp != nil && node == temp.getRight()) {
                node = temp;
                temp = temp.getParent();
            }
            return temp;
        }

        public String getColorName() {
            return ((isBlack()) ? "B" : "R");
        }

    }


    private Node root;

    private Node nil;

    private Node current;

    private boolean isRemoved;

    public RedBlackTree() {
        root = new Node();
        nil = new Node();
        nil.color = NodeColor.BLACK;
        root.parent = nil;
        root.right = nil;
        root.left = nil;
        isRemoved = false;
    }

    private static <T extends Comparable<T>> void leftRotate(RedBlackTree<T> tree, RedBlackTree<T>.Node node) {
        RedBlackTree<T>.Node nodeParent = node.getParent();
        RedBlackTree<T>.Node nodeRight = node.getRight();
        if(nodeParent != tree.nil) {
            if(nodeParent.getLeft() == node)
                nodeParent.setLeft(nodeRight);
            else
                nodeParent.setRight(nodeRight);
        }
        else {
            tree.root = nodeRight;
            tree.root.setParent(tree.nil);
        }
        node.setRight(nodeRight.getLeft());
        nodeRight.setLeft(node);
    }


    private static <T extends Comparable<T>> void rightRotate(RedBlackTree<T> tree, RedBlackTree<T>.Node node) {
        RedBlackTree<T>.Node nodeParent = node.getParent();
        RedBlackTree<T>.Node nodeLeft = node.getLeft();
        if(nodeParent != tree.nil) {
            if(nodeParent.getLeft() == node)
                nodeParent.setLeft(nodeLeft);
            else
                nodeParent.setRight(nodeLeft);
        }
        else {
            tree.root = nodeLeft;
            tree.root.setParent(tree.nil);
        }
        node.setLeft(nodeLeft.getRight());
        nodeLeft.setRight(node);
    }


    public static <T extends Comparable<T>> void printTree(RedBlackTree<T> tree) {
        ArrayList<RedBlackTree<T>.Node> nodes = new ArrayList<RedBlackTree<T>.Node>();
        nodes.add(0, tree.root);
        printNodes(tree, nodes);
    }


    private static <T extends Comparable<T>> void printNodes(RedBlackTree<T> tree, ArrayList<RedBlackTree<T>.Node> nodes) {
        int childsCounter = 0;
        int nodesAmount = nodes.size();
        ArrayList<RedBlackTree<T>.Node> childs = new ArrayList<RedBlackTree<T>.Node>();

        for(int i = 0; i < nodesAmount; i++) {
            if(nodes.get(i) != null && nodes.get(i) != tree.nil) {
                System.out.print("(" + nodes.get(i).getValue().toString() + "," + nodes.get(i).getColorName() + ")");
                if(!nodes.get(i).isLeftFree()) {
                    childs.add(nodes.get(i).getLeft());
                    childsCounter++;
                }
                else
                    childs.add(null);
                if(!nodes.get(i).isRightFree()) {
                    childs.add(nodes.get(i).getRight());
                    childsCounter++;
                }
                else
                    childs.add(null);
            }
            else {
                System.out.print("(nil)");
            }
        }
        System.out.print("\n");

        if(childsCounter != 0)
            printNodes(tree, childs);
    }


    public void add(T o) {
        Node node = root, temp = nil;
        Node newNode = new Node((T)o, NodeColor.RED);
        while(node != null && node != nil && !node.isFree()) {
            temp = node;
            if(newNode.getValue().compareTo(node.getValue()) < 0)
                node = node.getLeft();
            else
                node = node.getRight();
        }
        newNode.setParent(temp);
        if(temp == nil)
            root.setValue(newNode.getValue());
        else {
            if(newNode.getValue().compareTo(temp.getValue()) < 0)
                temp.setLeft(newNode);
            else
                temp.setRight(newNode);
        }
        newNode.setLeft(nil);
        newNode.setRight(nil);
        fixInsert(newNode);
    }

    private void fixInsert(Node node) {
        Node temp;
        while(!node.isParentFree() && node.getParent().isRed()) {
            if(node.getParent() == node.getGrandfather().getLeft()) {
                temp = node.getGrandfather().getRight();
                if(temp.isRed()) {
                    temp.makeBlack();
                    node.getParent().makeBlack();
                    node.getGrandfather().makeRed();
                    node = node.getGrandfather();
                }
                else {
                    if(node == node.getParent().getRight()) {
                        node = node.getParent();
                        leftRotate(this, node);
                    }
                    node.getParent().makeBlack();
                    node.getGrandfather().makeRed();
                    rightRotate(this, node.getGrandfather());
                }
            }
            else {
                temp = node.getGrandfather().getLeft();
                if(temp.isRed()) {
                    temp.makeBlack();
                    node.getParent().makeBlack();
                    node.getGrandfather().makeRed();
                    node = node.getGrandfather();
                }
                else {
                    if(node == node.getParent().getLeft()) {
                        node = node.getParent();
                        rightRotate(this, node);
                    }
                    node.getParent().makeBlack();
                    node.getGrandfather().makeRed();
                    leftRotate(this, node.getGrandfather());
                }
            }
        }
        root.makeBlack();
    }


    public boolean remove(T o) {
        return remove(findNode(o));
    }


    private boolean remove(Node node)
    {
        Node temp = nil, successor = nil;

        if(node == null || node == nil)
            return false;

        if(node.isLeftFree() || node.isRightFree())
            successor = node;
        else
            successor = node.getSuccessor();

        if(!successor.isLeftFree())
            temp = successor.getLeft();
        else
            temp = successor.getRight();
        temp.setParent(successor.getParent());

        if(successor.isParentFree())
            root = temp;
        else if(successor == successor.getParent().getLeft())
            successor.getParent().setLeft(temp);
        else
            successor.getParent().setRight(temp);

        if(successor != node) {
            node.setValue(successor.getValue());
        }
        if(successor.isBlack())
            fixRemove(temp);
        return true;
    }


    private void fixRemove(Node node)
    {
        Node temp;
        while(node != root && node.isBlack()) {
            if(node == node.getParent().getLeft()) {
                temp = node.getParent().getRight();
                if(temp.isRed()) {
                    temp.makeBlack();
                    node.getParent().makeRed();
                    leftRotate(this, node.getParent());
                    temp = node.getParent().getRight();
                }
                if(temp.getLeft().isBlack() && temp.getRight().isBlack()) {
                    temp.makeRed();
                    node = node.getParent();
                }
                else {
                    if(temp.getRight().isBlack()) {
                        temp.getLeft().makeBlack();
                        temp.makeRed();
                        rightRotate(this, temp);
                        temp = node.getParent().getRight();
                    }
                    temp.setColor(node.getParent().getColor());
                    node.getParent().makeBlack();
                    temp.getRight().makeBlack();
                    leftRotate(this, node.getParent());
                    node = root;
                }
            }
            else {
                temp = node.getParent().getLeft();
                if(temp.isRed()) {
                    temp.makeBlack();
                    node.getParent().makeRed();
                    rightRotate(this, node.getParent());
                    temp = node.getParent().getLeft();
                }
                if(temp.getLeft().isBlack() && temp.getRight().isBlack()) {
                    temp.makeRed();
                    node = node.getParent();
                }
                else {
                    if(temp.getLeft().isBlack()) {
                        temp.getRight().makeBlack();
                        temp.makeRed();
                        leftRotate(this, temp);
                        temp = node.getParent().getLeft();
                    }
                    temp.setColor(node.getParent().getColor());
                    node.getParent().makeBlack();
                    temp.getLeft().makeBlack();
                    rightRotate(this, node.getParent());
                    node = root;
                }
            }
        }
        node.makeBlack();
    }


    public boolean contains(T o) {
        return (findNode(o) != nil );
    }


    private Node findNode(T o) {
        Node node = root;
        while(node != null && node != nil && node.getValue().compareTo(o) != 0) {
            if(node.getValue().compareTo(o) > 0)
                node = node.getLeft();
            else
                node = node.getRight();
        }
        return node;
    }

    private Node first()
    {
        Node node = root;
        while(node.getLeft() != null && node.getLeft() != nil) {
            if(!node.isLeftFree())
                node = node.getLeft();
        }
        return node;
    }


    public Iterator<T> iterator() {
        current = null;
        isRemoved = false;
        return (Iterator<T>) this;
    }

    public boolean hasNext() {
        if(current != null) {
            if(!isRemoved) {
                RedBlackTree<T>.Node node = current.getSuccessor();
                return (node != null && node != nil );
            }
            return ( current != null && current != nil );
        }
        else {
            return ( root != null && !root.isFree());
        }
    }

    public T next() {
        if(current != null) {
            if(!isRemoved)
                current = current.getSuccessor();
            else
                isRemoved = false;
        }
        else {
            current = first();
        }
        if(current == null || current == nil)
            throw new NoSuchElementException();
        return current.getValue();
    }

    public void remove() {
        if(current != null && !isRemoved) {
            remove(current);
            isRemoved = true;
        }
        else
            throw new IllegalStateException();
    }
}