package io.github.memoriagame.Telas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

import io.github.memoriagame.MemoriaGame;
import io.github.memoriagame.Partida;

public class MenuScreen implements Screen {

    private final MemoriaGame game;

    private Texture frutasTexture;
    private TextButton iniciarButton;
    private Label tituloLabel;

    private float frutasWidth = 250;
    private float frutasHeight = 250;

    public MenuScreen(final MemoriaGame game) {
        this.game = game;

        game.setNivel(1);
        game.setPartida(new Partida(game.getQntNivel()));

        frutasTexture = new Texture("Cenario/frutasFelizes.png");

        // Limpa o stage antes de adicionar novos elementos
        game.stage.clear();

        // Define input do stage
        Gdx.input.setInputProcessor(game.stage);

        // Título
        tituloLabel = new Label("Jogo da Memória", game.skin);
        tituloLabel.setFontScale(4f);
        tituloLabel.setColor(Color.BLACK);
        tituloLabel.pack();
        tituloLabel.setPosition(
            (game.viewport.getWorldWidth() - tituloLabel.getWidth()) / 2f,
            game.viewport.getWorldHeight() - tituloLabel.getHeight() - 100
        );
        game.stage.addActor(tituloLabel);

        // Botão iniciar
        iniciarButton = new TextButton("Iniciar Jogo", game.skin);
        iniciarButton.setSize(200, 60);
        iniciarButton.setPosition(
            (game.getWorldWidth() - 200) / 2f,
            100
        );
        iniciarButton.getLabel().setFontScale(1.5f);
        iniciarButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game));
            }
        });
        game.stage.addActor(iniciarButton);
    }

    @Override
    public void show() {
        // Opcional: resetar input ou estado do stage aqui se necessário
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.WHITE);
        game.viewport.apply();
        game.spritebatch.setProjectionMatrix(game.viewport.getCamera().combined);

        game.spritebatch.begin();
            game.spritebatch.draw(game.backgroundTexture, 0, 0, game.getWorldWidth(), game.getWorldHeight());

            float imgX = (game.viewport.getWorldWidth() - frutasWidth) / 2f;
            float imgY = game.viewport.getWorldHeight() / 2f - frutasHeight / 2f;
            game.spritebatch.draw(frutasTexture, imgX, imgY, frutasWidth, frutasHeight);
        game.spritebatch.end();

        game.stage.act(delta);
        game.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height, true);
        game.stage.getViewport().update(width, height, true);

        frutasWidth = width * 0.25f;
        frutasHeight = frutasWidth;

        tituloLabel.pack();
        tituloLabel.setPosition(
            (game.getWorldWidth() - tituloLabel.getWidth()) / 2f,
            game.getWorldHeight() - tituloLabel.getHeight() - 100
        );

        iniciarButton.setPosition((game.getWorldWidth() - 200) / 2f, 100);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        frutasTexture.dispose();
        game.clearStage();
    }

    @Override
    public String toString() {
        return "MenuScreen";
    }
}
