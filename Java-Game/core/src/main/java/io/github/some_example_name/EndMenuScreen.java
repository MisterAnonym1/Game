package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

public class EndMenuScreen implements Screen {
    private final Main main;
    private SpriteBatch batch;
    private BitmapFont font;
    private Stage stage;
    private Skin skin;
    private ParticleEffect confetti;
    private String playtime;

    public EndMenuScreen(Main main) {
        this.main = main;
        batch = new SpriteBatch();
        font = new BitmapFont(Gdx.files.internal("ui/font-window.fnt"));
        stage = new Stage();
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        playtime = DataCenter.getformatedTimeplayed();

        // Konfetti-Effekt
        confetti = new ParticleEffect();
        confetti.load(Gdx.files.internal("confetti.p"), Gdx.files.internal(""));
        confetti.setPosition(Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight());
        confetti.start();

        // Nochmal spielen Button
        TextButton retry = new TextButton("Nochmal spielen", skin);
        retry.setPosition(Gdx.graphics.getWidth()/2f - 100, 120);retry.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
               /// main.restartGame();
            }
        });
        stage.addActor(retry);

        // Beenden Button
        TextButton exit = new TextButton("Beenden", skin);
        exit.setPosition(Gdx.graphics.getWidth()/2f - 100, 60);
        exit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        stage.addActor(exit);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        confetti.update(delta);
        batch.begin();
        confetti.draw(batch);
        font.getData().setScale(2f);
        font.draw(batch, "Glückwunsch!", Gdx.graphics.getWidth()/2f - 180, Gdx.graphics.getHeight() - 100);
        font.getData().setScale(1.2f);
        font.draw(batch, "Spielzeit: " + playtime, Gdx.graphics.getWidth()/2f - 120, Gdx.graphics.getHeight()/2f);
        batch.end();
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {}
    @Override
    public void pause() {}
    @Override
    public void resume() {}
    @Override
    public void hide() {}
    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        stage.dispose();
        confetti.dispose();
        skin.dispose();
    }
}

