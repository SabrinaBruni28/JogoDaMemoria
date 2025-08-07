package io.github.memoriagame.Telas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import io.github.memoriagame.MemoriaGame;

/** First screen of the application. Displayed after the application is created. */
public class WinScreen implements Screen {

    final MemoriaGame game;
    /* Imagens do cenário. */
    private Texture frutasTexture;
    /* Sons do jogo. */
    private Sound winSound;

    private BitmapFont font;

    private Stage stage;
    private Skin skin;
    private TextButton nextLevelButton;

    private float frutasWidth = 250;
    private float frutasHeight = 250;

    public WinScreen(final MemoriaGame game) {
        this.game = game;

        frutasTexture = new Texture("Cenario/frutasFelizes.png");
        this.game.pauseMusic();

        winSound = Gdx.audio.newSound(Gdx.files.internal("Sons_Musics/win.mp3"));
        winSound.play(1f);

        font = new BitmapFont();
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        font.getData().setScale(3.0f);
        font.setColor(Color.BLACK);

        stage = new Stage(new ScreenViewport());

        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        nextLevelButton = new TextButton("Próximo Nível", skin);

        // Tamanho proporcional: 1/4 da largura da tela e altura fixa
        float buttonWidth = game.getWorldWidth() / 4f;
        float buttonHeight = 60;

        nextLevelButton.setSize(buttonWidth, buttonHeight);
        nextLevelButton.setPosition(
            (game.getWorldWidth() - buttonWidth) / 2, // centralizado horizontalmente
            game.getWorldHeight() * 0.15f // 15% do topo para baixo (mais acima da base)
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
                dispose();
            }
        });

        stage.addActor(nextLevelButton);

        Gdx.input.setInputProcessor(stage);
    }

    private void draw() {
        ScreenUtils.clear(Color.WHITE);
        this.game.viewport.apply();

        this.game.spritebatch.setProjectionMatrix(this.game.viewport.getCamera().combined);

        this.game.spritebatch.begin();

        this.game.spritebatch.draw(this.game.backgroundTexture, 0, 0, this.game.getWorldWidth(), this.game.getWorldHeight());
        float imgX = (game.viewport.getWorldWidth() - frutasWidth) / 2f;
        float imgY = game.viewport.getWorldHeight() / 2f - frutasHeight / 2f;  // centro vertical

        game.spritebatch.draw(frutasTexture, imgX, imgY, frutasWidth, frutasHeight);

        // Centralizar o texto “Você ganhou!!!”
        String winText = "Você ganhou!!!";
        float winTextX = (game.getWorldWidth() - font.getData().getGlyph(' ').width * winText.length()) / 2f;

        // Melhor maneira: medir texto com BitmapFont layout
        com.badlogic.gdx.graphics.g2d.GlyphLayout layout = new com.badlogic.gdx.graphics.g2d.GlyphLayout(font, winText);
        winTextX = (game.getWorldWidth() - layout.width) / 2;

        font.draw(game.spritebatch, winText, winTextX, game.getWorldHeight() * 0.85f);

        this.game.spritebatch.end();
    }

    @Override
    public void render(float delta) {
        draw();
        // input() não é mais necessário, porque a interação é via Stage
        logic();

        // Atualiza e desenha o stage com o botão
        stage.act(delta);
        stage.draw();
    }


    @Override
    public String toString() {
        return "WinScreen"; // Identificador da tela
    }

    @Override
    public void show() {
        // Prepare your screen here.
    }

    private void logic() {
        /* */
    }

    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height, true);
        stage.getViewport().update(width, height, true);

        // Atualiza tamanho da imagem
        frutasWidth = width * 0.25f;
        frutasHeight = frutasWidth;

        // Reposiciona botão
        float buttonWidth = game.getWorldWidth() / 4f;
        float buttonHeight = 60;
        nextLevelButton.setSize(buttonWidth, buttonHeight);
        nextLevelButton.setPosition(
            (width - buttonWidth) / 2, // centralizado horizontalmente
            game.getWorldHeight() * 0.15f // 15% do topo para baixo (mais acima da base)
        );
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
    }

    @Override
    public void dispose() {
        frutasTexture.dispose();
        winSound.dispose();
        font.dispose();
        stage.dispose();
        skin.dispose();
    }
}