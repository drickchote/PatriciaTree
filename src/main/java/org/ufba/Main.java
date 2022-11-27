package org.ufba;

import org.ufba.enums.SortType;
import org.ufba.enums.WordClass;
import org.ufba.node.WordNode;
import org.ufba.utils.ConsoleUtils;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        PatriciaTree patriciaTree = new PatriciaTree();


        String input = null;
        while(input ==null || !input.equals("e")){
            input = scanner.nextLine();
            switch (input){
                case "i":
                    WordNode wordNode = ConsoleUtils.getWordFromConsole();
                    patriciaTree.insertWord(wordNode);
                    break;
                case "l": {
                    SortType sortType = ConsoleUtils.getSortType(scanner.nextLine());
                    patriciaTree.listWords(sortType);
                    break;
                }
                case "t":{
                    String word = scanner.nextLine();
                    patriciaTree.listTranslations(word);
                    break;
                }
                case "a":{
                    WordClass wordClass = WordClass.valueOf(scanner.nextLine());
                    SortType sortType = ConsoleUtils.getSortType(scanner.nextLine());
                    patriciaTree.listWordsByClass(wordClass, sortType);
                    break;
                }
                case "c":{
                    String word = scanner.nextLine();
                    patriciaTree.printWordClass(word);
                    break;
                }
                case "r":{
                    String word = scanner.nextLine();
                    patriciaTree.removeWord(word);
                    break;
                }
                case "p":
                    patriciaTree.printTree();
                    break;
                case "e":
                    continue;
                default:
                    System.out.println("Opcao inválida!");
                    break;
            }
        }
    }
}