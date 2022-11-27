package org.ufba.utils;

import org.ufba.enums.NodeType;
import org.ufba.node.BitSeparator;
import org.ufba.node.Node;
import org.ufba.node.WordNode;

import static org.ufba.Constants.*;

public class NodeDiskUtils {

    public static Node root;

    public static Node getRoot(){
        if(root == null){
            root = getRootOnDisk();
        }
        return root;
    }

    private static Node getRootOnDisk(){
        FileUtils fileUtils = new FileUtils(BIT_SEPARATOR_FILE);
        if(!fileUtils.isFileEmpty()){
            return getNodeByPointer(getRootPointer());
        }
        return null;
    }

    private static String getRootPointer(){
        FileUtils fileUtils = FileUtils.from(NodeType.bitSeparator);

        String rootPointer =  ArrayUtils.getStringFrom(fileUtils.readBytes(ROOT_OFFSET, POINTER_SIZE));
        return rootPointer;
    }

    public static Node getNodeByPointer(String pointer){

        if(pointer == null){
            return null;
        }

        String [] arrayPointer = pointer.split("_");

        NodeType type = NodeType.valueOf(arrayPointer[0]);
        int offset = Integer.parseInt(arrayPointer[1]);
        FileUtils fileUtils = FileUtils.from(type);

        byte[] bytes = fileUtils.readBytes(offset, getByteForNode(type));


        if(type.equals(NodeType.word)){
            return WordNode.from(bytes);
        }

        return BitSeparator.from(bytes);
    }

    public static int getByteForNode(NodeType nodeType){
        switch (nodeType){
            case word:
                return WordNode.getBytesLength();
            case bitSeparator:
                return BitSeparator.getBytesLength();
            default:
                throw new Error("Invalid node type");
        }
    }

    public static Node updateNodeOnDisk(Node node, int position) {
        switch (node.getNodeType()){
            case bitSeparator:
                return writeBitSeparatorNodeOnDisk((BitSeparator) node, position);
            case word:
                return writeWordNodeOnDisk((WordNode) node, position);
        }
        return null;
    }

    public static Node writeNodeOnDisk(Node node) {
        switch (node.getNodeType()){
            case bitSeparator:
                return writeBitSeparatorNodeOnDisk((BitSeparator) node);
            case word:
                return writeWordNodeOnDisk((WordNode) node);
        }
        return null;
    }

    public static BitSeparator writeBitSeparatorNodeOnDisk(BitSeparator bitSeparator){
        FileUtils fileUtils = new FileUtils(BIT_SEPARATOR_FILE);
        bitSeparator.setDiskFile(NodeType.bitSeparator.toString());

        fileUtils.writeObject(bitSeparator);
        return bitSeparator;
    }

    public static BitSeparator writeBitSeparatorNodeOnDisk(BitSeparator bitSeparator, int position){
        FileUtils fileUtils = new FileUtils(BIT_SEPARATOR_FILE);
        bitSeparator.setDiskFile(NodeType.bitSeparator.toString());

        fileUtils.writeObject(bitSeparator, position);
        return bitSeparator;
    }

    public static WordNode writeWordNodeOnDisk(WordNode wordNode){
        FileUtils fileUtils = new FileUtils(DICTIONARY_FILE);
        wordNode.setDiskFile(NodeType.word.toString());
        fileUtils.writeObject(wordNode);
        return wordNode;
    }

    public static WordNode writeWordNodeOnDisk(WordNode wordNode, int position){
        FileUtils fileUtils = new FileUtils(DICTIONARY_FILE);
        wordNode.setDiskFile(NodeType.word.toString());
        fileUtils.writeObject(wordNode);
        return wordNode;
    }

    public static void updateRootPointerOnDisk(String pointer){
        FileUtils fileUtils = FileUtils.from(NodeType.bitSeparator);
        pointer += END_OF_STRING;
        fileUtils.writeBytes(pointer.getBytes(), ROOT_OFFSET, POINTER_SIZE);
    }

    public static NodeType getNodeTypeFrom(String pointer){
        return NodeType.valueOf(pointer.split("_")[0]);
    }

}
