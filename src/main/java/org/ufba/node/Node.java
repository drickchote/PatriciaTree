package org.ufba.node;

import org.ufba.PatriciaTree;
import org.ufba.enums.NodeType;
import org.ufba.enums.SortType;
import org.ufba.enums.WordClass;
import org.ufba.utils.BinaryUtils;
import org.ufba.utils.NodeDiskUtils;

public abstract class Node extends SerializebleEntity{
    private NodeType nodeType;

    Node(NodeType nodeType){
        this.nodeType = nodeType;
    }

    public NodeType getNodeType() {
        return nodeType;
    }

    public void setNodeType(NodeType nodeType) {
        this.nodeType = nodeType;
    }

    public void printWords(SortType sortType, WordClass wordClass, boolean printBinary){
        if(nodeType.equals(NodeType.word)){
            WordNode currentNode = (WordNode) this;
            if(wordClass == null || currentNode.getWordClass().equals(wordClass)){
                System.out.print(currentNode.getWord());
                if(printBinary){
                    System.out.print(" "+ BinaryUtils.toBinaryWithSpaces(currentNode.getWord()));
                }
                System.out.println();
            }
            return;
        }
        BitSeparator currentNode = (BitSeparator) this;

        if(sortType.equals(SortType.ascending)){
            NodeDiskUtils.getNodeByPointer(currentNode.getPointerLeft()).printWords(sortType, wordClass, printBinary);
            NodeDiskUtils.getNodeByPointer(currentNode.getPointerRight()).printWords(sortType, wordClass, printBinary);
        } else {
            NodeDiskUtils.getNodeByPointer(currentNode.getPointerRight()).printWords(sortType, wordClass, printBinary);
            NodeDiskUtils.getNodeByPointer(currentNode.getPointerLeft()).printWords(sortType, wordClass, printBinary);
        }
    }

    public void printTree(){
        if(this.nodeType.equals(NodeType.word)){
            WordNode currentNode =  ((WordNode) this);
            System.out.println(currentNode.getValue());
        } else{
            BitSeparator currentNode =  ((BitSeparator) this);
            Node leftChild = NodeDiskUtils.getNodeByPointer(currentNode.getPointerLeft());
            Node rightChild = NodeDiskUtils.getNodeByPointer(currentNode.getPointerRight());
            System.out.println("bit: "+currentNode.getBit()+" fesq: "+ leftChild.getValue()+" fdir: "+rightChild.getValue());
            leftChild.printTree();
            rightChild.printTree();
        }
    }

    public String getValue(){
        if(this.nodeType.equals(NodeType.word)){
            return ((WordNode) this).getWord();
        } else{
            return String.valueOf(((BitSeparator) this).getBit());
        }
    }

    public WordNode findWord(String word){
        WordNode node = findLeaf(word);
        if(!node.getWord().equals(word)){
            return null;
        }
        return node;
    }

    public WordNode findLeaf(String word){
        if(this.nodeType.equals(NodeType.word)){
            return (WordNode) this;
        }

        BitSeparator currentNode = (BitSeparator) this;

        char bitInWord = BinaryUtils.binaryAt(word, currentNode.getBit());
        if(bitInWord == '0'){
            Node leftChild = NodeDiskUtils.getNodeByPointer(currentNode.getPointerLeft());
            return leftChild.findLeaf(word);
        }

        Node rightChild = NodeDiskUtils.getNodeByPointer(currentNode.getPointerRight());
        return rightChild.findLeaf(word);
    }

    public void updateNodeOnDisk(){
        NodeDiskUtils.updateNodeOnDisk(this, this.getDiskOffset());
    }

    public boolean removeWord(String word, BitSeparator parent, PatriciaTree tree){
        if(this.nodeType.equals(NodeType.word)){ // This node is root
            if(!this.getValue().equals(word)){
                return false;
            }
            tree.updateRoot(null);
            return true;
        }

        BitSeparator currentNode = (BitSeparator) this;


        Node nextChild = null;
        String otherChildPointer = null;

        if(BinaryUtils.binaryAt(word, currentNode.getBit()) == '0'){
            nextChild = NodeDiskUtils.getNodeByPointer(currentNode.getPointerLeft());
            otherChildPointer = currentNode.getPointerRight();
        } else {
            nextChild = NodeDiskUtils.getNodeByPointer(currentNode.getPointerRight());
            otherChildPointer = currentNode.getPointerLeft();
        }

        if(nextChild.getNodeType().equals(NodeType.bitSeparator)){
            return nextChild.removeWord(word, currentNode, tree);
        }

        /** next child is a leaf **/

        if(!nextChild.getValue().equals(word)){
            return false;
        }

        Node otherChild =  NodeDiskUtils.getNodeByPointer(otherChildPointer);
        if(parent == null){ //  if parent equals null, other child becomes root
            tree.updateRoot(otherChild);
            return true;
        }

        if(BinaryUtils.binaryAt(word, parent.getBit()) == '0'){
            parent.setPointerLeft(otherChild.getPointerToDiskCharacters());
        } else {
            parent.setPointerRight(otherChild.getPointerToDiskCharacters());
        }

        parent.updateNodeOnDisk();
        return true;

    }

    public void insertBitSeparator(BitSeparator bitSeparator, BitSeparator parent, PatriciaTree tree, String word){
        if(nodeType.equals(NodeType.word) || ((BitSeparator)this).getBit() > bitSeparator.getBit()){
            bitSeparator.setAnotherPointer(this.getPointerToDiskCharacters());
            NodeDiskUtils.writeNodeOnDisk(bitSeparator);

            if(parent == null){ // this node is root
                tree.updateRoot(bitSeparator);
                return;
            }

            if(BinaryUtils.binaryAt(word, parent.getBit()) == '0'){
                parent.setPointerLeft(bitSeparator.getPointerToDiskCharacters());
            } else {
                parent.setPointerRight(bitSeparator.getPointerToDiskCharacters());
            }

            parent.updateNodeOnDisk();
            return;
        }

        BitSeparator currentNode = (BitSeparator) this;
        Node nextNode;
        if(BinaryUtils.binaryAt(word, currentNode.getBit()) == '0'){
            nextNode = NodeDiskUtils.getNodeByPointer(currentNode.getPointerLeft());
        } else {
            nextNode = NodeDiskUtils.getNodeByPointer(currentNode.getPointerRight());
        }

        nextNode.insertBitSeparator(bitSeparator, currentNode, tree, word);
        return;
    }

}
