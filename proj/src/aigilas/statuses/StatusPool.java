package aigilas.statuses;

import aigilas.creatures.CreatureAction;
import aigilas.creatures.ICreature;
import aigilas.entities.Elements;

import java.util.ArrayList;
import java.util.List;

public class StatusPool {
    private List<IStatus> _statuses = new ArrayList<IStatus>();

    public boolean Allows(CreatureAction action) {
        for (int ii = 0; ii < _statuses.size(); ii++) {
            if (_statuses.get(ii).Prevents(action)) {
                return false;
            }
        }
        return true;
    }

    public void Add(IStatus status) {
        _statuses.add(status);
    }

    public void Update() {
        for (int ii = 0; ii < _statuses.size(); ii++) {
            _statuses.get(ii).Update();
            if (!_statuses.get(ii).IsActive()) {
                _statuses.remove(_statuses.get(ii));
                ii--;
            }
        }
    }

    public void Act() {
        for (int ii = 0; ii < _statuses.size(); ii++) {
            _statuses.get(ii).Act();
        }
    }

    public void PassOn(ICreature target, StatusComponent componentType) {
        for (int ii = 0; ii < _statuses.size(); ii++) {
            _statuses.get(ii).PassOn(target, componentType);
        }
    }

    public boolean IsElementBlocked(Elements element) {
        for (IStatus status : _statuses) {
            if (status.IsElementBlocked(element)) {
                return true;
            }
        }
        return false;
    }
}