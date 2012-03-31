using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;
using Microsoft.Xna.Framework.Input;
using OGUR.States;
using SPX.States;
using SPX.Core;
using SPX.Text;
using OGUR.Management;
using SPX.Sprites;

namespace OGUR
{
    public class Game : Microsoft.Xna.Framework.Game
    {
        private GraphicsDeviceManager graphics;

        public Game()
        {
            graphics = new GraphicsDeviceManager(this);
            XnaManager.SetupCamera(ref graphics,true);
            Content.RootDirectory = "Content";
        }

        protected override void Initialize()
        {
            XnaManager.SetContentManager(this.Content);
            Input.Setup(new InputInitializer());
            SpriteSheetManager.Setup(new SpriteInitializer());
            StateManager.LoadState(new GameplayState());
            base.Initialize();
        }

        protected override void LoadContent()
        {
            XnaManager.Renderer = new SpriteBatch(GraphicsDevice); ;
            StateManager.LoadContent();
            TextManager.LoadContent();
        }

        protected override void UnloadContent()
        {
            
        }

        protected override void Update(GameTime gameTime)
        {
            // Allows the game to exit
            for (int ii = 0; ii < 4;ii++)
            {
                var player = (PlayerIndex) ii;
                if (GamePad.GetState(player).Buttons.Back == ButtonState.Pressed && GamePad.GetState(player).Buttons.Start==ButtonState.Pressed)
                {
                    Exit();
                    return;
                }
            }

            Input.Update();
            StateManager.Update();
            TextManager.Update();
            base.Update(gameTime);
        }

        /// <summary>
        /// This is called when the game should draw itself.
        /// </summary>
        /// <param name="gameTime">Provides a snapshot of timing values.</param>
        protected override void Draw(GameTime gameTime)
        {
            GraphicsDevice.Clear(Color.White);
            Resolution.BeginDraw();
            XnaManager.Renderer.Begin(SpriteSortMode.FrontToBack,
                         BlendState.AlphaBlend,
                         null,
                         null,
                         null,
                         null,
                         XnaManager.GetScalingMatrix());
            StateManager.Draw();
            TextManager.Draw();
            base.Draw(gameTime);
            XnaManager.Renderer.End();
        }
    }
}