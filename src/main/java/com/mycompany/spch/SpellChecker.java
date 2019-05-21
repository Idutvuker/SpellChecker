package com.mycompany.spch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.apache.log4j.BasicConfigurator;
import org.languagetool.JLanguageTool;
import org.languagetool.language.*;
import org.languagetool.rules.Rule;
import org.languagetool.rules.RuleMatch;

public class SpellChecker {

    private static JLanguageTool langTool;

    private static void initLangTool() {
        BasicConfigurator.configure();
        langTool = new JLanguageTool(new BritishEnglish());

        for (Rule rule : langTool.getAllActiveRules()) {
            if (!rule.isDictionaryBasedSpellingRule()) {
                langTool.disableRule(rule.getId());
            }
        }
    }

    private static void langToolSpellCheck(String word) {
        try {
            List<RuleMatch> matches = langTool.check(word);

            if (matches.isEmpty()) {
                System.out.println("Word is correct");
            } else {
                System.out.println("Word is incorrect");
                ArrayList<String> suggestionList = new ArrayList<>();

                for (RuleMatch match : matches) {
                    for (String suggestion : match.getSuggestedReplacements()) {
                        suggestionList.add(suggestion);
                    }
                }

                if (suggestionList.isEmpty()) {
                    System.out.println("No suggestions found");
                } else {
                    System.out.println("Suggestions:");
                    for (String suggestion : suggestionList) {
                        System.out.println(suggestion);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    public static void main(String[] args) {
        initLangTool();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter one word");
        String word = scanner.nextLine().trim();

        if (!word.contains(" ")) {
            langToolSpellCheck(word);
        } else {
            System.err.println("Input must be one word");
        }

    }
}
