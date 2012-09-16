package aigilas.hud;

import aigilas.creatures.ICreature;
import aigilas.items.Equipment;
import aigilas.items.GenericItem;
import aigilas.items.ItemSlot;
import spx.core.SpxManager;
import spx.util.StringSquisher;
import spx.util.StringStorage;

public class DeltasHud extends IHud {
    private Equipment _equipment;

    public DeltasHud(ICreature owner, Equipment equipment) {
        super(owner, SpxManager.WindowWidth / 2, SpxManager.WindowHeight / 2);
        _equipment = equipment;
    }

    public void Draw() {
        if (_isVisible) {
            _textHandler.Draw();
        }
    }

    private GenericItem GetEquipmentIn(ItemSlot slot) {
        if (_equipment.GetItems().containsKey(slot)) {
            return _equipment.GetItems().get(slot);
        }
        return null;
    }

    private static final String delim = "|";
    private static final String positive = "+";
    private static final String title = "Deltas";
    private String display = "EMPTY";

    public void Update(GenericItem item, boolean refresh) {
        if (_isVisible) {
            _textHandler.Update();
            _textHandler.Clear();
            if (item != null && refresh) {
                if (GetEquipmentIn(item.GetItemClass().Slot) != null) {
                    StringSquisher.Clear();
                    for (Float stat : GetEquipmentIn(item.GetItemClass().Slot).Modifers.GetDeltas(item.Modifers)) {
                        StringSquisher.Squish(((stat > 0) ? positive : ""), StringStorage.Get(stat), delim);
                    }
                    display = StringSquisher.Flush();
                }
            }
            _textHandler.WriteDefault(title, 30, 260, GetHudOrigin());
            _textHandler.WriteDefault(display, 30, 290, GetHudOrigin());
        }
    }
}