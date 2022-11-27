package org.ufba;

import org.ufba.enums.SortType;
import org.ufba.enums.WordClass;
import org.ufba.node.BitSeparator;
import org.ufba.node.Node;
import org.ufba.node.WordNode;
import org.ufba.utils.BinaryUtils;
import org.ufba.utils.NodeDiskUtils;

import java.util.List;

public class PatriciaTree {
    private Node root;

    PatriciaTree(){
        this.root = NodeDiskUtils.getRoot();
    }

    public void insertWord(WordNode node){
        if(root == null){
            root = node;
            System.out.println("palavra inserida no dicionario: "+node.getWord());
            WordNode savedUserNode = (WordNode) NodeDiskUtils.writeNodeOnDisk(root);
            NodeDiskUtils.updateRootPointerOnDisk(savedUserNode.getPointerToDisk());
            return;
        }

        WordNode leaf = this.root.findLeaf(node.getWord());
        if(leaf.getWord().equals(node.getWord())){
            System.out.println("palavra ja existente: "+ node.getWord());
            return;
        }

        NodeDiskUtils.writeNodeOnDisk(node);

        int bitDifferencePosition = BinaryUtils.bitDifference(leaf.getWord(), node.getWord());
        BitSeparator bitSeparator = new BitSeparator(bitDifferencePosition);

        if(BinaryUtils.binaryAt(node.getWord(), bitDifferencePosition) == '0'){
            bitSeparator.setPointerLeft(node.getPointerToDiskCharacters());
        }  else {
            bitSeparator.setPointerRight(node.getPointerToDiskCharacters());
        }

        System.out.println("palavra inserida no dicionario: "+node.getWord());
        insertBitSeparator(bitSeparator, node.getWord());
    }

    private void insertBitSeparator(BitSeparator bitSeparator, String word){
        this.root.insertBitSeparator(bitSeparator, null, this, word);
    }


    public void listWords(SortType sortType){
        if(this.root == null){
            return;
        }
        this.root.printWords(sortType, null, false);
    }

    public void listTranslations(String word){
        List<String> wordTranslations = null;

        WordNode wordNode = null;

        if(root != null){
            wordNode = root.findWord(word);
        }

        if(wordNode!= null){
            wordTranslations = wordNode.getWordTranslations();
        }

        if(wordTranslations == null){
            System.out.println("palavra inexistente no dicionario: "+word);
            return;
        }

        System.out.println("traducoes da palavra: "+word);
        for (String translation: wordTranslations) {
            System.out.println(translation);
        }
    }

    public void listWordsByClass(WordClass wordClass, SortType sortType){
        if(root == null){
            return;
        }
        this.root.printWords(sortType, wordClass, false);
    }

    public void printWordClass(String word){
        if(root == null){
            return;
        }
        WordNode node = this.root.findWord(word);

        if(node == null){
            System.out.println("palavra inexistente no dicionario: "+word);
            return;
        }

        System.out.println(node.getWordTypeInPortuguese());
    }

    public void removeWord(String word){
        if(root == null){
            System.out.println("palavra inexistente no dicionario: "+word);
        }

        boolean removed = root.removeWord(word, null ,this);
        if(removed){
            System.out.println("palavra removida: "+word);
        } else {
            System.out.println("palavra inexistente no dicionario: "+word);
        }
    }

    public void printTree(){
        if(root == null){
            return;
        }

        this.root.printTree();
        this.root.printWords(SortType.ascending, null, true);
    }

    public void updateRoot(Node node){
        this.root = node;
        NodeDiskUtils.root = node;
        NodeDiskUtils.updateRootPointerOnDisk(node != null ? node.getPointerToDisk() : "");
    }
}
