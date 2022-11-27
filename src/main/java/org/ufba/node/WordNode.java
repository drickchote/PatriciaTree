package org.ufba.node;

import org.ufba.enums.NodeType;
import org.ufba.enums.WordClass;
import org.ufba.utils.ArrayUtils;
import org.ufba.utils.BinaryUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.ufba.Constants.*;

/**
 * This class controls the leaves of the tree
 *
 *  Note: word and wordTranslations are array of characters for the ease control of word storage size
 *  but they are treat as String and String list respectively when manipulated by getters and setters in memory.
 * */
public class WordNode extends Node{
    private char[] word = new char[MAX_WORD_LENGTH];

    private WordClass wordClass;

    private int numberOfTranslations;

    private char wordTranslations[][] = new char[MAX_TRANSLATIONS_PER_WORD][MAX_TRANSLATION_LENGTH];

    public WordNode(String word, WordClass wordClass, List<String> wordTranslations){
        super(NodeType.word);
        this.setWord(word);
        this.wordClass = wordClass;
        this.setWordTranslations(wordTranslations);
    }

    public WordNode(char [] word, WordClass wordClass, char [][] wordTranslations, int numberOfTranslations){
        super(NodeType.word);
        this.word = word;
        this.wordClass = wordClass;
        this.wordTranslations = wordTranslations;
        this.numberOfTranslations = numberOfTranslations;
    }

    public WordNode(char [] word, WordClass wordClass, char [][] wordTranslations, int numberOfTranslations, String pointerToDisk){
        super(NodeType.word);
        this.word = word;
        this.wordClass = wordClass;
        this.wordTranslations = wordTranslations;
        this.numberOfTranslations = numberOfTranslations;
        this.setPointerToDisk(pointerToDisk);
    }

    /**
    * Create a word node from bytes
    * for better understanding take a look at WordNodeTest.from
    * */
    public static WordNode from(byte [] bytes){

        byte [] wordBytes = Arrays.copyOfRange(bytes,0, MAX_WORD_LENGTH);
        char [] word = new String(wordBytes).toCharArray();

        byte [] wordClassBytes = Arrays.copyOfRange(bytes,wordBytes.length, wordBytes.length + 1);
        WordClass wordClass = WordClass.valueOf(new String(wordClassBytes));

        int offset = wordBytes.length + wordClassBytes.length;
        byte [] numberOfTranslationsBytes = Arrays.copyOfRange(bytes,offset, offset + Integer.BYTES);
        int numberOfTranslations = BinaryUtils.convertBytesToInt(numberOfTranslationsBytes);

        int pointerToDiskOffset = wordBytes.length + wordClassBytes.length + numberOfTranslationsBytes.length;
        int pointerToDiskOffsetEnd = pointerToDiskOffset+POINTER_SIZE;
        byte [] pointerToDiskBytes = Arrays.copyOfRange(bytes,pointerToDiskOffset, pointerToDiskOffsetEnd);
        String pointerToDisk = ArrayUtils.getStringFrom(pointerToDiskBytes);

        char [][] translations = new char[MAX_TRANSLATIONS_PER_WORD][MAX_TRANSLATION_LENGTH];

        int translationsOffset = wordBytes.length + wordClassBytes.length + numberOfTranslationsBytes.length + pointerToDiskBytes.length;
        int translationOffsetEnd = translationsOffset + (MAX_TRANSLATIONS_PER_WORD * MAX_TRANSLATION_LENGTH);
        byte [] translationsBytes = Arrays.copyOfRange(bytes,translationsOffset, translationOffsetEnd);
        for(int i = 0; i< numberOfTranslations; i++){
            int beginOffset = i * MAX_TRANSLATION_LENGTH;
            int endOffset = beginOffset + MAX_TRANSLATION_LENGTH;
            byte[] translationBytes = Arrays.copyOfRange(translationsBytes,beginOffset, endOffset);
            translations[i] = (new String(translationBytes).toCharArray());
        }

        return new WordNode(word, wordClass, translations, numberOfTranslations, pointerToDisk);
    }


    public String getWordTypeInPortuguese(){
        switch (wordClass){
            case a:
                return "adjetivo";
            case s:
                return "substantivo";
            case v:
                return "verbo";
            default:
                throw new Error("WordType "+ wordClass +" does is invalid");
        }
    }

    public String getWord() {
        return ArrayUtils.getStringFrom(this.word);
    }

    public void setWord(String word) {
        ArrayUtils.copyStringToCharacter(word, this.word);
    }

    public char[] getWordCharacters(){
        return this.word;
    }

    public List<String> getWordTranslations() {
        List<String> translations = new ArrayList<>();

        for(int i=0; i<numberOfTranslations; i++){
            translations.add(ArrayUtils.getStringFrom(wordTranslations[i]));
        }

        return translations;
    }

    public void setWordTranslations(List<String> wordTranslations) {

        this.numberOfTranslations = wordTranslations.size();

        for(int i=0; i< numberOfTranslations; i++){
            ArrayUtils.copyStringToCharacter(wordTranslations.get(i), this.wordTranslations[i]);
        }
    }


    public char[][] getWordTranslationsCharacters(){
        return this.wordTranslations;
    }

    public WordClass getWordClass() {
        return wordClass;
    }

    public int getNumberOfTranslations() {
        return numberOfTranslations;
    }

    public static int getBytesLength(){
        return MAX_WORD_LENGTH + WordClass.a.toString().getBytes().length + Integer.BYTES + (MAX_TRANSLATIONS_PER_WORD * MAX_TRANSLATION_LENGTH) + POINTER_SIZE;
    }

    @Override
    public String toString() {
        return "WordNode{" +
            "word=" + Arrays.toString(word) +
            ", wordClass=" + wordClass +
            ", numberOfTranslations=" + numberOfTranslations +
            ", wordTranslations=" + Arrays.toString(wordTranslations) +
            ", pointerToDisk='" + pointerToDisk + '\'' +
            '}';
    }
}
