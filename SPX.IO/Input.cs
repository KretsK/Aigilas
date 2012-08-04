﻿using System;
using System.Collections.Generic;
using System.Linq;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Input;
using System.Threading;
using SPX.DevTools;

namespace SPX.IO
{
    public interface IInputInitializer
    {
        ICollection<CommandDefinition> GetCommands();
    }
    public class Contexts
    {
        public const int All = 0;
        public const int Nonfree = 1;
        public const int Free = 2;
        public const int Inventory = 3;
    }
    public class CommandDefinition
    {
        public CommandDefinition(int command, Keys key, Buttons button, int lockContext)
        {
            Command = command;
            Gamepad = button;
            Keyboard = key;
            LockContext = lockContext;
        }
        public int Command{get;set;}
        public Buttons Gamepad{get;set;}
        public Keys Keyboard{get;set;}
        public int LockContext { get; set; }
    }
    public class Input
    {        
        //Maps a playerId to a context
        private static readonly Dictionary<int, int> __contexts = 
            new Dictionary<int, int>()
            {
                {0, Contexts.Free},
                {1, Contexts.Free},
                {2, Contexts.Free},
                {3, Contexts.Free}
            };

        //Lists what commands are locked for a given player
        private static readonly List<CommandLock> __locks = new List<CommandLock>(); 

        //The commands that cannot be used by simply holding down the command's input depending on the given context
        private static readonly Dictionary<int, int> __lockOnPress = new Dictionary<int, int>();

        private static readonly Dictionary<int, Keys> _keyboardMapping = new Dictionary<int, Keys>();

        private static readonly Dictionary<int, Buttons> _gamePadMapping = new Dictionary<int, Buttons>();

        private static readonly List<PlayerIndex> _playerIndex = new List<PlayerIndex>()
                                                                      {
                                                                          PlayerIndex.One,
                                                                          PlayerIndex.Two,
                                                                          PlayerIndex.Three,
                                                                          PlayerIndex.Four
                                                                      };
        private static readonly Dictionary<int, bool> __inputs = new Dictionary<int, bool>();
        private static bool __isInputActive = false;

        public static void Setup(IInputInitializer initializer)
        {
            foreach (var command in initializer.GetCommands())
            {
                _keyboardMapping.Add(command.Command, command.Keyboard);
                _gamePadMapping.Add(command.Command, command.Gamepad);
                if (command.LockContext >= 0)
                {
                    __lockOnPress.Add(command.Command, command.LockContext);
                }
            }
        }

        public static bool DetectState(int command, int playerIndex)
        {
            return GamePad.GetState(_playerIndex[playerIndex]).IsButtonDown(_gamePadMapping[command])
            ||
            (playerIndex == Client.Get().GetFirstPlayerIndex() && Keyboard.GetState().IsKeyDown(_keyboardMapping[command]));
        }

        private static bool IsDown(int command, int playerIndex)
        {
            return Client.Get().IsActive(command, playerIndex);            
        }

        public static bool IsActive(int command, int playerIndex,bool failIfLocked=true)
        {
            __isInputActive = IsDown(command,playerIndex);
            if (!__isInputActive && ShouldLock(command,playerIndex))
            {
                Unlock(command, playerIndex);
            }

            if (IsLocked(command, playerIndex) && failIfLocked)
            {
                return false;
            }

            if (__isInputActive && ShouldLock(command,playerIndex))
            {
                Lock(command,playerIndex);
            }

            return __isInputActive;
        }

        //If the key is marked to be locked on press and its lock context is currently inactive
        private static bool ShouldLock(int command,int playerIndex)
        {
            foreach(var key in __lockOnPress.Keys)
            {
                if (key == command)
                {
                    if (__lockOnPress[key] == __contexts[playerIndex] ||
                        (__lockOnPress[key] == Contexts.Nonfree && __contexts[playerIndex] != Contexts.Free) ||
                        __lockOnPress[key] == Contexts.All)
                    {
                        return true;
                    }
                }
            }
            return false;
        }

        public static void SetContext(int context,int playerIndex)
        {
            __contexts[playerIndex] = context;
        }
        
        public static bool IsContext(int context,int playerIndex)
        {
            return __contexts[playerIndex] == context;
        }

        public static bool IsLocked(int command,int playerIndex)
        {
            foreach (var pair in __locks)
            {
                if (pair.Command == command && pair.PlayerIndex == playerIndex)
                {
                    return true;
                }
            }
            return false;
        }

        public static void Lock(int command,int playerIndex)
        {
            __locks.Add(new CommandLock(command,playerIndex));
        }

        public static void Unlock(int command, int playerIndex)
        {
            for(int ii = 0;ii<__locks.Count();ii++)
            {
                if(__locks[ii].Command==command && __locks[ii].PlayerIndex==playerIndex)
                {
                    __locks.Remove(__locks[ii]);
                    ii--;
                }
            }
        }

        private static readonly List<PlayerIndex> __playerIndices = new List<PlayerIndex>()
        {
            PlayerIndex.One,
            PlayerIndex.Two,
            PlayerIndex.Three,
            PlayerIndex.Four,
        };

        public static void Update()
        {
            //Remove command locks if the associated key/button isn't being pressed
            for(int ii = 0;ii<__locks.Count();ii++)
            {
                if (!IsDown(__locks[ii].Command, __locks[ii].PlayerIndex))
                {
                    __locks.Remove(__locks[ii]);
                    ii--;
                }
            }
            
            foreach (int command in _keyboardMapping.Keys)
            {
                Client.Get().SetState(command, Client.Get().GetFirstPlayerIndex(), DetectState(command, Client.Get().GetFirstPlayerIndex()));
            }
        }
        private class CommandLock
        {
            public CommandLock(int command, int playerIndex)
            {
                Command = command;
                PlayerIndex = playerIndex;
            }
            public int Command;
            public int PlayerIndex { get; set; }
        }
    }
}