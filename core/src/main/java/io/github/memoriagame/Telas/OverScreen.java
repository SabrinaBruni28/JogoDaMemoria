package io.github.memoriagame.Telas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;

import io.github.memoriagame.MemoriaGame;

public class OverScreen implements Screen {

    final MemoriaGame game;

    private Texture frutasTexture;
    private Sound overSound;
    private TextButton tryAgainButton;

    private float frutasWidth = 250;
    private float frutasHeight = 250;

    public OverScreen(final MemoriaGame game) {
        this.game = game;

        frutasTexture = new Texture("Cenario/frutasTristes.png");
        game.pauseMusic();

        overSound = Gdx.audio.newSound(Gdx.files.internal("Sons_Musics/over.mp3"));
        overSound.play(1f);

        game.stage.clear(); // Remove qualquer ator anterior

        tryAgainButton = new TextButton("Tentar Novamente", game.skin);

        float buttonWidth = game.getWorldWidth() / 4f;
        float buttonHeight = 60;

        tryAgainButton.setSize(buttonWidth, buttonHeight);
        tryAgainButton.setPosition(
            (game.getWorldWidth() - buttonWidth) / 2,
            game.getWorldHeight() * 0.15f
        );

        tryAgainButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game));
                game.playMusic();
                dispose();
            }
        });

        game.stage.addActor(tryAgainButton);
        Gdx.input.setInputProcessor(game.stage);
    }

    @Override
    public void render(float delta) {
        draw();
        game.stage.act(delta);
        game.stage.draw();
    }

    private void draw() {
        ScreenUtils.clear(Color.WHITE);
        game.viewport.apply();
        game.spritebatch.setProjectionMatrix(game.viewport.getCamera().combined);

        game.spritebatch.begin();

        game.spritebatch.draw(game.backgroundTexture, 0, 0, game.getWorldWidth(), game.getWorldHeight());

        float imgX = (game.getWorldWidth() - frutasWidth) / 2f;
        float imgY = (game.getWorldHeight() - frutasHeight) / 2f;
        game.spritebatch.draw(frutasTexture, imgX, imgY, frutasWidth, frutasHeight);

        String overText = "Você perdeu! :(";
        GlyphLayout layout = new GlyphLayout(game.font, overText);
        float textX = (game.getWorldWidth() - layout.width) / 2;
        float textY = game.getWorldHeight() * 0.85f;

        game.font.draw(game.spritebatch, layout, textX, textY);

        game.spritebatch.end();
    }

    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height, true);
        game.stage.getViewport().update(width, height, true);

        frutasWidth = width * 0.25f;
        frutasHeight = frutasWidth;

        float buttonWidth = game.getWorldWidth() / 4f;
        float buttonHeight = 60;
        tryAgainButton.setSize(buttonWidth, buttonHeight);
        tryAgainButton.setPosition(
            (game.getWorldWidth() - buttonWidth) / 2,
            game.getWorldHeight() * 0.15f
        );
    }

    @Override public void show() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        frutasTexture.dispose();
        overSound.dispose();
        game.clearStage();
    }

    @Override
    public String toString() {
        return "OverScreen";
    }
}
