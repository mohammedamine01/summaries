package cleanCode.Chapter3.videoStore.refactored;

import java.util.ArrayList;
import java.util.List;

public class Statement {
    private String customerName;
    private List<Rental> rentals = new ArrayList<Rental>();
    private double totalAmount;
    private int frequentRenterPoints;


    public Statement(String customerName) {
        this.customerName = customerName;
    }

    public void addRental(Rental rental) {
        rentals.add(rental);
    }

    public String generate() {
        clearTotals();
        String statementText = header();

        statementText += rentalLines();
        statementText += footer();

        return statementText;
    }

    private void clearTotals() {
        totalAmount = 0;
        frequentRenterPoints = 0;
    }

    private String header() {
        return String.format("Rental Record for %s\n", customerName);
    }

    private String rentalLines() {
        String rentalLines = "";
        for (Rental rental : rentals)
            rentalLines += rentalLine(rental);

        return rentalLines;
    }

    private String rentalLine(Rental rental) {
        double rentalAmount = determineAmount(rental);
        frequentRenterPoints += determineFrequentRenterPoint(rental);
        totalAmount += rentalAmount;

        return formatRentalLine(rental, rentalAmount);
    }

    private String formatRentalLine(Rental rental, double rentalAmount) {
        /*String rentalLine = "";
        rentalLine += "\t" + rental.getMovie().getTitle() + "\t" + String.valueOf(rentalAmount) + "\n";
        return rentalLine;*/

        return String.format("\t%s\t%.1f\n",rental.getTitle(),rentalAmount);
    }

    private int determineFrequentRenterPoint(Rental rental) {
        boolean bonusIsEarned = rental.getMovie().getPriceCode() == Movie.NEW_RELEASE && rental.getDaysRented() > 1;
        if (bonusIsEarned)
            return 2;

        return 1;
    }

    private double determineAmount(Rental rental) {
        double rentalAmount = 0;
        switch (rental.getMovie().getPriceCode()) {
            case Movie.REGULAR:
                rentalAmount += 2;
                if (rental.getDaysRented() > 2)
                    rentalAmount += (rental.getDaysRented() - 2) * 1.5;
                break;
            case Movie.NEW_RELEASE:
                rentalAmount += rental.getDaysRented() * 3;
                break;
            case Movie.CHILDRENS:
                rentalAmount += 1.5;
                if (rental.getDaysRented() > 3)
                    rentalAmount += (rental.getDaysRented() - 3) * 1.5;
                break;
        }
        return rentalAmount;
    }

    private String footer() {
        return String.format(
                "You owed %.1f\n" +
                        "You earned %d frequent renter points\n",
                totalAmount, frequentRenterPoints);
    }

    public double getTotal() {
        return totalAmount;
    }

    public int getFrequentRenterPoints() {
        return frequentRenterPoints;
    }
}
