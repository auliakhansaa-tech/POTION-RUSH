package com.potionrush.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.potionrush.brewing.Cauldron;
import com.potionrush.brewing.CauldronState;
import com.potionrush.brewing.Ingredient;
import com.potionrush.brewing.Recipe;
import com.potionrush.core.GameWorld;
import com.potionrush.core.ScreenState;
import com.potionrush.customer.Customer;

import java.util.ArrayList;

/**
 * libGDX replacement for the old JavaFX GameView + GameLoop.
 *
 * All layout constants use the same top-left origin (y pointing down) as the
 * old JavaFX canvas. libGDX uses a bottom-left origin (y pointing up), so every
 * draw call goes through toScreenY()/rect()/text() helpers that flip the y axis.
 */
public class GameScreen extends InputAdapter implements Screen {

    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;

    // Adventurer slots
    private static final float ADVENTURER_X = 60;
    private static final float ADVENTURER_Y = 90;
    private static final float ADVENTURER_WIDTH = 220;
    private static final float ADVENTURER_HEIGHT = 170;
    private static final float ADVENTURER_GAP = 250;

    // Cauldrons
    private static final float CAULDRON_X = 120;
    private static final float CAULDRON_Y = 330;
    private static final float CAULDRON_WIDTH = 240;
    private static final float CAULDRON_HEIGHT = 170;
    private static final float CAULDRON_GAP = 300;

    // Ingredient buttons
    private static final float INGREDIENT_X = 60;
    private static final float INGREDIENT_Y = 560;
    private static final float INGREDIENT_WIDTH = 140;
    private static final float INGREDIENT_HEIGHT = 80;
    private static final float INGREDIENT_GAP = 160;

    // Undo button
    private static final float UNDO_X = 880;
    private static final float UNDO_Y = 560;
    private static final float UNDO_WIDTH = 140;
    private static final float UNDO_HEIGHT = 80;

    // Center button used on MENU, RINGKASAN and GAME_OVER screens
    private static final float BUTTON_X = 490;
    private static final float BUTTON_Y = 500;
    private static final float BUTTON_WIDTH = 300;
    private static final float BUTTON_HEIGHT = 80;

    // Recipe board
    private static final float RECIPE_X = 1000;
    private static final float RECIPE_Y = 90;
    private static final float RECIPE_WIDTH = 250;
    private static final float RECIPE_HEIGHT = 410;

    // Size of libGDX's built-in default font, used to scale to the wanted size
    private static final float DEFAULT_FONT_SIZE = 15f;

    // Longest frame step allowed, so the game does not jump forward
    // after the browser tab was in the background
    private static final float MAX_DELTA = 0.1f;

    private static final Color BACKGROUND = Color.valueOf("2b2238");
    private static final Color PANEL = Color.valueOf("3d3150");
    private static final Color PANEL_HIGHLIGHT = Color.valueOf("7a5fb0");
    private static final Color BAR_BACKGROUND = Color.valueOf("1a1424");
    private static final Color TITLE_GOLD = Color.valueOf("e8c46a");
    private static final Color TITLE_GREEN = Color.valueOf("7bd88f");
    private static final Color CRIMSON = Color.valueOf("dc143c");
    private static final Color LIME_GREEN = Color.valueOf("32cd32");
    private static final Color GOLD = Color.valueOf("ffd700");
    private static final Color UNDO_COLOR = Color.valueOf("6b2f3a");
    private static final Color RECIPE_BOARD = Color.valueOf("3a2c22");
    private static final Color WHEAT = Color.valueOf("f5deb3");
    private static final Color CAULDRON_FILLED = Color.valueOf("4a3f6b");
    private static final Color CAULDRON_BOILING = Color.valueOf("8a4b2a");
    private static final Color CAULDRON_READY = Color.valueOf("2f6b4a");

