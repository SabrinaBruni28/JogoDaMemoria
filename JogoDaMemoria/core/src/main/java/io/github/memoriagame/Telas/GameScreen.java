package io.github.memoriagame.Telas;

import java.util.ArrayList;
import java.util.Collections;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import io.github.memoriagame.Card;
import io.github.memoriagame.JsonLoader;
import io.github.memoriagame.MemoriaGame;

/** First screen of the application. Displayed after the application is created. */
public class GameScreen implements Screen {
    
    final MemoriaGame game;

    private Sound clickSound, erroSound, acertoSound;
    private Texture cardBack;
    private BitmapFont font;

    private ArrayList<Texture> frutasTextures;
    private ArrayList<String> imagens;
    private Array<Card> cards;
    private Vector2 touchPos;
    private ShapeRenderer shapeRenderer;

    private int rows, cols;
    private float cardWidth, cardHeight, spacingX, spacingY;

    private long tempoInicial, tempoDecorrido;
    private float segundosDecorridos, tempoLimite;

    public GameScreen(final MemoriaGame game) {
        this.game = game;
        // Variáveis simples iniciais
        rows = 2 + this.game.getNivel();
        cols = 3 + this.game.getNivel();
        tempoLimite = 30.0f * this.game.getNivel();
        segundosDecorridos = tempoLimite;

        cards = new Array<>();
        frutasTextures = new ArrayList<>();
        imagens = new ArrayList<>();
        touchPos = new Vector2();

        font = new BitmapFont();
        font.getData().setScale(2.5f);
        font.setColor(Color.BLACK);
        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void show() {
        // Registra o tempo inicial quando a tela aparece
        tempoInicial = TimeUtils.millis();

        float paddingX = 20;
        float paddingTop = 70;   // espaço maior para os textos
        float paddingBottom = 40; // pode ser menor

        // Calcula tamanho das cartas e espaçamento
        float maxCardWidth = (game.getWorldWidth() - paddingX * 2) / (cols + 1);
        float maxCardHeight = (game.getWorldHeight() - paddingTop - paddingBottom) / (rows + 1);
        cardWidth = Math.min(maxCardWidth, maxCardHeight);
        cardHeight = cardWidth;

        float totalWidth = cols * cardWidth;
        float totalHeight = rows * cardHeight;

        spacingX = (game.getWorldWidth() - totalWidth) / (cols + 1);
        spacingY = (game.getWorldHeight() - totalHeight - paddingTop - paddingBottom) / (rows + 1);

        // Carregar sons
        acertoSound = Gdx.audio.newSound(Gdx.files.internal("Sons_Musics/acerto.mp3"));
        erroSound = Gdx.audio.newSound(Gdx.files.internal("Sons_Musics/error.mp3"));
        clickSound = Gdx.audio.newSound(Gdx.files.internal("Sons_Musics/click.mp3"));

        // Carregar textura do verso da carta
        cardBack = new Texture("Cartas/card_back.png");

        // Carregar imagens das frutas do JSON
        imagens = JsonLoader.carregarFrutas();

        // Criar Texturas frontais e duplicar para pares
        frutasTextures.clear();
        int qtdCartas = (rows * cols) / 2;
        for (int i = 0; i < qtdCartas; i++) {
            int idx = i % imagens.size();
            frutasTextures.add(new Texture(imagens.get(idx)));
        }
        frutasTextures.addAll(new ArrayList<>(frutasTextures));
        Collections.shuffle(frutasTextures);

        // Criar cartas e posicionar
        cards.clear();
        int index = 0;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                float x = spacingX + col * (cardWidth + spacingX);
                float y = paddingBottom + spacingY + row * (cardHeight + spacingY);
                Texture frontTexture = frutasTextures.get(index);
                cards.add(new Card(x, y, cardWidth, cardHeight, cardBack, frontTexture));
                index++;
            }
        }
    }

    @Override
    public void render(float delta) {
        draw();
        input();
        logic();
    }

    @Override
    public String toString() {
        return "GameScreen"; // Identificador da tela
    }

    private void input() {
       /* Faz alguma coisa quando clica ou toca a tela. */
       if (Gdx.input.isTouched()) { // If the user has clicked or tapped the screen
            /* Pega onde o toque aconteceu na tela. */
            touchPos.set(Gdx.input.getX(), Gdx.input.getY()); 
            /* Converte as unidades da janela para as coordenadas do cenário. */
            this.game.viewport.unproject(touchPos);

           /* Verifica se uma carta foi clicada. */
           for (Card card : cards) {
                if (card.bounds.contains(touchPos.x, touchPos.y) && !card.isFlipping()) {
                    /* Gira a carta. */
                    if (card.flip()) clickSound.play(); 
                }
            }
        }
    }

    private void logic() {
        /* Confere se o jogo terminou. */
        isWinOrOver();

        // Calcula o tempo decorrido
        tempoDecorrido = TimeUtils.millis() - tempoInicial;

        // Converte para segundos
        float tempoPassado = tempoDecorrido / 1000f;

        // Calcula o tempo restante
        segundosDecorridos = Math.max(0, tempoLimite - tempoPassado);

        Array<Card> flippedCards = getFlippedCards();
        
        if (flippedCards != null) {
            /* Pega as cartas viradas. */
            Card firstCard = flippedCards.get(0);
            Card secondCard = flippedCards.get(1);

            /* Verifica se as cartas combinam. */
            if (firstCard.getFrontTexture() == secondCard.getFrontTexture()) {
                acertoSound.play(0.8f);
                /* Se as cartas combinam elas são fixadas. */
                firstCard.setFixa(true);
                secondCard.setFixa(true);
            } 
            else {
                erroSound.play(0.8f);
                /* Se as cartas não combinam, elas são viradas de volta. */
                firstCard.flip();
                secondCard.flip();
            }
        }  
    }

    private void draw() {
        /* Limpa a tela. */
        ScreenUtils.clear(Color.WHITE);
        this.game.viewport.apply();
    
        // Define a matriz de projeção do SpriteBatch
        this.game.spritebatch.setProjectionMatrix(this.game.viewport.getCamera().combined);

        // Inicia o desenho
        this.game.spritebatch.begin(); // Use o spriteBatch, não game.batch
            
            /* Desenha o background. */
            this.game.spritebatch.draw(this.game.backgroundTexture, 0, 0, this.game.getWorldWidth(), this.game.getWorldHeight());
           
            // Desenha as cartas e atualiza a animação de virar
            for (Card card : cards) {
                card.update(Gdx.graphics.getDeltaTime());
                card.draw(this.game.spritebatch);
               
            }

        // Fim do spriteBatch, pois ShapeRenderer precisa fora
        game.spritebatch.end();

        // --- DESENHAR FUNDO COM SHAPERENDERER ---
        shapeRenderer.setProjectionMatrix(game.viewport.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.WHITE);

        // Fundo do texto "Nível"
        float nivelX = 30;
        float nivelY = game.getWorldHeight() - 60;
        float textoAltura = 50;
        float textoLargura = 150;
        shapeRenderer.rect(nivelX, nivelY, textoLargura, textoAltura);

        // Fundo do texto "Tempo"
        float tempoX = game.getWorldWidth() - 220;
        float tempoY = game.getWorldHeight() - 60;
        textoAltura = 50;
        textoLargura = 200;
        shapeRenderer.rect(tempoX, tempoY, textoLargura, textoAltura);

        shapeRenderer.end();

        // Volta para desenhar texto
        game.spritebatch.begin();

        // Texto por cima do fundo
        font.draw(game.spritebatch, "Nível: " + game.getNivel(), nivelX + 10, nivelY + 40);
        font.draw(game.spritebatch, "Tempo: " + (int) segundosDecorridos + "s", tempoX + 10, tempoY + 40);
        // Termina o desenho
        this.game.spritebatch.end();
    }

    public Array<Card> getFlippedCards() {
        Array<Card> flippedCards = new Array<>();

        /* Pega as cartas que estão viradas para frente. */
        for (Card card : cards) {
            /* Analisa as que não estão girando e nem fixa. */
            if (card.isFlipped() && !card.isFlipping() && !card.isFixa()) {
                flippedCards.add(card);
            }
        }

        // Retorna as duas cartas viradas.
        if (flippedCards.size == 2) {
            return flippedCards;
        } 
        else {
            return null; // Retorna null se não houver duas cartas viradas
        }
    }

    public void isWinOrOver() {
        int count = 0;
        /* Confere se todas as cartas estão fixas. */
        for (Card card : cards) {
            if (!card.isFixa()) {
                count++;
            }
        }
        /* Se todas as cartas estiverem fixas o jogo terminou e o usuário ganhou. */
        if (count == 0) {
            this.game.getPartida().setTempo(this.game.getNivel()-1, segundosDecorridos);
            /* Muda para a próxima tela. */
			this.game.setScreen(new WinScreen(this.game));
			dispose();
        }

        /* Se o tempo limite for excedido. */
        if (segundosDecorridos <= 0f) {
            this.game.getPartida().setTempo(this.game.getNivel()-1, segundosDecorridos);
            /* Muda para a próxima tela. */
			this.game.setScreen(new OverScreen(this.game));
			dispose();
        }
        /* Caso contrário o jogo continua. */
        return;
    }

    @Override
    public void resize(int width, int height) {
        this.game.viewport.update(width, height, true); // true centers the camera
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
        for (Texture t : frutasTextures) t.dispose();
        for (Card c : cards) c.dispose();
        cardBack.dispose();
        clickSound.dispose();
        erroSound.dispose();
        acertoSound.dispose();
        font.dispose();
        shapeRenderer.dispose();
    }
}