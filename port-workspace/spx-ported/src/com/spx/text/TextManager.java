package com.spx.text;import com.spx.wrapper.*;import java.util.*;import com.spx.core.*;    public class TextManager
    {
        static private SpriteFont actionFont;
        static private List<Text> _contents = new ArrayList<Text>();

        public static void Add(Text textToAdd)
        {
            if(!_contents.contains(textToAdd))
            {
                _contents.add(textToAdd);    
            }
        }

        public static void Clear()
        {
            _contents.clear();
        }

        static public SpriteFont GetFont()
        {
            return actionFont;
        }

        public static void Update()
        {
            for (int ii = 0; ii < _contents.size(); ii++)
            {
                if(_contents.get(ii).Update()<=0)
                {
                    _contents.remove(_contents.get(ii));
                    ii--;
                }
            }
        }

        public static void Draw()
        {
            for(Text component: _contents)
            {
                if (XnaManager.Renderer != null)
                {
                    component.Draw();
                }
            }
        }

        public static void LoadContent()
        {
            actionFont = XnaManager.GetActionFont();
        }
    }
