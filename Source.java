import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Source {
    private static int i = 0;
    private static double h = 0.001;
    private static double g = 9.8;
    private static double k = 0.1;

    public static double getTime() {
        return i * h;
    }

    static List<Pair<Double, Double>> getData() {
        Scanner input = new Scanner(System.in);
         /*double m = input.nextDouble();
        double l = input.nextDouble();
        double c = input.nextDouble();
        */
        double m = 0.5, l = 1.0, c = 0;

        double buf;
        List<Double> x = new ArrayList<>();
        double x0 = l;
        x.add(x0);
        List<Double> y = new ArrayList<>();
        double y0 = l;
        y.add(y0);
        //first elements
        x.add(x.get(i) + h * y.get(i));
        y.add(y.get(i) + h * ((-g / l) * Math.sin(x.get(i)) - c * y.get(i) * l * m));
        i++;
        while (Math.abs(x.get(i) - x.get(i - 1)) > 0.00001) {
            x.add(x.get(i) + h * y.get(i));
            y.add(y.get(i) + h * ((-g / l) * Math.sin(x.get(i)) - c * y.get(i) * l * m));
            i++;
        }

        int step = i / 100;
        List<Pair<Double, Double>> result = new ArrayList<>();

        for (int j = 0; j <= i; j += step)
            result.add(new Pair(x.get(i), y.get(i)));

        return result;
        /*for (int j = 0; j <= i; j += step) {
            v.add(y.get(j));
        }

        //System.out.print("Наивысшее положение: " + y.get(i) + " м, время достижения: " + h * i + " с.");
        return v;*/

    }
}
