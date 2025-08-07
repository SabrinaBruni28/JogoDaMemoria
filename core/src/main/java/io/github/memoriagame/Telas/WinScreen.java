package io.github.memoriagame.Telas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

import io.github.memoriagame.MemoriaGame;

public class WinScreen implements Screen {

    private final MemoriaGame game;

    private Texture frutasTexture;
    private Sound winSound;
    private TextButton nextLevelButton;

    private float frutasWidth = 250;
    private float frutasHeight = 250;

    public WinScreen(final MemoriaGame game) {
        this.game = game;

        frutasTexture = new Texture("Cenario/frutasFelizes.png");

        game.pauseMusic();
        winSound = Gdx.audio.newSound(Gdx.files.internal("Sons_Musics/win.mp3"));
        winSound.play(1f);

        game.stage.clear(); // limpa atores anteriores

        Gdx.input.setInputProcessor(game.stage);

        // Botão "Próximo Nível"
        nextLevelButton = new TextButton("Próximo Nível", game.skin);

        float buttonWidth = game.getWorldWidth() / 4f;
        float buttonHeight = 60;

        nextLevelButton.setSize(buttonWidth, buttonHeight);
        nextLevelButton.setPosition(
            (game.getWorldWidth() - buttonWidth) / 2f,
            game.getWorldHeight() * 0.15f
        );

        nextLevelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.proximoNivel();

                if (game.getNivel() == 1) {
                    game.setScreen(new MenuScreen(game));
                } else {
                    game.setScreen(new GameScreen(game));
                }

                game.playMusic();
            }
        });

        game.stage.addActor(nextLevelButton);
    }

    private void draw() {
        ScreenUtils.clear(Color.WHITE);
        game.viewport.apply();
        game.spritebatch.setProjectionMatrix(game.viewport.getCamera().combined);

        game.spritebatch.begin();
        game.spritebatch.draw(game.backgroundTexture, 0, 0, game.getWorldWidth(), game.getWorldHeight());

        float imgX = (game.viewport.getWorldWidth() - frutasWidth) / 2f;
        float imgY = game.viewport.getWorldHeight() / 2f - frutasHeight / 2f;
        game.spritebatch.draw(frutasTexture, imgX, imgY, frutasWidth, frutasHeight);

        // Texto centralizado
        String winText = "Você ganhou!!!";
        GlyphLayout layout = new GlyphLayout(game.font, winText);
        float winTextX = (game.getWorldWidth() - layout.width) / 2f;
        float winTextY = game.getWorldHeight() * 0.85f;

        game.font.draw(game.spritebatch, layout, winTextX, winTextY);

        game.spritebatch.end();
    }

    @Override
    public void render(float delta) {
        draw();
        game.stage.act(delta);
        game.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height, true);
        game.stage.getViewport().update(width, height, true);

        frutasWidth = width * 0.25f;
        frutasHeight = frutasWidth;

        float buttonWidth = game.getWorldWidth() / 4f;
        float buttonHeight = 60;
        nextLevelButton.setSize(buttonWidth, buttonHeight);
        nextLevelButton.setPosition(
            (game.getWorldWidth() - buttonWidth) / 2f,
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
        winSound.dispose();
        game.clearStage();
    }

    @Override
    public String toString() {
        return "WinScreen";
    }
}
