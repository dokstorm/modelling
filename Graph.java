import javafx.util.Pair;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.util.Pair;
import javax.swing.*;

public class Graph extends JPanel {

    private Color lineColor = new Color(44, 102, 230, 180);
    private Color pointColor = new Color(100, 100, 100, 180);
    private Color gridColor = new Color(200, 200, 200, 200);
    private static final Stroke GRAPH_STROKE = new BasicStroke(2f);
    private static List<Pair<Double, Double>> pnt = Source.getData();
    private static List<Double> scores = new ArrayList<>();


    private Graph(List<Double> scores) {
        Graph.scores = scores;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        List<Double> xpnt = new ArrayList<>();
        List<Double> ypnt = new ArrayList<>();
        for (int i = 0; i < pnt.size(); i++) {
            xpnt.add(pnt.get(i).getKey());
            ypnt.add(pnt.get(i).getValue());
        }
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int padding = 25;
        int labelPadding = 25;
        double xScale = ((double) getWidth() - (2 * padding) - labelPadding) / (Collections.max(xpnt) - Collections.min(xpnt));
        double yScale = ((double) getHeight() - 2 * padding - labelPadding) / (Collections.max(ypnt) - Collections.min(ypnt));

        List<Point> graphPoints = new ArrayList<>();
        for (int i = 0; i < pnt.size(); i++) {
            int x1 = (int) ((Collections.max(xpnt) - xpnt.get(i)) * xScale + padding + labelPadding);
            int y1 = (int) ((Collections.max(ypnt) - ypnt.get(i)) * yScale + padding);
            graphPoints.add(new Point(x1, y1));
        }

        // draw white background
        g2.setColor(Color.WHITE);
        g2.fillRect(padding + labelPadding, padding, getWidth() - (2 * padding) - labelPadding, getHeight() - 2 * padding - labelPadding);
        g2.setColor(Color.BLACK);

        // create hatch marks and grid lines for y axis.
        int pointWidth = 4;
        int numberYDivisions = 10;
        for (int i = 0; i < numberYDivisions + 1; i++) {
            int x0 = padding + labelPadding;
            int x1 = pointWidth + padding + labelPadding;
            int y0 = getHeight() - ((i * (getHeight() - padding * 2 - labelPadding)) / numberYDivisions + padding + labelPadding);
            if (pnt.size() > 0) {
                g2.setColor(gridColor);
                g2.drawLine(padding + labelPadding + 1 + pointWidth, y0, getWidth() - padding, y0);
                g2.setColor(Color.BLACK);
                String yLabel = ((int) ((Collections.min(ypnt) + (Collections.max(ypnt) - Collections.min(ypnt)) * ((i * 1.0) / numberYDivisions)) * 100)) / 100.0 + "";
                FontMetrics metrics = g2.getFontMetrics();
                int labelWidth = metrics.stringWidth(yLabel);
                g2.drawString(yLabel, x0 - labelWidth - 5, y0 + (metrics.getHeight() / 2) - 3);
            }
            g2.drawLine(x0, y0, x1, y0);
        }

        // and for x axis
        for (int i = 0; i < pnt.size(); i++) {
            if (pnt.size() > 1) {
                int x0 = i * (getWidth() - padding * 2 - labelPadding) / (pnt.size() - 1) + padding + labelPadding;
                int y0 = getHeight() - padding - labelPadding;
                int y1 = y0 - pointWidth;
                if ((i % ((int) ((pnt.size() / 20.0)) + 1)) == 0) {
                    g2.setColor(gridColor);
                    g2.drawLine(x0, getHeight() - padding - labelPadding - 1 - pointWidth, x0, padding);
                    g2.setColor(Color.BLACK);
                   /* String xLabel = String.format("%.2f", Source.getTime() * i / pnt.size()) + "";
                    FontMetrics metrics = g2.getFontMetrics();
                    int labelWidth = metrics.stringWidth(xLabel);
                    if (i != 0)
                        g2.drawString(xLabel, x0 - labelWidth / 2, y0 + metrics.getHeight() + 3);*/
                }
                g2.drawLine(x0, y0, x0, y1);
            }
        }

        // create x and y axes 
        g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, padding + labelPadding, padding);
        g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, getWidth() - padding, getHeight() - padding - labelPadding);

        Stroke oldStroke = g2.getStroke();
        g2.setColor(lineColor);
        g2.setStroke(GRAPH_STROKE);
        for (int i = 0; i < graphPoints.size() - 1; i++) {
            int x1 = graphPoints.get(i).x;
            int y1 = graphPoints.get(i).y;
            int x2 = graphPoints.get(i + 1).x;
            int y2 = graphPoints.get(i + 1).y;
            g2.drawLine(x1, y1, x2, y2);
        }

        g2.setStroke(oldStroke);
        g2.setColor(pointColor);
        for (Point graphPoint : graphPoints) {
            int x = graphPoint.x - pointWidth / 2;
            int y = graphPoint.y - pointWidth / 2;
            g2.fillOval(x, y, pointWidth, pointWidth);
        }
    }

    private double getMinScore() {
        return Collections.min(scores);
    }

    private double getMaxScore() {
        return Collections.max(scores);
    }

    private static void createAndShowGui() {
        Graph mainPanel = new Graph(scores);
        mainPanel.setPreferredSize(new Dimension(800, 600));
        JFrame frame = new JFrame("DrawGraph");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(mainPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Graph::createAndShowGui);
    }
}