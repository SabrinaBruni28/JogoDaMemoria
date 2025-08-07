package io.github.memoriagame.Telas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

import io.github.memoriagame.MemoriaGame;
import io.github.memoriagame.Partida;

public class MenuScreen implements Screen {

    final MemoriaGame game;

    private Texture frutasTexture;

    private Stage stage;
    private Skin skin;

    private TextButton iniciarButton;
    private Label tituloLabel;

    private float frutasWidth = 250;
    private float frutasHeight = 250;

    public MenuScreen(final MemoriaGame game) {
        this.game = game;

        game.setNivel(1);
        game.setPartida(new Partida(game.getQntNivel()));

        frutasTexture = new Texture("Cenario/frutasFelizes.png");

        // Carrega skin padrão LibGDX
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        stage = new Stage(game.viewport);
        Gdx.input.setInputProcessor(stage);

        // Título
        tituloLabel = new Label("Jogo da Memória", skin);
        tituloLabel.setFontScale(4f);
        tituloLabel.setColor(Color.BLACK);
        tituloLabel.pack(); // Atualiza tamanho com base na nova escala de fonte
        tituloLabel.setPosition(
            (game.viewport.getWorldWidth() - tituloLabel.getWidth()) / 2f,
            game.viewport.getWorldHeight() - tituloLabel.getHeight() - 100
        );
        stage.addActor(tituloLabel);

        // Botão iniciar
        iniciarButton = new TextButton("Iniciar Jogo", skin);
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
                dispose();
            }
        });
        stage.addActor(iniciarButton);
    }

    @Override
    public String toString() {
        return "MenuScreen";
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.WHITE);
        game.viewport.apply();
        game.spritebatch.setProjectionMatrix(game.viewport.getCamera().combined);

        game.spritebatch.begin();
            game.spritebatch.draw(game.backgroundTexture, 0, 0, game.getWorldWidth(), game.getWorldHeight());
            float imgX = (game.viewport.getWorldWidth() - frutasWidth) / 2f;
            float imgY = game.viewport.getWorldHeight() / 2f - frutasHeight / 2f;  // centro vertical

            game.spritebatch.draw(frutasTexture, imgX, imgY, frutasWidth, frutasHeight);

        game.spritebatch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height, true);
        stage.getViewport().update(width, height, true);

        // Atualiza tamanho da imagem
        frutasWidth = width * 0.25f;
        frutasHeight = frutasWidth;

        // Reposiciona título
        tituloLabel.pack();
        tituloLabel.setPosition(
            (width - tituloLabel.getWidth()) / 2f,
            game.viewport.getWorldHeight() - tituloLabel.getHeight() - 100
        );

        // Reposiciona botão
        iniciarButton.setPosition((width - 200) / 2f, 100);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        frutasTexture.dispose();
        stage.dispose();
        skin.dispose();
    }
}
