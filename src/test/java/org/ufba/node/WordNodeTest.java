package org.ufba.node;

import org.ufba.enums.WordClass;
import org.ufba.utils.FileUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WordNodeTest {

    @org.junit.jupiter.api.Test
    void from() {
        List<String> translations = new ArrayList<>();
        translations.add("word-translation-1");
        translations.add("word-translation-2");

        WordNode wordNode = new WordNode("word", WordClass.s, translations);
        assertEquals("word", wordNode.getWord());
        assertEquals("word-translation-1", wordNode.getWordTranslations().get(0));
        assertEquals("word-translation-2", wordNode.getWordTranslations().get(1));
        wordNode.setDiskFile("word");
        wordNode.setDiskOffset(0);

        byte [] bytes = FileUtils.convertNodeToBytes(wordNode);

        WordNode createdWordNode = WordNode.from(bytes);

        assertEquals(wordNode.getWord(), createdWordNode.getWord());
        assertEquals(wordNode.getWordClass(), createdWordNode.getWordClass());
        assertEquals(wordNode.getNumberOfTranslations(), createdWordNode.getNumberOfTranslations());
        assertEquals(wordNode.getNodeType(), createdWordNode.getNodeType());
        assertEquals(wordNode.getWordTranslations().size(), createdWordNode.getWordTranslations().size());
        assertEquals("word_0", createdWordNode.getPointerToDisk());

        for(int i=0; i< wordNode.getWordTranslationsCharacters().length; i++){
            assertTrue(Arrays.equals(wordNode.getWordTranslationsCharacters()[i], createdWordNode.getWordTranslationsCharacters()[i]));
        }
    }
}