package main;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class TextProc {
    private static final String SENTENCE_REGEX = "[^.!?]+[.!?]+";

    public void processText() {
        try {

            // C3 = 0, тому основний текст має тип StringBuilder
            StringBuilder text = new StringBuilder(
                    "Це перше речення. Це друге речення, в якому є слово Java! "
                            + "Третє речення також має слово Java. "
                            + "А це речення про Python. Java це мова програмування."
                            + " Останнє речення без ключових слів."
            );

            // Масив слів, які потрібно шукати
            String[] searchWords = {"Java", "Python", "неіснуюче"};

            if (text == null || text.length() == 0) {
                throw new IllegalArgumentException("Текст не може бути порожнім або null.");
            }

            if (searchWords == null || searchWords.length == 0) {
                throw new IllegalArgumentException("Масив слів для пошуку не може бути порожнім або null.");
            }

            Map<String, Integer> wordInSentenceCount = new HashMap<>();
            for (String word : searchWords) {

                if (word == null || word.trim().isEmpty()) {
                    System.out.println("Попередження: пропущено null або порожнє слово в масиві пошуку.");
                    continue;
                }
                wordInSentenceCount.put(word.toLowerCase(), 0);
            }

            Pattern sentencePattern = Pattern.compile(SENTENCE_REGEX);
            Matcher sentenceMatcher = sentencePattern.matcher(text);

            while (sentenceMatcher.find()) {
                String currentSentence = sentenceMatcher.group().toLowerCase();
                for (String word : wordInSentenceCount.keySet()) {

                    // Створюємо патерн для пошуку *цілого* слова
                    // \b - позначає межу слова
                    // Pattern.quote() - екранує будь-які спецсимволи,
                    Pattern wordPattern = Pattern.compile("\\b" + Pattern.quote(word) + "\\b");
                    Matcher wordMatcher = wordPattern.matcher(currentSentence);
                    if (wordMatcher.find()) {
                        wordInSentenceCount.put(word, wordInSentenceCount.get(word) + 1);
                    }
                }
            }

            System.out.println("Оригінальний текст (StringBuilder):");
            System.out.println(text.toString());
            System.out.println("\nМасив слів для пошуку:");
            System.out.println(java.util.Arrays.toString(searchWords));
            System.out.println("----------------------------------------");
            System.out.println("Результати підрахунку:");

            if (wordInSentenceCount.isEmpty()) {
                System.out.println("Не було слів для пошуку.");
            } else {
                for (Map.Entry<String, Integer> entry : wordInSentenceCount.entrySet()) {
                    System.out.printf("Слово '%s' зустрічається у %d реченнях.%n",
                            entry.getKey(), entry.getValue());
                }
            }

        } catch (IllegalArgumentException e) {
            System.err.println("ПОМИЛКА ВХІДНИХ ДАНИХ: " + e.getMessage());
        } catch (NullPointerException e) {
            System.err.println("ПОМИЛКА: Виникло звернення до null об'єкта. " + e.getMessage());
        }
    }
}
