package spx.io;import java.io.IOException;import java.io.ObjectInputStream;import java.io.ObjectOutputStream;import java.net.InetAddress;import java.net.ServerSocket;import java.net.Socket;import java.net.SocketTimeoutException;import java.util.ArrayList;import java.util.HashMap;import java.util.List;import spx.core.Settings;import spx.devtools.DevConsole;import xna.wrapper.Console;import xna.wrapper.Environment;public class Server extends Thread {	private boolean __otherServerExists;	private static int __listenTimeout = 1000;	private boolean isRunning = true;	private ServerSocket _server;	private HashMap<Integer, HashMap<Integer, Boolean>> _playerStatus = new HashMap<>();	private int _rngSeed = Environment.TickCount();	private MessageContents _message = MessageContents.Empty();	private Integer _turnCount = 0;	private boolean[] _readyCheckIn = { true, true, true, true };	private HashMap<InetAddress, Integer> _addressToIndexMap = new HashMap<>();	private List<Socket> _connections;	private ObjectInputStream _ois;	private ObjectOutputStream _oos;	public Server() {		try {			_server = new ServerSocket(Settings.Get().GetPort());			_server.setSoTimeout(__listenTimeout);			for (int ii = 0; ii < MessageContents.PlayerMax; ii++) {				_playerStatus.put(ii, new HashMap<Integer, Boolean>());				for (int jj = 0; jj < MessageContents.CommandMax; jj++) {					_playerStatus.get(ii).put(jj, false);				}			}			_connections = new ArrayList<Socket>();			__otherServerExists = false;			Console.WriteLine("Spinning up a server instance");		}		catch (Exception hide) {			__otherServerExists = true;			Console.WriteLine("SERVER: Failure to start. If this isn't the host machine, then this message is harmless.");			DevConsole.Get().Add("SERVER: Failure to start. If this isn't the host machine, then this message is harmless.");		}	}	public void run() {		while (isRunning) {			// try {			// Thread.sleep(100);			// }			// catch (InterruptedException e) {			// e.printStackTrace();			// }			pollForNewMessages();			broadCastGameState();		}	}	private void pollForNewMessages() {		try {			Socket client = null;			client = _server.accept();			if (client != null) {				if (Settings.Get().GetServerVerbose())					Console.WriteLine("SERVER: Client was valid");				_ois = new ObjectInputStream(client.getInputStream());				_oos = new ObjectOutputStream(client.getOutputStream());				_message = (MessageContents) _ois.readObject();				System.out.println("SERVER: Message received: " + _message.MessageType);				switch (_message.MessageType) {					case CONNECT:						Console.WriteLine("SERVER: New client connection");						InitPlayer(_connections.size(), 0);						SendMessage(MessageContents.CreateInit(_connections.size(), _rngSeed));						_addressToIndexMap.put(client.getLocalAddress(), _connections.size());						_connections.add(client);						if (Settings.Get().GetServerVerbose())							Console.WriteLine("SERVER: Accepted new connection");						_turnCount = 0;					case CHECK_STATE:						InitPlayer(_message.PlayerIndex, _message.Command);						_message.IsActive = _playerStatus.get((int) _message.PlayerIndex).get((int) _message.Command);						if (Settings.Get().GetServerVerbose())							Console.WriteLine("SERVER: Check extends  CMD({1}) PI({0}) AC({2})", _message.PlayerIndex, _message.Command, _playerStatus.get((int) _message.PlayerIndex).get((int) _message.Command));						SendMessage(_message);						break;					case MOVEMENT:						InitPlayer(_message.PlayerIndex, _message.Command);						_playerStatus.get(_message.PlayerIndex).put((int) _message.Command, _message.IsActive);						if (Settings.Get().GetServerVerbose())							Console.WriteLine("SERVER: Moves extends  CMD({1}) PI({0}) AC({2})", _message.PlayerIndex, _message.Command, _message.IsActive);						break;					case START_GAME:						Console.WriteLine("SERVER: Announcing game commencement.");						Announce(_message);						break;					case PLAYER_COUNT:						if (Settings.Get().GetServerVerbose())							Console.WriteLine("SERVER: PLAYER COUNT");						SendMessage(MessageContents.CreatePlayerCount(_connections.size()));						break;					case READY_FOR_NEXT_TURN:						if (Settings.Get().GetServerVerbose())							Console.WriteLine("SERVER: Received ready signal from client");						_readyCheckIn[_addressToIndexMap.get(client.getLocalAddress())] = true;						break;					case HEART_BEAT:						_readyCheckIn[_addressToIndexMap.get(client.getLocalAddress())] = true;						break;					default:						if (Settings.Get().GetServerVerbose())							Console.WriteLine("SERVER: Unknown message");						break;				}			}			else {				if (Settings.Get().GetServerVerbose())					Console.WriteLine("SERVER: Client wasn't valid");			}		}		catch (SocketTimeoutException hide) {			hide.printStackTrace();		}		catch (Exception e) {			e.printStackTrace();		}	}	private void broadCastGameState() {		int readyCount = 0;		for (int ii = 0; ii < _readyCheckIn.length; ii++) {			readyCount += _readyCheckIn[ii] ? 1 : 0;		}		if (readyCount >= _connections.size()) {			if (Settings.Get().GetServerVerbose())				Console.WriteLine("SERVER: Announcing player input status.");			Announce(MessageContents.CreatePlayerState(_playerStatus, _turnCount++));			for (int ii = 0; ii < _readyCheckIn.length; ii++) {				_readyCheckIn[ii] = false;			}		}	}	private void InitPlayer(int playerIndex, int command) {		if (!_playerStatus.containsKey(playerIndex)) {			_playerStatus.put(playerIndex, new HashMap<Integer, Boolean>());		}		if (!_playerStatus.get(playerIndex).containsKey(command)) {			_playerStatus.get(playerIndex).put(command, false);		}	}	private void Announce(MessageContents contents) {		try {			for (Socket client : _connections) {				_oos = new ObjectOutputStream(client.getOutputStream());				SendMessage(contents);			}		}		catch (IOException e) {			e.printStackTrace();		}	}	private void SendMessage(MessageContents contents) {		try {			_oos.writeObject(contents);			_oos.flush();		}		catch (IOException e) {			e.printStackTrace();		}	}	public boolean IsOnlyInstance() {		return !__otherServerExists;	}	public void Close() {		isRunning = false;		System.out.println("SERVER: Shutting down");	}}