package service;
import java.util.*;
import java.util.regex.*;

public class KeywordExtractor {
    private static final Set<String> STOP_WORDS = Set.of(
    "called","using","having","there","their","these","those",
    "where","which","whose","about","because","while",
    "this","that","into","between","after","before",
    "from","with","without","should","could"
);

private static List<String> extractPhrases(String text) {

    List<String> phrases = new ArrayList<>();

    String[] lines = text.split("\\r?\\n");

    Pattern pattern =
        Pattern.compile("\\b([A-Z][a-z]+(?: [A-Z][a-z]+){1,2})\\b");

    for(String line : lines){

        line = line.trim();

        if(line.isEmpty()) continue;

        line = line.replace("-", " ");

        Matcher matcher = pattern.matcher(line);

        while (matcher.find()) {
            phrases.add(matcher.group().toLowerCase());
        }
    }

    return phrases;
}

public static List<String> extractKeywords(String text) {

    Map<String,Integer> freq = new HashMap<>();

    List<String> phrases = extractPhrases(text);

    for(String phrase : phrases){
        freq.put(phrase, freq.getOrDefault(phrase,0)+3);
    }
    text = text.replace("-", " ");

    String[] words = text.split("\\W+");

    for(String w : words){

        w = w.toLowerCase();

        if(w.length() < 4) continue;

        if(STOP_WORDS.contains(w)) continue;

        if(w.endsWith("ing") || w.endsWith("ed")) continue;

        boolean insidePhrase = false;

        for(String p : phrases){
            if(p.contains(w)){
                insidePhrase = true;
                break;
            }
        }

        if(insidePhrase) continue;

        freq.put(w, freq.getOrDefault(w,0)+1);
    }

    List<String> keywords = new ArrayList<>(freq.keySet());

    keywords.sort((a,b)->freq.get(b)-freq.get(a));

    return keywords;
}
    
}
