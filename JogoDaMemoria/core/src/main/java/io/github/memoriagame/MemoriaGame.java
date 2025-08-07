package io.github.memoriagame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import io.github.memoriagame.Telas.MenuScreen;

/* 
Adicionar:
    V* Botões
    * Gifs de fogos de artifício quando ganha
    * Gifs de chuva quando perde
    V* Tempo para perder
    V* Níveis diferentes
    V* Melhor fonte de escrita
    * Como uso o show() e o hide()
    * Imagem no .jar
    * Imagem no app aberto
    V* Caixa de texto
    * Como manter a caixa quando muda o tamanho da janela.
    
Em um novo programa:
    * Imagens na frente
    * Nome com imagem atrás
    * Descrição destacada
    * Cartas selecionadas destacadas
    * Caixa de seleção
*/
/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class MemoriaGame extends Game {

    /* Cenário para as imagens. */
    public SpriteBatch spritebatch;
    
	/* Define as proporções da janela. */
    public ScreenViewport viewport;
    /* Para cálculo de coordenadas. */
    public Vector2 touchPos;

    /* Música do jogo. */
    private Music music;
	/* Imagens */
    public Texture backgroundTexture;

    /* Atributos da partida. */
    private int nivel;
    private int qntNivel;

    private Partida partida;

    @Override
    public void create() {
        /* Define os níveis. */
        this.nivel = 1;
        this.qntNivel = 3;
        
        /* Inicializa os cenários. */
        spritebatch = new SpriteBatch();
		
        /* Inicializa e define o tamanho do cenário. */
		viewport = new ScreenViewport();
        /* Centraliza o cenário. */
		viewport.getCamera().position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0); 
        /* Inicialização do vetor de coordenadas. */
        touchPos = new Vector2();
		/* Inicialização das imagens */
        backgroundTexture = new Texture("Cenario/background.jpg");
        
        /* Inicializa a música do jogo. */
        music = Gdx.audio.newMusic(Gdx.files.internal("Sons_Musics/musicgame.mp3"));
        
        /* Para tocar a música em loop no inicio do jogo. */
        music.setLooping(true);
        /* Define o voluma da música. */
        music.setVolume(.5f);
        /* Coloca a música para tocar. */
        music.play();
        
        /* Mudando para a primeira tela. */
        setScreen(new MenuScreen(this));
    }
    
    public void render() {
        super.render(); // important!
	}
    
    @Override
    public String toString() {
        return "PrincipalScreen"; // Identificador da tela
    }

    public float getWorldWidth() {
        return viewport.getWorldWidth();
    }

    public float getWorldHeight() {
        return viewport.getWorldHeight();
    }
    
    public void dispose() {
        /* Exclui os assets quando o programa é fechado. */
		this.spritebatch.dispose();
		this.backgroundTexture.dispose();
        this.music.dispose();

        super.dispose();
        
        /* Exclui os assets da tela atualmente aberta. */
        if (getScreen() != null) getScreen().dispose();
        
        /* Faz coleta de lixo. */
        System.gc();
	}

    public void playMusic(){
        music.play();
    }

    public void pauseMusic(){
        music.pause();
    }

    public int proximoNivel(){
        if (nivel == qntNivel) this.nivel = 1;
        else this.nivel += 1;
        return nivel;
    }

    public int whichProximoNivel(){
        if (nivel == qntNivel) return 1;
        else return nivel + 1;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel){
        this.nivel = nivel;
    }

    public Partida getPartida() {
        return partida;
    }

    public void setPartida(Partida partida) {
        this.partida = partida;
    }

    public int getQntNivel() {
        return qntNivel;
    }
}