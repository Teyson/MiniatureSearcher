package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        System.out.println("Please enter the keyword(s) you want to search for: ");

        Scanner s = new Scanner(System.in);
        String searchWord = s.nextLine();

        //The webpage that will be opened. This variable will be changed later in the program so that all the pages are scraped.
        String webpage = "https://www.shapeways.com/designer/mz4250/creations?s=0#more-products";

        //This boolean is for stopping the while loop when the program gets to the last page.
        boolean goOn = true;

        //For printing information to the user on how far in the search process the program is.
        int pageNum = 1;
        final int totalPageNum = 42;

        //Jsoup attempt
        while (goOn) {
            try {
                //The java object that contains the actual webpage.
                Document doc = Jsoup.connect(webpage).get();

                //Finds all the items on the page
                Elements items = doc.getElementsByClass("product-name");

                //Prints status message to the user
                String pageNumbering = "page " + Integer.toString(pageNum++) + " out of " + Integer.toString(totalPageNum) + ":";
                System.out.println(pageNumbering);

                //Loops through all the items and figures out if it fits the search term
                for (Element item : items) {
                    //Gets the name of the item
                    String text = item.text();

                    //region Make the strings more comparable
                    String lowerCaseText = text.toLowerCase();
                    String lowerCaseSearch = searchWord.toLowerCase();
                    //endregion

                    //Compares whether the search term is part of the item name
                    if (lowerCaseText.contains(lowerCaseSearch)) {
                        //Prints the item name and the url for the item to the user
                        System.out.println("    " + lowerCaseText + ": " + item.child(0).attributes().get("href"));
                    }
                }

                //region Finds the next page arrow
                Elements navArrow = doc.getElementsByClass("sw-pagination__nav-circle\n" +
                        "                  sw-grid-flex\n" +
                        "                  sw-grid-flex--align-center\n" +
                        "                  sw-grid-flex--justify-center\n" +
                        "                  sw--circle\n" +
                        "                  sw--border-grey-1\n" +
                        "                  sw--margin-left-2\n" +
                        "                  sw--text-decoration-none");
                //endregion

                //Checks if there is a next page navigation arrow which is what I use to on to the next page
                //If there is a navigation arrow it takes the href attribute and uses for the next pass through the loop
                if (navArrow.size() > 0) {
                    webpage = "https://www.shapeways.com" + navArrow.attr("href");
                } else {
                    goOn = false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
}
