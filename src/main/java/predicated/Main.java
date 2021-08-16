package predicated;

import lombok.Value;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;

enum DishType {
    MEAT, FISH, OTHER;
}

@Value
class Dish {
    String name;
    boolean vegetarian;
    int calories;
    DishType type;

    public static final List<Dish> menu = Arrays.asList(
            new Dish("pork", false, 800, DishType.MEAT),
            new Dish("beef", false, 700, DishType.MEAT),
            new Dish("Chicken", false, 400, DishType.MEAT),
            new Dish("french fries", true, 530, DishType.OTHER),
            new Dish("rice", true, 350, DishType.OTHER),
            new Dish("seasin Fruit", true, 120, DishType.OTHER),
            new Dish("pizza", false, 400, DishType.FISH),
            new Dish("prawns", false, 4000, DishType.FISH),
            new Dish("salmon", false, 450, DishType.FISH)
    );

    @Override
    public String toString() {
        return String.format("%8s|%b|%4d|%8s",
                name, vegetarian, calories, type.name());
    }
}

public class Main {
    static Predicate<List<Dish>> isVegetarianFriendlyMenu = menu ->
            menu.stream().anyMatch(Dish::isVegetarian);
    static Predicate<List<Dish>> isHealthyMenu = menu ->
            menu.stream().allMatch(d -> d.getCalories() < 1000);

    static Function<List<Dish>, Optional<Dish>> findVegetarianDish = menu ->
            menu.stream()
                    .filter(Dish::isVegetarian)
                    .findFirst();
    static Function<List<Dish>, List<String>> findLowCaloriesDishNames = menu ->
            menu.stream()
                    .filter(f -> f.getCalories() < 500)
                    .sorted(Comparator.comparing(Dish::getCalories))
                    .map(Dish::getName)
                    .collect(toList());

    public static void main(String[] args) {
        findLowCaloriesDishNames.apply(Dish.menu)
                .forEach(System.out::println);
    }

}
