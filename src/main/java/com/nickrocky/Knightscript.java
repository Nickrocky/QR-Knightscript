package com.nickrocky;

import lombok.SneakyThrows;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class Knightscript {

    private static String test = "Served on locally sourced artisan breads. Add bacon or a fried egg to any sandwich\n" +
            "\n" +
            "TRUFFLE MELT\n" +
            "Havarti, truffle oil, arugula, fresh ground pepper\n" +
            "THE BIG CHEESE\n" +
            "Cheddar, Swiss, Muenster, Jack cheese, arugula\n" +
            "PESTO CHICKEN\n" +
            "Mozzarella, pulled chicken, pesto, tomato chutney, arugula\n" +
            "The Smokehouse\n" +
            "Chipotle Cheddar, BBQ pulled pork, mac & cheese\n" +
            "FIG and GOAT\n" +
            "Havarti, Goat cheese, mission fig, basil, honey\n" +
            "BUFFALO CHICKEN\n" +
            "Muenster, housemade buffalo cheese, pulled chicken, buffalo sauce, choice of housemade blue cheese or ranch dressing\n" +
            "THE 101\n" +
            "Cheddar, tomato\n" +
            "BRAISED BRISKET\n" +
            "Fontina, braised brisket, housemade BBQ sauce, sweet tea caramelized onions\n" +
            "AS GOUDA AS IT GETS\n" +
            "Smoked Gouda, pulled chicken, roasted red peppers, garlic spinach\n" +
            "Barbacoa Melt\n" +
            "Jack & Cotija cheese, barbacoa braised beef, housemade cilantro chimichurri, pickled red onion\n" +
            "GrilledCheese\n" +
            "\n" +
            "Burgers\n" +
            "Served on locally sourced artisan bun. Add bacon or a fried egg to any burger\n" +
            "\n" +
            "SOUTHERN COMFORT\n" +
            "Cheddar, applewood smoked bacon, housemade BBQ sauce, mac & cheese\n" +
            "HOLY BASIL\n" +
            "Turkey burger, Mozzarella, tomato chutney, pesto, arugula\n" +
            "SWEET HEAT\n" +
            "Jack cheese, brown sugar caramelized pineapple, housemade sriracha BBQ sauce, cilantro\n" +
            "BANH MI\n" +
            "Turkey burger, Vietnamese pickled carrot & daikon, cucumbers, cilantro, jalapenos, sriracha mayo\n" +
            "Angry Burger\n" +
            "Chipotle Cheddar, charred jalapenos, sweet tea caramelized onions, sriracha\n" +
            "VIVA LA VEGAN\n" +
            "Housemade vegan burger, Toasted???s sauce, tomato chutney, arugula\n" +
            "THE BELLA\n" +
            "Swiss, confit portobellos, sweet tea caramelized onions\n" +
            "THE CLASSIC\n" +
            "Choice of beef or turkey, Cheddar, Toasted???s sauce, leaf lettuce, tomato\n" +
            "BUFFALO BOMB\n" +
            "Muenster, housemade buffalo cheese, spring mix, choice of Blue cheese or ranch dressing\n" +
            "Burgers\n" +
            "Burgers\n" +
            "Sides\n" +
            "FRENCH FRIES\n" +
            "TRUFFLE FRIES\n" +
            "Truffle oil & fresh rosemary\n" +
            "TOASTED???S MAC and CHEESE\n" +
            "Sharp Cheddar, Asiago, Mozzarella\n" +
            "HOUSEMADE ROASTED TOMATO SOUP\n" +
            "Dipper / Cup\n" +
            "Make Your Own\n" +
            "ANGRY Jalapeno, sriracha\n" +
            "FANCY Truffle honey\n" +
            "BBQ Sriracha BBQ sauce, Cotija cheese\n" +
            "Sides\n" +
            "Sides\n" +
            "Salads\n" +
            "Served on spring mix with housemade dressings. Add roasted chicken to any salad\n" +
            "\n" +
            "TUSCAN SALAD\n" +
            "Goat cheese, dried cranberries, roasted pistachios, balsamic vinaigrette\n" +
            "SUMMER SALAD\n" +
            "Blue cheese crumbles, seasonal fruit, red onions, candied pecans, sesame-poppyseed dressing\n" +
            "Caprese Salad\n" +
            "Buffalo Mozzarella, basil, grape tomatoes, cucumbers, roasted red peppers, truffle-honey dressing\n" +
            "Little Muenster's Menu\n" +
            "Little Muenster's Menu\n" +
            "Served with choice of drink and side\n" +
            "\n" +
            "Cheeseburger\n" +
            "choice of beef, turkey or vegan patty, American cheese, spring mix, tomato\n" +
            "Grilled Cheese\n" +
            "American cheese, choice of tomato\n" +
            "Mac and Cheese\n" +
            "Housemade with three cheeses";


    @SneakyThrows
    public static void main(String... margs){
        KQRWriter kqrWriter = new KQRWriter();
        kqrWriter.create("Test", EncodingSchema.BYTE, test.getBytes());

    }

}
