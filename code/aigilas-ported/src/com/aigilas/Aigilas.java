package com.aigilas;
    public class Aigilas extends  com.xna.wrapper.Game
    {
        private GraphicsDeviceManager graphics;

        public Aigilas()
        {
            this.TargetElapsedTime = TimeSpan.FromSeconds(1.0f / 60.0f);
            Client.Get();
            graphics = new GraphicsDeviceManager(this);
            XnaManager.SetupCamera(graphics,false);
            Content.RootDirectory = "Content";
        }

@Override
        {
            XnaManager.SetContentManager(this.Content);
            Input.Setup(new InputInitializer());
            SpriteSheetManager.Setup(new SpriteInitializer());
            StateManager.LoadState(new MainMenuState());
            ParticleEngine.Reset();
            super.Initialize();
        }

@Override
        {
            XnaManager.Renderer = new SpriteBatch(GraphicsDevice);
            StateManager.LoadContent();
            TextManager.LoadContent();
            //$$$MediaPlayer.Play(Content.Load<Song>("MainTheme"));
            MediaPlayer.IsRepeating = true;
        }

@Override
        {
            
        }

@Override
        {
            Input.Update();
            if (Input.DetectState(Commands.ToggleDevConsole, Client.Get().GetFirstPlayerIndex()))
            {
                DevConsole.Get().Toggle();
            }
            if (Client.Get().NextTurn())
            {
                for (int ii = 0; ii < 4; ii++)
                {
                    PlayerIndex player = PlayerIndex.values()[ii];
                    if (GamePad.GetState(player).IsPressed(Buttons.Back) && GamePad.GetState(player).IsPressed(Buttons.Start))
                    {
                        SetIsRunning(false);
                    }
                }
                ParticleEngine.Update();
                StateManager.Update();
                TextManager.Update();
                super.Update(gameTime);
                Client.Get().PrepareForNextTurn();
            }
            else
            {
                Client.Get().HeartBeat();
            }
        }

@Override
        {
            GraphicsDevice.clear(Color.White);
            Resolution.BeginDraw();
            XnaManager.Renderer.Begin(SpriteSortMode.FrontToBack,
                         BlendState.AlphaBlend,
                         null,
                         null,
                         null,
                         null,
                         XnaManager.GetScalingMatrix());
            StateManager.Draw();
            ParticleEngine.Draw();
            TextManager.Draw();
            DevConsole.Get().Draw();
            super.Draw(gameTime);
            XnaManager.Renderer.End();
        }
    }