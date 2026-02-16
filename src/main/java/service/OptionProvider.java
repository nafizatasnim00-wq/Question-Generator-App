package service;

import java.util.List;

public interface OptionProvider {
    List<String> getOptions(String keyword, String difficulty);
    int getCorrectIndex(String keyword);
}
