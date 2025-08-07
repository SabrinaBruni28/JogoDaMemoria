package io.github.memoriagame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import io.github.memoriagame.Telas.MenuScreen;

public class MemoriaGame extends Game {

    // Renderização e fonte
    public SpriteBatch spritebatch;
    public BitmapFont font;

    // Controle de coordenadas e tamanho de janela
    public ScreenViewport viewport;
    public Vector2 touchPos;

    // Música e imagem de fundo
    private Music music;
    public Texture backgroundTexture;

    // UI compartilhada entre telas
    public Stage stage;
    public Skin skin;

    // Controle da partida e níveis
    private int nivel;
    private int qntNivel;
    private Partida partida;

    @Override
    public void create() {
        // Inicializa estado do jogo
        this.nivel = 1;
        this.qntNivel = 5;

        // Elementos gráficos
        spritebatch = new SpriteBatch();

        font = new BitmapFont();
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        font.getData().setScale(3.0f);
        font.setColor(Color.BLACK);

        // Viewport e coordenadas
        viewport = new ScreenViewport();
        viewport.getCamera().position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
        touchPos = new Vector2();

        // Fundo do jogo
        backgroundTexture = new Texture("Cenario/background.jpg");

        // Stage e Skin compartilhados entre todas as telas
        stage = new Stage(viewport, spritebatch);
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        // Música de fundo
        music = Gdx.audio.newMusic(Gdx.files.internal("Sons_Musics/musicgame.mp3"));
        music.setLooping(true);
        music.setVolume(0.5f);
        music.play();

        // Tela inicial
        setScreen(new MenuScreen(this));
    }

    @Override
    public void render() {
        super.render(); // LibGDX gerencia a tela atual
    }

    @Override
    public void dispose() {
        // Recursos gráficos
        spritebatch.dispose();
        font.dispose();
        backgroundTexture.dispose();
        music.dispose();

        // UI
        if (stage != null) stage.dispose();
        if (skin != null) skin.dispose();

        // Libera tela atual e memória
        if (getScreen() != null) getScreen().dispose();
        super.dispose();
        System.gc();
    }

    // Música
    public void playMusic() {
        music.play();
    }

    public void pauseMusic() {
        music.pause();
    }

    // Acesso à UI global
    public Stage getStage() {
        return stage;
    }

    public Skin getSkin() {
        return skin;
    }

    // Acesso à viewport
    public float getWorldWidth() {
        return viewport.getWorldWidth();
    }

    public float getWorldHeight() {
        return viewport.getWorldHeight();
    }

    // Controle de níveis
    public int proximoNivel() {
        if (nivel == qntNivel) {
            this.nivel = 1;
        } else {
            this.nivel += 1;
        }
        return nivel;
    }

    public int whichProximoNivel() {
        return (nivel == qntNivel) ? 1 : nivel + 1;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    // Partida
    public Partida getPartida() {
        return partida;
    }

    public void setPartida(Partida partida) {
        this.partida = partida;
    }

    public int getQntNivel() {
        return qntNivel;
    }

    public void setScaleFont(float scale) {
        font.getData().setScale(scale);
    }

    public void resetScaleFont() {
        font.getData().setScale(3.0f);
    }

    public void clearStage() {
        if (stage != null) {
            stage.clear();
        }
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public String toString() {
        return "PrincipalScreen";
    }
}