    private final GameWorld world;
    private final FitViewport viewport;
    private final ShapeRenderer shapes;
    private final SpriteBatch batch;
    private final BitmapFont font;

    public GameScreen() {
        world = new GameWorld();
        viewport = new FitViewport(WIDTH, HEIGHT);
        shapes = new ShapeRenderer();
        batch = new SpriteBatch();

        font = new BitmapFont();
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        font.setUseIntegerPositions(false);
    }

    // =====================================================
    // SCREEN LIFECYCLE
    // =====================================================

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        // Game logic (replaces GameLoop.handle)
        world.update(Math.min(delta, MAX_DELTA));

        // Drawing (replaces GameView.render)
        ScreenUtils.clear(BACKGROUND);
        viewport.apply();
        shapes.setProjectionMatrix(viewport.getCamera().combined);
        batch.setProjectionMatrix(viewport.getCamera().combined);

        ScreenState state = world.getLayar();
        if (state == ScreenState.MENU) {
            drawMenu();
        } else if (state == ScreenState.MAIN) {
            drawMainScreen();
        } else if (state == ScreenState.RINGKASAN) {
            drawSummary();
        } else if (state == ScreenState.GAME_OVER) {
            drawGameOver();
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        shapes.dispose();
        batch.dispose();
        font.dispose();
    }

