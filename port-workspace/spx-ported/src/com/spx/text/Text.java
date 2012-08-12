package com.spx.text;import com.spx.wrapper.*;import java.util.*;import com.spx.core.*;
    public class Text
    {
        protected String _contents;
        protected Vector2 _position = new Vector2(0,0);
        protected int _textType = TextType.Inventory;

        public Text()
        { }

        public void Reset(String contents, int x, int y)
        {
            _contents = contents;
            _position.X = x;
            _position.Y = y;
        }

        public Text(String contents,int x, int y,int type)
        {
            Reset(contents, x, y);
            _textType = type;
        }
        public int Update()
        {
            return 0;
        }

        public int GetTextType()
        {
            return _textType;
        }

        protected void DrawText(SpriteBatch target)
        {
        }

        public void Draw()
        {
           
        }
    }
