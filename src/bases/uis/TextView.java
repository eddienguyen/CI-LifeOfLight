package bases.uis;


import bases.Vector2D;
import bases.renderers.LineRenderer;
import bases.renderers.WordsRenderer;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by huynq on 7/28/17.
 */
public class TextView extends GamePanel {
    ArrayList<LineRenderer> lineRenderers;

    private final String SEPARATOR = "------------------------------------------------------------------------------------------------------------------------";
    private Color textColor;
    public static final int HEX_NUMBER_OF_CHAR = 7;

    private FontMetrics fontMetrics;
    private int linesMax = -1;
    Vector2D offsetText;
    private final Object renderLock;

    TextView() {
        super();
        lineRenderers = new ArrayList<>();
        offsetText = new Vector2D();
        textColor = Color.WHITE;
        fontMetrics = null;
        linesMax = -1;
        renderLock = new Object();
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }

    public Vector2D getOffsetText() {
        return offsetText;
    }

    @Override
    public void render(Graphics2D g2d) {
        super.render(g2d);
        g2d.setColor(textColor);

        if (fontMetrics == null) {
            setFontMetrics(g2d.getFontMetrics());
        }

        drawVerticalLines(g2d);

        Vector2D drawPosition = drawPosition();

        g2d.drawString(SEPARATOR, drawPosition.x, drawPosition.y);

        Vector2D realPosition = drawPosition.add(offsetText);

        synchronized (renderLock) {
            for (LineRenderer lineRenderer : lineRenderers) {
                lineRenderer.render(g2d, realPosition);
                realPosition = realPosition.add(0, fontMetrics.getHeight());
            }
        }
    }

    private Vector2D drawPosition() {
        return position.subtract(getAnchor().x * getSize().x, getAnchor().y * getSize().y);
    }

    private void drawVerticalLines(Graphics2D g2d) {
        Vector2D drawPosition = drawPosition();
        for (int i = 0; i < (getSize().y / g2d.getFontMetrics().getHeight()) + 1; i++) {
            g2d.drawString("|", drawPosition.x, drawPosition.y);
            drawPosition.y += fontMetrics.getHeight();
        }
    }

    void clear() {
        synchronized (renderLock) {
            this.lineRenderers.clear();;
        }
    }

    void setFontMetrics(FontMetrics fontMetrics) {
        this.fontMetrics = fontMetrics;
        if (linesMax == -1) {
            linesMax = (int) ((getSize().y - offsetText.y) / fontMetrics.getHeight()) - 1;
        }
    }

    void addText(String str) {
        if (fontMetrics == null) {
            System.out.println("Font metrics is not ready");
        } else {
            ArrayList<WordsRenderer> wordsRenderers = new ArrayList<>();

            for (String words : str.split(";")) {
                if (words.length() > 0) {
                    wordsRenderers.addAll(WordsRenderer.parse(words));
                }
            }

            LineRenderer newLineRenderer = new LineRenderer();

            for (int wordIndex = 0; wordIndex < wordsRenderers.size(); wordIndex++) {
                WordsRenderer wordsRenderer = wordsRenderers.get(wordIndex);
                newLineRenderer.add(wordsRenderer);

                boolean lineLengthExceeds = newLineRenderer.stringWidth(fontMetrics) - 20 > this.getSize().y;
                boolean isLastWord = (wordIndex == wordsRenderers.size() - 1);
                if (lineLengthExceeds || isLastWord) {
                    synchronized (renderLock) {
                        this.lineRenderers.add(newLineRenderer);
                        newLineRenderer = new LineRenderer();
                        if (lineRenderers.size() > linesMax) {
                            // Trim the begnining
                            for (int lineIndex = 0; lineIndex < (lineRenderers.size() - linesMax) && lineRenderers.size() > 0; lineIndex++) {
                                lineRenderers.remove(0);
                            }
                        }
                    }
                }
            }
        }
    }
}