    // =====================================================
    // INPUT
    // =====================================================

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // Convert window pixels to world coordinates (y up),
        // then flip back to the top-left system used by the layout constants
        Vector2 point = viewport.unproject(new Vector2(screenX, screenY));
        handleClick(point.x, HEIGHT - point.y);
        return true;
    }

    private void handleClick(float clickX, float clickY) {
        ScreenState state = world.getLayar();

        if (state == ScreenState.MENU) {
            if (isInsideButton(clickX, clickY)) {
                world.mulai();
            }
        } else if (state == ScreenState.MAIN) {
            handleMainClick(clickX, clickY);
        } else if (state == ScreenState.RINGKASAN) {
            if (isInsideButton(clickX, clickY)) {
                world.lanjutHari();
            }
        } else if (state == ScreenState.GAME_OVER) {
            if (isInsideButton(clickX, clickY)) {
                world.keMenu();
            }
        }
    }

    private void handleMainClick(float clickX, float clickY) {
        for (int i = 0; i < GameWorld.MAKS_PETUALANG; i++) {
            float x = ADVENTURER_X + i * ADVENTURER_GAP;
            if (isInside(clickX, clickY, x, ADVENTURER_Y, ADVENTURER_WIDTH, ADVENTURER_HEIGHT)) {
                world.klikPetualang(i);
                return;
            }
        }

        for (int i = 0; i < world.getKuali().size(); i++) {
            float x = CAULDRON_X + i * CAULDRON_GAP;
            if (isInside(clickX, clickY, x, CAULDRON_Y, CAULDRON_WIDTH, CAULDRON_HEIGHT)) {
                world.klikKuali(i);
                return;
            }
        }

        Ingredient[] ingredients = Ingredient.values();
        for (int i = 0; i < ingredients.length; i++) {
            float x = INGREDIENT_X + i * INGREDIENT_GAP;
            if (isInside(clickX, clickY, x, INGREDIENT_Y, INGREDIENT_WIDTH, INGREDIENT_HEIGHT)) {
                world.klikBahan(ingredients[i]);
                return;
            }
        }

        if (isInside(clickX, clickY, UNDO_X, UNDO_Y, UNDO_WIDTH, UNDO_HEIGHT)) {
            world.undo();
        }
    }

    private boolean isInside(float clickX, float clickY, float boxX, float boxY, float width, float height) {
        boolean insideHorizontally = clickX >= boxX && clickX <= boxX + width;
        boolean insideVertically = clickY >= boxY && clickY <= boxY + height;
        return insideHorizontally && insideVertically;
    }

    private boolean isInsideButton(float clickX, float clickY) {
        return isInside(clickX, clickY, BUTTON_X, BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
    }

    // =====================================================
    // MENU, SUMMARY, GAME OVER
    // =====================================================

    private void drawMenu() {
        beginShapes();
        rect(BUTTON_X, BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT, PANEL_HIGHLIGHT);
        shapes.end();

        batch.begin();
        textCentered("POTION RUSH", WIDTH / 2f, 220, 72, TITLE_GOLD);
        textCentered("Brew potions to match each adventurer's order before they run out of patience.",
            WIDTH / 2f, 300, 22, Color.WHITE);
        textCentered("Click an ingredient, click a cauldron, boil it, then serve the adventurer.",
            WIDTH / 2f, 340, 22, Color.WHITE);
        buttonLabel("START");
        batch.end();
    }

    private void drawSummary() {
        int day = world.getSession().getHari();

        beginShapes();
        rect(BUTTON_X, BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT, PANEL_HIGHLIGHT);
        shapes.end();

        batch.begin();
        textCentered("DAY " + day + " COMPLETE", WIDTH / 2f, 170, 56, TITLE_GREEN);
        textCentered("Coins today: " + world.getKoinHariIni() + " / " + world.getTargetHari(),
            WIDTH / 2f, 260, 26, Color.WHITE);
        textCentered("Adventurers served: " + world.getDilayaniHariIni(), WIDTH / 2f, 310, 26, Color.WHITE);
        textCentered("Adventurers who left: " + world.getPergiHariIni(), WIDTH / 2f, 360, 26, Color.WHITE);
        textCentered("Total coins: " + world.getSession().getKoin(), WIDTH / 2f, 410, 26, Color.WHITE);
        buttonLabel("GO TO DAY " + (day + 1));
        batch.end();
    }

    private void drawGameOver() {
        beginShapes();
        rect(BUTTON_X, BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT, PANEL_HIGHLIGHT);
        shapes.end();

        batch.begin();
        textCentered("GAME OVER", WIDTH / 2f, 200, 72, CRIMSON);
        textCentered(world.getAlasanGameOver(), WIDTH / 2f, 280, 26, Color.WHITE);
        textCentered("You reached day " + world.getSession().getHari(), WIDTH / 2f, 340, 26, Color.WHITE);
        textCentered("Total coins: " + world.getSession().getKoin(), WIDTH / 2f, 390, 26, Color.WHITE);
        buttonLabel("BACK TO MENU");
        batch.end();
    }

    private void buttonLabel(String label) {
        textCentered(label, BUTTON_X + BUTTON_WIDTH / 2f, BUTTON_Y + 50, 24, Color.WHITE);
    }

    // =====================================================
    // MAIN SCREEN
    // =====================================================

    private void drawMainScreen() {
        // Pass 1: every filled shape
        beginShapes();
        drawAdventurerShapes();
        drawCauldronShapes();
        drawIngredientShapes();
        rect(UNDO_X, UNDO_Y, UNDO_WIDTH, UNDO_HEIGHT, UNDO_COLOR);
        rect(RECIPE_X, RECIPE_Y, RECIPE_WIDTH, RECIPE_HEIGHT, RECIPE_BOARD);
        shapes.end();

        // Pass 2: every text on top of the shapes
        batch.begin();
        drawHud();
        drawAdventurerTexts();
        drawCauldronTexts();
        drawIngredientTexts();
        text("UNDO", UNDO_X + 45, UNDO_Y + 47, 18, Color.WHITE);
        drawRecipeTexts();
        drawMessage();
        batch.end();
    }

    private void drawHud() {
        text("Day " + world.getSession().getHari(), 30, 45, 22, Color.WHITE);
        text("Time: " + (int) world.getSisaWaktuHari() + " s", 160, 45, 22, Color.WHITE);
        text("Coins today: " + world.getKoinHariIni() + " / " + world.getTargetHari(), 380, 45, 22, Color.WHITE);
        text("Total coins: " + world.getSession().getKoin(), 700, 45, 22, Color.WHITE);
        text("Reputation: " + world.getSession().getReputasi(), 920, 45, 22, Color.WHITE);
    }

    private void drawAdventurerShapes() {
        ArrayList<Customer> adventurers = world.getPetualang();

        for (int i = 0; i < GameWorld.MAKS_PETUALANG; i++) {
            float x = ADVENTURER_X + i * ADVENTURER_GAP;
            rect(x, ADVENTURER_Y, ADVENTURER_WIDTH, ADVENTURER_HEIGHT, PANEL);

            if (i >= adventurers.size()) {
                continue;
            }

            // Patience bar: dark background, then a colored fill
            Customer adventurer = adventurers.get(i);
            float barWidth = ADVENTURER_WIDTH - 30;
            float fillWidth = barWidth * (float) adventurer.getPersenKesabaran();

            rect(x + 15, ADVENTURER_Y + 120, barWidth, 20, BAR_BACKGROUND);
            rect(x + 15, ADVENTURER_Y + 120, fillWidth, 20, patienceColor(adventurer.getTingkatKesabaran()));
        }
    }

    private void drawAdventurerTexts() {
        ArrayList<Customer> adventurers = world.getPetualang();

        for (int i = 0; i < GameWorld.MAKS_PETUALANG; i++) {
            float x = ADVENTURER_X + i * ADVENTURER_GAP;

            if (i >= adventurers.size()) {
                text("(empty)", x + 80, ADVENTURER_Y + 90, 16, Color.GRAY);
                continue;
            }

            Customer adventurer = adventurers.get(i);
            text(adventurer.getJenis(), x + 15, ADVENTURER_Y + 35, 20, Color.WHITE);
            text("Order: " + DisplayName.ramuan(adventurer.getPesanan()), x + 15, ADVENTURER_Y + 75, 16, Color.WHITE);
        }
    }

    private Color patienceColor(String level) {
        if (level.equals("HIJAU")) {
            return LIME_GREEN;
        } else if (level.equals("KUNING")) {
            return GOLD;
        } else {
            return CRIMSON;
        }
    }

    private void drawCauldronShapes() {
        ArrayList<Cauldron> cauldrons = world.getKuali();

        for (int i = 0; i < cauldrons.size(); i++) {
            float x = CAULDRON_X + i * CAULDRON_GAP;
            rect(x, CAULDRON_Y, CAULDRON_WIDTH, CAULDRON_HEIGHT, cauldronColor(cauldrons.get(i).getStatus()));
        }
    }

    private void drawCauldronTexts() {
        ArrayList<Cauldron> cauldrons = world.getKuali();

        for (int i = 0; i < cauldrons.size(); i++) {
            Cauldron cauldron = cauldrons.get(i);
            float x = CAULDRON_X + i * CAULDRON_GAP;

            text("Cauldron " + (i + 1) + " - " + DisplayName.statusKuali(cauldron.getStatus()),
                x + 15, CAULDRON_Y + 35, 20, Color.WHITE);
            text("Contents: " + DisplayName.daftarBahan(cauldron.getIsi()),
                x + 15, CAULDRON_Y + 75, 15, Color.WHITE);

            if (cauldron.getStatus() == CauldronState.MENDIDIH) {
                int secondsLeft = (int) Math.ceil(cauldron.getSisaWaktuMendidih());
                text("Time left: " + secondsLeft + " s", x + 15, CAULDRON_Y + 110, 15, Color.WHITE);
            } else if (cauldron.getStatus() == CauldronState.SIAP) {
                if (cauldron.getHasil() == null) {
                    text("Result: FAILED", x + 15, CAULDRON_Y + 110, 15, Color.WHITE);
                } else {
                    text("Result: " + DisplayName.ramuan(cauldron.getHasil()),
                        x + 15, CAULDRON_Y + 110, 15, Color.WHITE);
                }
            }
        }
    }

    private Color cauldronColor(CauldronState state) {
        if (state == CauldronState.DIISI) {
            return CAULDRON_FILLED;
        } else if (state == CauldronState.MENDIDIH) {
            return CAULDRON_BOILING;
        } else if (state == CauldronState.SIAP) {
            return CAULDRON_READY;
        } else {
            return PANEL;
        }
    }

    private void drawIngredientShapes() {
        Ingredient[] ingredients = Ingredient.values();

        for (int i = 0; i < ingredients.length; i++) {
            float x = INGREDIENT_X + i * INGREDIENT_GAP;

            // The ingredient being held is highlighted
            Color color = PANEL;
            if (ingredients[i] == world.getBahanDipegang()) {
                color = PANEL_HIGHLIGHT;
            }
            rect(x, INGREDIENT_Y, INGREDIENT_WIDTH, INGREDIENT_HEIGHT, color);
        }
    }

    private void drawIngredientTexts() {
        Ingredient[] ingredients = Ingredient.values();

        for (int i = 0; i < ingredients.length; i++) {
            float x = INGREDIENT_X + i * INGREDIENT_GAP;
            text(DisplayName.bahan(ingredients[i]), x + 20, INGREDIENT_Y + 47, 18, Color.WHITE);
        }
    }

    private void drawRecipeTexts() {
        text("RECIPES", RECIPE_X + 80, RECIPE_Y + 35, 20, WHEAT);

        // Each recipe takes two lines: potion name, then its ingredient order
        float lineY = RECIPE_Y + 80;
        for (Recipe recipe : Recipe.semuaResep()) {
            text(DisplayName.ramuan(recipe.getHasil()) + ":", RECIPE_X + 15, lineY, 14, WHEAT);
            text(DisplayName.daftarBahan(recipe.getUrutanBahan()), RECIPE_X + 15, lineY + 20, 14, WHEAT);
            lineY = lineY + 70;
        }
    }

    private void drawMessage() {
        String holding = "Holding nothing";
        if (world.getBahanDipegang() != null) {
            holding = "Holding: " + DisplayName.bahan(world.getBahanDipegang());
        } else if (world.getRamuanDipegang() != null) {
            holding = "Holding: " + DisplayName.ramuan(world.getRamuanDipegang());
        }

        text(holding, 30, 685, 18, Color.WHITE);
        text(world.getPesan(), 400, 685, 18, Color.WHITE);
    }

    // =====================================================
    // DRAWING HELPERS (top-left coordinates -> libGDX coordinates)
    // =====================================================

    private void beginShapes() {
        shapes.begin(ShapeRenderer.ShapeType.Filled);
    }

    // Converts a top-left y (JavaFX style) into a bottom-left y (libGDX style)
    private float toScreenY(float topY) {
        return HEIGHT - topY;
    }

    // Filled rectangle; y is the TOP edge, like the old JavaFX fillRect
    private void rect(float x, float topY, float width, float height, Color color) {
        shapes.setColor(color);
        shapes.rect(x, toScreenY(topY) - height, width, height);
    }

    // Left-aligned text; baselineY works like the y of JavaFX fillText
    private void text(String content, float x, float baselineY, float size, Color color) {
        applyFont(size, color);
        font.draw(batch, content, x, toScreenY(baselineY) + font.getCapHeight());
    }

    // Text centered horizontally on centerX
    private void textCentered(String content, float centerX, float baselineY, float size, Color color) {
        applyFont(size, color);
        float areaWidth = WIDTH;
        font.draw(batch, content, centerX - areaWidth / 2f, toScreenY(baselineY) + font.getCapHeight(),
            areaWidth, Align.center, false);
    }

    private void applyFont(float size, Color color) {
        font.getData().setScale(size / DEFAULT_FONT_SIZE);
        font.setColor(color);
    }
}
