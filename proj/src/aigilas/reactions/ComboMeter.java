package aigilas.reactions;

import aigilas.creatures.ICreature;
import aigilas.entities.ComboMarker;
import aigilas.entities.Elements;
import spx.text.ActionText;
import spx.text.TextManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ComboMeter {
    private static HashMap<Integer, ReactionId> __reactions = new HashMap<>();

    static {
        __reactions.put(12, ReactionId.SWEAT);
        __reactions.put(13, ReactionId.MAGMA);
        __reactions.put(14, ReactionId.EXPLOSION);
        __reactions.put(15, ReactionId.SCORCH);
        __reactions.put(16, ReactionId.BLIND);
        __reactions.put(17, ReactionId.LACTIC_ACID);
        __reactions.put(18, ReactionId.MIND_BLOWN);
        __reactions.put(23, ReactionId.VENT);
        __reactions.put(24, ReactionId.DROWN);
        __reactions.put(25, ReactionId.REFLECT);
        __reactions.put(26, ReactionId.DRENCH);
        __reactions.put(27, ReactionId.PNEUMONIA);
        __reactions.put(28, ReactionId.LOBOTOMY);
        __reactions.put(34, ReactionId.RUST);
        __reactions.put(35, ReactionId.PURIFY);
        __reactions.put(36, ReactionId.ECLIPSE);
        __reactions.put(37, ReactionId.RESPECT);
        __reactions.put(38, ReactionId.CRAFTSMAN);
        __reactions.put(45, ReactionId.FLASH);
        __reactions.put(46, ReactionId.METABOLISM);
        __reactions.put(47, ReactionId.FAST_FORWARD);
        __reactions.put(48, ReactionId.BLANK);
        __reactions.put(56, ReactionId.YIN_YANG);
        __reactions.put(57, ReactionId.EXPOSE);
        __reactions.put(58, ReactionId.ENLIGHTEN);
        __reactions.put(67, ReactionId.ATROPHY);
        __reactions.put(68, ReactionId.NEUROSIS);
        __reactions.put(78, ReactionId.CONFUSE);
    }

    ;

    private ICreature _parent;
    private List<Elements> _elements = new ArrayList<>();
    private List<ComboMarker> _markers = new ArrayList<>();
    private static final int _maxTimer = 120;
    private int _reactionTimer = _maxTimer;

    public ComboMeter(ICreature parent) {
        _parent = parent;
    }

    private void ResetComboDisplay() {
        _reactionTimer = _maxTimer;
        for (int ii = 0; ii < _markers.size(); ii++) {
            _markers.get(ii).SetInactive();
        }
        _markers.clear();
        int jj = 0;
        for (Elements element : _elements) {
            _markers.add(new ComboMarker(_parent, element, jj));
            jj++;
            _markers.get(_markers.size() - 1).LoadContent();
        }
    }

    public void Draw() {
        for (ComboMarker marker : _markers) {
            marker.Draw();
        }
    }

    public void Add(Elements element) {
        if (!_elements.contains(element)) {
            if (_elements.size() == 2) {
                if (_elements.get(0).Value > element.Value) {
                    _elements.add(0, element);
                } else if (_elements.get(1).Value > element.Value) {
                    _elements.add(1, element);
                } else {
                    _elements.add(element);
                }
            } else if (_elements.size() == 1) {
                if (_elements.get(0).Value > element.Value) {
                    _elements.add(0, element);
                } else {
                    _elements.add(element);
                }
            }
            if (_elements.size() == 0) {
                _elements.add(element);
            }
            ResetComboDisplay();
        }
    }

    private IReaction reaction;

    public void Update() {
        for (ComboMarker marker : _markers) {
            marker.Update();
        }
        int key = 0;
        if (_elements.size() == 3) {
            key = _elements.get(0).Value * 100 + _elements.get(1).Value * 10 + _elements.get(2).Value;
            React(key);
        }
        if (_elements.size() == 2) {
            key = _elements.get(0).Value * 10 + _elements.get(1).Value;
            React(key);
        }
        if (_elements.size() == 1) {
            React(_elements.get(0).Value);
        }
    }

    private void React(int reactionId) {
        _reactionTimer--;
        if (_reactionTimer <= 0) {
            if (__reactions.keySet().contains(reactionId)) {
                reaction = ReactionFactory.Create(__reactions.get(reactionId));
                if (reaction != null) {
                    reaction.Affect(_parent);
                    TextManager.Add(new ActionText(__reactions.get(reactionId).toString(), 10, (int) _parent.GetLocation().PosX, (int) _parent.GetLocation().PosY));
                }
            }
            _elements.clear();
            ResetComboDisplay();
            _reactionTimer = _maxTimer;
        }
    }
}