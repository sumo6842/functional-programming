package article;

import lombok.Value;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;

@Value
class Article {
    String title;
    List<String> tags;

    public Article(String title, String ... tags) {
        this.title = title;
        this.tags = Arrays.asList(tags);
    }

    public boolean hasTitle(String title) { return this.title.equals(title); }
    public boolean hasTags(String tag) { return this.tags.contains(tag);}

}

@FunctionalInterface
interface Archive{
    List<Article> article();

    private List<Article> filterBy(Predicate<Article> conditions) {
        return article().stream()
                .filter(conditions)
                .collect(toList());
    }

    default List<Article> findByTitle(String title) {
        return filterBy(a -> a.hasTitle(title));
    }

    default List<Article> findByTags(String tag) {
        return filterBy(a -> a.hasTags(tag));
    }

}

public class Main {
    public static void main(String[] args) {
        Archive cnn = () -> Arrays.asList(
            new Article("Trump's tax cuts and the trade war", "Trump", "tax", "trade war"),
            new Article("Trump's claims on economist recession warnings", "Trump", "econimist"),
            new Article("China trade war spirals as 2020 looms", "China", "trade war")
        );

        System.out.println("Trump ->");
        cnn.findByTags("Trump").forEach(System.out::println);
        System.out.println("Trade war");
        cnn.findByTags("trade war").forEach(System.out::println);

    }
}
