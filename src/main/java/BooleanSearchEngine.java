import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BooleanSearchEngine implements SearchEngine {
    Map<String, List<PageEntry>> wordsIndex = new HashMap<>();

    public BooleanSearchEngine(File pdfsDir) throws IOException {
        // прочтите тут все pdf и сохраните нужные данные,
        // тк во время поиска сервер не должен уже читать файлы

        File[] listFiles = pdfsDir.listFiles();
        for (File file : listFiles) {
            var doc = new PdfDocument(new PdfReader(file));
            for (int i = 1; i < doc.getNumberOfPages() + 1; i++) {
                PdfPage page = doc.getPage(i);
                var text = PdfTextExtractor.getTextFromPage(page);
                var words = text.split("\\P{IsAlphabetic}+");

                //Считаем количество каждого слова на странице
                Map<String, Integer> wordCount = new HashMap<>();
                for (String word : words) {
                    if (word.isEmpty()){
                        continue;
                    }
                    wordCount.put(word.toLowerCase(), wordCount.getOrDefault(word.toLowerCase(), 0) + 1);
                }

                //на каждое слово создаем PageEntry, проверяем есть ли оно у нас
                for (Map.Entry<String, Integer> map : wordCount.entrySet()) {
                    PageEntry pageEntry = new PageEntry(file.getName(), i, map.getValue());
                    if (wordsIndex.containsKey(map.getKey())){
                        wordsIndex.get(map.getKey()).add(pageEntry);
                    }else{
                        List<PageEntry> list = new ArrayList<>();
                        list.add(pageEntry);
                        wordsIndex.put(map.getKey(), list);
                    }
                }
            }
        }
    }

    @Override
    public List<PageEntry> search(String word) {
        // тут реализуйте поиск по слову
        for (Map.Entry<String, List<PageEntry>> map : wordsIndex.entrySet()) {
            if (map.getKey().equals(word.toLowerCase())){
                List<PageEntry> result = map.getValue();
                Collections.sort(result);
                return result;
            }
        }
        return Collections.emptyList();
    }
}
