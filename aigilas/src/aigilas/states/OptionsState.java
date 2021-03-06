package aigilas.states;

import aigilas.Aigilas;
import aigilas.net.Client;
import aigilas.ui.SelectableButton;
import aigilas.ui.UiAssets;
import aigilas.ui.UiSelection;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import sps.bridge.Commands;
import sps.bridge.Contexts;
import sps.bridge.Sps;
import sps.graphics.FrameStrategy;
import sps.graphics.RenderStrategy;
import sps.graphics.Renderer;
import sps.graphics.StretchStrategy;
import sps.io.Input;
import sps.states.State;
import sps.states.StateManager;

import java.util.ArrayList;

public class OptionsState implements State {
    private int _selection = 0;
    private final int horizDelta = 1;
    private final int verticalDelta = 3;

    private Stage stage;
    private ArrayList<SelectableButton> buttons = new ArrayList<SelectableButton>();

    public OptionsState() {

        Input.setContext(Contexts.get(Sps.Contexts.Non_Free), Client.get().getFirstPlayerIndex());
        stage = new Stage();

        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);

        final SelectableButton fullScreenBtn = new SelectableButton("Toggle Fullscreen", UiAssets.getButtonStyle());
        fullScreenBtn.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                toggleFullScreen();
            }
        });

        final SelectableButton renderStratBtn = new SelectableButton("Toggle Fixed Aspect Ratio", UiAssets.getButtonStyle());
        renderStratBtn.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                toggleRenderStrategy();
            }
        });

        final SelectableButton backBtn = new SelectableButton("Main Menu", UiAssets.getButtonStyle());
        backBtn.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                StateManager.loadState(new MainMenuState());
            }
        });

        table.add(fullScreenBtn);
        buttons.add(fullScreenBtn);
        table.row();
        table.add(renderStratBtn);
        buttons.add(renderStratBtn);
        table.row();
        table.add(backBtn);
        buttons.add(backBtn);


        stage.addActor(table);
    }

    private void toggleFullScreen() {
        Renderer.get().toggleFullScreen();
        StateManager.loadState(new OptionsState());
    }

    private RenderStrategy current;

    private void toggleRenderStrategy() {
        if (current == null || current.getClass() == FrameStrategy.class) {
            current = new StretchStrategy();
        }
        else {
            current = new FrameStrategy();
        }
        Renderer.get().setStrategy(current);
    }

    @Override
    public void update() {
        int selectionVelocity = (Input.isActive(Commands.get(Aigilas.Commands.MoveRight), Client.get().getFirstPlayerIndex()) ? horizDelta : 0) + (Input.isActive(Commands.get(Aigilas.Commands.MoveLeft), Client.get().getFirstPlayerIndex()) ? -horizDelta : 0);
        selectionVelocity += (Input.isActive(Commands.get(Aigilas.Commands.MoveUp), Client.get().getFirstPlayerIndex()) ? -verticalDelta : 0) + (Input.isActive(Commands.get(Aigilas.Commands.MoveDown), Client.get().getFirstPlayerIndex()) ? verticalDelta : 0);
        _selection += selectionVelocity;
        _selection %= buttons.size();
        if (_selection < 0) {
            _selection = buttons.size() - 1;
        }

        if (Client.get().isGameStarting()) {
            for (int ii = 0; ii < Client.get().getPlayerCount(); ii++) {
                Input.setContext(Contexts.get(Sps.Contexts.Free), ii);
            }

            StateManager.loadState(new LoadingState());
        }
        else {
            if (UiSelection.commandActive()) {
                switch (_selection) {
                    case 0:
                        toggleFullScreen();
                        return;
                    default:
                        break;
                }
            }
        }

        if (selectionVelocity != 0) {
            for (int ii = 0; ii < buttons.size(); ii++) {
                buttons.get(ii).setSelected(false);
            }
            buttons.get(_selection).setSelected(true);
        }
        boolean reset = false;
        for (int ii = 0; ii < buttons.size(); ii++) {
            if (buttons.get(ii).mouseIsOver() && _selection != ii) {
                _selection = ii;
                reset = true;
            }
        }
        if (reset) {
            for (int ii = 0; ii < buttons.size(); ii++) {
                buttons.get(ii).setSelected(_selection == ii);
            }
        }
    }

    @Override
    public void load() {

    }

    @Override
    public void unload() {

    }

    @Override
    public void draw() {
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }
}
