package kick_start;

import lombok.Value;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

@Value
class Receipt {
    String item;
    double price, discount, tax;
}

@FunctionalInterface
interface  Printer<T extends Receipt> {
    void print(T t);

    private double getDiscountPrice(T t) {
        return t.getPrice() - (t.getPrice() * t.getTax());
    }

    default double computeTotal(T t) {
        double discountPrice = getDiscountPrice(t);
        return discountPrice + (discountPrice * t.getTax());
    }
}

public class Main {

    private static final Function<Receipt, Function<Printer<Receipt>, List<String>>> printTax =
                receipt -> printer -> Arrays.asList(
                        "Item: " + receipt.getItem(),
                        "Price: $" + receipt.getPrice(),
                        "Discount: " + receipt.getDiscount(),
                        "Tax: " + receipt.getTax() + "%",
                        "Total: $" + printer.computeTotal(receipt));

    private static final Printer<Receipt> printer = receipt -> System.out.println("Tax: $" + receipt.getTax());


    public static void main(String[] args) {
        /*todo Override method interface functional*/
        new Printer<Receipt>(){
            @Override
            public void print(Receipt receipt) {
                System.out.println("Item: " + receipt.getItem());
                System.out.println("Price: $" + receipt.getPrice());
                System.out.println("Discount: " + receipt.getDiscount());
                System.out.println("Tax: " + receipt.getTax() + "%");
                System.out.println("Total: $" + computeTotal(receipt));
            }
        }.print(new Receipt("T-shirt", 20, 0.05, 0.07));
    }
}
