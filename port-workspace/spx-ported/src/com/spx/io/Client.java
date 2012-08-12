package com.spx.io;
    public class Client
    {
        private static Client __instance;
        public static Client Get()
        {
            if (__instance == null)
            {
                __instance = new Client();
                while (!__instance.IsConnected()) 
                {
                    __instance.Update();
                }
            }
            return __instance;
        }

        private NetClient _client;
        private NetPeerConfiguration _config;
        private NetIncomingMessage _message;
        private NetOutgoingMessage _outMessage;        
        private Integer _initialPlayerIndex;
        private boolean _isGameStarting;
        private boolean _isConnected;

        private HashMap<Integer, HashMap<Integer, Boolean>> _playerStatus = new HashMap<Integer, HashMap<Integer, Boolean>>();

        private Client()
        {
            _config = new NetPeerConfiguration(Server.ConnectionName);
            for (int ii = 0; ii < MessageContents.PlayerMax; ii++)
            {
                _playerStatus.put(ii, new HashMap<Integer, Boolean>());
                for (int jj = 0; jj < MessageContents.CommandMax; jj++)
                {
                    _playerStatus.get(ii).put(jj, false);
                }
            }
            _client = new NetClient(_config);
            _client.Start();
            NetOutgoingMessage init = _client.CreateMessage();
            init.Write(MessageTypes.CONNECT);      
            _client.Connect(Settings.Get().GetIp(), Settings.Get().GetPort(), init);
        }

        //Client<->Game communication
        public boolean IsGameStarting()
        {
            return _isGameStarting;
        }

        public boolean IsConnected()
        {
            return _isConnected;
        }


        private int _heartBeat = 30;
        public void HeartBeat()
        {
            if (!_dungeonHasLoaded)
            {
                _heartBeat--;
                if (_heartBeat <= 0)
                {
                    Console.WriteLine("CLIENT: Heartbeating...");
                    SendMessage(MessageContents.CreateHeartBeat());
                    _heartBeat = 15;
                }
            }
        }

        private boolean _dungeonHasLoaded = false;
        public void DungeonHasLoaded()
        {
            Console.WriteLine("CLIENT: Dungeon has finished loading...");
            _dungeonHasLoaded = true;
        }

        public boolean NextTurn()
        {
            _contents.Clear();
            Update();
            if (_contents.MessageType == MessageTypes.SYNC_STATE)
            {
                if (Settings.Get().GetClientVerbose()) DevConsole.Get().Add("RNG Test Broke?: " + RNG.Next(0, 100000));
                if (Settings.Get().GetClientVerbose()) DevConsole.Get().Add("CLIENT: Synced  extends  " + _contents.TurnCount + ". Seeding extends  " + _contents.RngSeed);
                RNG.Seed(_contents.RngSeed);
                if (Settings.Get().GetClientVerbose()) DevConsole.Get().Add("RNG Test extends  " + RNG.Next(0, 100000) + " extends  Turn - " + _contents.TurnCount);
                _heartBeat = 15;
            }
            return _contents.MessageType == MessageTypes.SYNC_STATE;
        }

        private void InitPlayer(int playerIndex, int command)
        {
            if (!_playerStatus.containsKey(playerIndex))
            {
                _playerStatus.put(playerIndex, new HashMap<Integer, Boolean>());
            }
            if (!_playerStatus.get(playerIndex).containsKey(command))
            {
                _playerStatus.get(playerIndex).put(command, false);
            }
        }

        public int GetFirstPlayerIndex()
        {
            return _initialPlayerIndex;
        }
        

        //Client<->Server communication
        public boolean IsActive(int command, int playerIndex)
        {
            if (_playerStatus.containsKey(playerIndex) && _playerStatus.get(playerIndex).containsKey(command))
            {
                return _playerStatus.get(playerIndex).get(command);
            }
            return false;
        }

        public void SetState(int command, int playerIndex, boolean isActive)
        {
            InitPlayer(playerIndex, command);
            if (_playerStatus.get(playerIndex).get(command) != isActive)
            {
                if(Settings.Get().GetClientVerbose())Console.WriteLine("CLIENT: Moves extends  CMD({0}) PI({1}) AC({2})", command, playerIndex, isActive);
                SendMessage(MessageContents.CreateMovement(command, playerIndex, isActive));
            }
        }

        int _playerCount = 0;
        public int GetPlayerCount()
        {
            if (_playerCount == 0)
            {
                SendMessage(MessageContents.CreatePlayerCount(0));
                AwaitReply(MessageTypes.PLAYER_COUNT);
                _playerCount = _contents.PlayerCount;
            }
            return _playerCount;
        }

        public void StartGame()
        {
            SendMessage(MessageContents.Create(MessageTypes.START_GAME));
        }

        private void SendMessage(MessageContents contents)
        {
            _outMessage = _client.CreateMessage(MessageContents.ByteCount);
            contents.Serialize(_outMessage);
            _client.SendMessage(_outMessage, NetDeliveryMethod.ReliableOrdered);
        }

        //If the server doesn't reply at some point with the messageType you expect
        //Then the client will hang extends an infinite loop.

        private void AwaitReply(byte messageType)
        {
            if (Settings.Get().GetClientVerbose()) Console.WriteLine("CLIENT: Waiting for " + CmtString.Get(messageType));          
            while (true)
            {
                if (Settings.Get().GetClientVerbose()) Console.WriteLine("CLIENT: Waiting");
                _client.MessageReceivedEvent.WaitOne();
                _message = _client.ReadMessage();
                if (_message != null)
                {
                   
                    if (_message.MessageType == NetIncomingMessageType.Data)
                    {
                        _contents.Deserialize(_message);
                        if (_contents.MessageType == messageType)
                        {
                            if (Settings.Get().GetClientVerbose()) Console.WriteLine("CLIENT: Right message received");
                            return;
                        }
                        else
                        {
                            if(Settings.Get().GetClientVerbose())Console.WriteLine("CLIENT: Wrong message received extends  "+_contents.MessageType+" Expected extends  "+messageType);
                            HandleResponse(_contents);
                        }                            
                    }
                    else
                    {
                        if (Settings.Get().GetClientVerbose()) Console.WriteLine("CLIENT: Unexpected  extends  " + _message.MessageType + " extends  " + _message.ReadString());
                    }
                    _client.Recycle(_message);
                }                
            }
        }

        private void HandleResponse(MessageContents contents)
        {
            switch (contents.MessageType)
            {
                case MessageTypes.CONNECT:
                    if (Settings.Get().GetClientVerbose()) Console.WriteLine("CLIENT: Handshake successful. Starting player id extends  " + contents.PlayerIndex);
                    RNG.Seed(contents.RngSeed);
                    _initialPlayerIndex = (int) contents.PlayerCount;
                    _isConnected = true;
                    break;
                case MessageTypes.START_GAME:
                    Console.WriteLine("CLIENT: Start game reply has been received");
                    _isGameStarting = true;
                    break;
                case MessageTypes.SYNC_STATE:
                    if (Settings.Get().GetClientVerbose()) Console.WriteLine("CLIENT: Input state received");
                    contents.ReadPlayerState(_playerStatus);
                    break;
                default:
                    break;
            }
        }

        private MessageContents _contents = MessageContents.Empty();
        public void Update()
        {
            while ((_message = _client.ReadMessage()) != null && _message.MessageType == NetIncomingMessageType.Data)
            {
                _contents.Deserialize(_message);
                HandleResponse(_contents);
                _client.Recycle(_message);
            }
        }

        public void PrepareForNextTurn()
        {
            SendMessage(MessageContents.CreateReadyForNextTurn());
        }

        public void Close()
        {
            _client.Shutdown("CLIENT: Shutting down");
        }
    }