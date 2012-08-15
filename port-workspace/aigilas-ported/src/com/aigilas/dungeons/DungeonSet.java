package com.aigilas.dungeons;

import java.util.HashMap;

import com.spx.particles.ParticleEngine;

public class DungeonSet
{
    private int _currentFloor = 0;
    private HashMap<Integer,Dungeon> _floors = new HashMap<Integer, Dungeon>();

    /*
     * This whole "area" thing is very messy and doesn't work:an intuitive way.
     * In order to pre-load, we need to make sure we can easily get back to start.
     * */
    public DungeonSet() throws Exception
    {
        _floors.put(_currentFloor, new Dungeon());
    }

    public void GotoNext() throws Exception
    {
        _floors.get(_currentFloor).CacheContents();
        _currentFloor++;
        LoadOrCreateDungeon(false);
    }

    public boolean GotoPrevious() throws Exception
    {
        if (_currentFloor > 0)
        {
            _floors.get(_currentFloor).CacheContents();
            _currentFloor--;
            LoadOrCreateDungeon(true);
            return true;
        }
        return false;
    }

    private void LoadOrCreateDungeon(boolean goingUp) throws Exception
    {
        if (!_floors.containsKey(_currentFloor))
        {
            _floors.put(_currentFloor, new Dungeon());
            DungeonFactory.IncreaseFloorCount();
        }
        ParticleEngine.Reset();
        _floors.get(_currentFloor).LoadTiles(goingUp);
    }
}