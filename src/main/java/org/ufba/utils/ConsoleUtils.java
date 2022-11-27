package org.ufba.utils;

import org.ufba.enums.SortType;
import org.ufba.enums.WordClass;
import org.ufba.node.WordNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsoleUtils {
    public static WordNode getWordFromConsole(){
        Scanner scanner = new Scanner(System.in);

        String word =  scanner.nextLine();
        WordClass wordClass =  WordClass.valueOf(scanner.nextLine());
        int numberOfTranslations = Integer.parseInt(scanner.nextLine());

        List<String> wordTranslations = new ArrayList<>();

        for(int i=0; i<numberOfTranslations; i++){
            wordTranslations.add(scanner.nextLine());
        }

        return new WordNode(word, wordClass,wordTranslations);
    }

    public static SortType getSortType(String sortOption){
        switch (sortOption){
            case "c":
                return SortType.ascending;
            case "d":
                return SortType.descending;
            default:
                throw new Error("Sort option "+ sortOption +" does not exists");
        }
    }
}
