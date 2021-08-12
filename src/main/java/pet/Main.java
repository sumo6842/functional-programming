package pet;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@NoArgsConstructor
@AllArgsConstructor
class Pet {
    String name, animal, breed, color;
    double price;
    static List<Pet> pets;

    static {
        try {
            var systemResource = ClassLoader.getSystemResource("pets.csv");
            var path = Paths.get(systemResource.toURI());
            pets = Files.lines(path)
                    .map(Pet::new)
                    .collect(toList());
        } catch (IOException | URISyntaxException e) {
            System.out.println(e.getMessage());
        }
    }

    public Pet(String line) {
        var info = line.split(",");
        this.name = info[0];
        this.animal = info[1];
        this.breed = info[2];
        this.color = info[3];
        this.price = Double.parseDouble(info[4]);
    }

    public Pet setAnimal(String animal) {
        this.animal = animal;
        return this;
    }

    public Pet setBreed(String breed) {
        this.breed = breed;
        return this;
    }

    public Pet setPrice(double price) {
        this.price = price;
        return this;
    }

    @Override
    public String toString() {
        return String.format("%s (%s - %s): %s $%s", name, animal, breed, color, price);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + Objects.hashCode(this.animal);
        hash = 23 * hash + Objects.hash(this.breed);
        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Pet)) return false;
        Pet p = (Pet) o;
        return animal.equals(p.animal) && breed.equals(p.breed);
    }
}

class NullablePet extends Pet {
    @Override
    public String toString() {
        return "not found";
    }
}

@FunctionalInterface
interface PetMatcher<T extends Pet> {
    List<T> match(T t);

    default T first(List<T> list, T t) {
        return match(t).stream()
                .findFirst().orElse((T) new NullablePet());
    }
}

public class Main {
    private static final Function<String, Function<PetMatcher<Pet>, Consumer<Pet>>> matcherPet =
            criteria -> matcher -> pet -> {
                System.out.println("\n" + criteria + ":");
                System.out.println("First: " + matcher.first(Pet.pets, pet));
                System.out.println("All match: ");
                matcher.match(pet).forEach(System.out::println);
            };

    /*todo implementation of PetMatcher*/
    private static final PetMatcher<Pet> breedMatcher = pet -> Pet.pets.stream()
            .filter(p -> p.equals(pet))
            .collect(toList());

    /*todo implementation of PetMatcher*/
    private static final PetMatcher<Pet> priceMatcher = pet ->
            Pet.pets.stream()
            .filter(p -> p.price <= pet.price)
            .collect(toList());

    public static void main(String[] args) {
        Pet.pets.forEach(System.out::println);
        matcherPet.apply("This is Poodle")
                .apply(breedMatcher)
                .accept((new Pet()).setAnimal("dog").setBreed("poodle"));
        matcherPet.apply("Pet for 800$ or less: ")
                .apply(priceMatcher)
                .accept(new Pet().setPrice(800.0));

    }

}
