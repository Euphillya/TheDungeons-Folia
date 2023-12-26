package t.me.p1azmer.plugin.dungeons.editor;

import t.me.p1azmer.engine.api.editor.EditorLocale;
import t.me.p1azmer.plugin.dungeons.Placeholders;
import t.me.p1azmer.plugin.dungeons.dungeon.Dungeon;

public class EditorLocales extends t.me.p1azmer.engine.api.editor.EditorLocales {

    private static final String PREFIX = "Editor.DungeonEditorType."; // Old version compatibility

    public static final EditorLocale DUNGEON_EDITOR = builder(PREFIX + "EDITOR_DUNGEON")
            .name("Dungeons")
            .text("Create and manage your dungeons here!").breakLine()
            .actionsHeader().action("LMB", "Open").build();
    public static final EditorLocale KEYS_EDITOR = builder(PREFIX + "EDITOR_KEYS")
            .name("Keys")
            .text("Create and manage your keys here!").breakLine()
            .actionsHeader().action("LMB", "Open").build();

    public static final EditorLocale DUNGEON_OBJECT = builder(PREFIX + "DUNGEON_OBJECT")
            .name(Placeholders.DUNGEON_NAME + " &7(ID: &f" + Placeholders.DUNGEON_ID + "&7)")
            .actionsHeader()
            .action("LMB", "Configure")
            .action("RMB+SHIFT", "Delete " + RED + "(No Undo)").build();

    public static final EditorLocale DUNGEON_CREATE = builder(PREFIX + "DUNGEON_CREATE")
            .name("Create Dungeon")
            .text("Create a new dungeon.")
            .actionsHeader().action("LMB", "Create").build();

    public static final EditorLocale DUNGEON_NAME = builder(PREFIX + "DUNGEON_CHANGE_NAME")
            .name("Name")
            .text("Sets the displayed name of the dungeon.", "Used in messages and menus.").breakLine()
            .currentHeader().current("Displayed Name", Placeholders.DUNGEON_NAME).breakLine()
            .actionsHeader().action("LMB", "Change").build();

    public static final EditorLocale DUNGEON_KEYS = builder(PREFIX + "DUNGEON_CHANGE_KEYS")
            .name("Attached Keys")
            .text("Specifies which keys can be used to open this dungeon.").breakLine()
            .currentHeader().current("ID", Placeholders.DUNGEON_KEY_IDS).breakLine()
            .warningHeader().warning("If no keys are set, the dungeon can be opened without them!")
            .warning("If incorrect keys are provided, you won't be able to open the dungeon!").breakLine()
            .actionsHeader().action("LMB", "Attach Key").action("RMB", "Clear List")
            .build();

    public static final EditorLocale DUNGEON_BLOCK_HOLOGRAM_OPEN = builder(PREFIX + "DUNGEON_CHANGE_BLOCK_HOLOGRAM_OPEN")
            .name("Hologram - Stage=Opening")
            .text("Sets the hologram text when the dungeon is in the opening stage.").breakLine()
            .noteHeader()
            .action("You can specify the time until the dungeon opens", "")
            .action("using the placeholder", "dungeon_open_in")
            .breakLine()
            .current("Opening Text", "").text(Placeholders.DUNGEON_HOLOGRAM_TEXT_OPEN.toString()).breakLine()
            .actionsHeader().action("LMB", "Add Text").action("RMB+SHIFT", "Clear")
            .build();
    public static final EditorLocale DUNGEON_BLOCK_HOLOGRAM_CLOSE = builder(PREFIX + "DUNGEON_CHANGE_BLOCK_HOLOGRAM_CLOSE")
            .name("Hologram - Stage=Closure")
            .text("Sets the hologram text when the dungeon is open.").breakLine()
            .noteHeader()
            .action("You can specify the time until the dungeon closes", "")
            .action("using the placeholder", "dungeon_hologram_text_close")
            .breakLine()
            .current("Closure Text", "").text(Placeholders.DUNGEON_HOLOGRAM_TEXT_CLOSE).breakLine()
            .actionsHeader().action("LMB", "Add Text").action("RMB+SHIFT", "Clear")
            .build();

    public static final EditorLocale DUNGEON_BLOCK_HOLOGRAM_WAIT = builder(PREFIX + "DUNGEON_CHANGE_BLOCK_HOLOGRAM_WAIT")
            .name("Hologram - Stage=Waiting")
            .text("Sets the hologram text when the dungeon is in the waiting stage for opening (CLICK opening mode).").breakLine()
            .current("Waiting Text", "").text(Placeholders.DUNGEON_HOLOGRAM_TEXT_WAIT).breakLine()
            .actionsHeader().action("LMB", "Add Text").action("RMB+SHIFT", "Clear")
            .build();

    public static final EditorLocale DUNGEON_SCHEMATIC = builder(PREFIX + "DUNGEON_CHANGE_SCHEMATIC")
            .name("Schematics")
            .text("List of schematics that will be used").breakLine()
            .warningHeader().warning("The schematic must contain the block specified in the configuration.").breakLine()
            .warningHeader().warning("If the list is " + RED + "EMPTY" + GRAY + ", the dungeon won't work!").breakLine()
            .currentHeader().current("List", "").text(Placeholders.DUNGEON_SCHEMATICS)
            .breakLine()
            .actionsHeader().action("LMB", "Add").action("RMB+SHIFT", "Clear").build();

    public static final EditorLocale DUNGEON_OPEN_TYPE = builder(PREFIX + "DUNGEON_CHANGE_OPEN_TYPE")
            .name("Opening Type")
            .text("Sets the type of opening for the dungeon", Dungeon.OpenType.CLICK.name() + " - Opens the dungeon on click", Dungeon.OpenType.TIMER.name() + " - Opens the dungeon based on a timer").breakLine()
            .currentHeader().current("", Placeholders.DUNGEON_OPEN_TYPE).breakLine()
            .actionsHeader().action("LMB", "Change").build();

    public static final EditorLocale DUNGEON_REWARDS = builder(PREFIX + "DUNGEON_CHANGE_REWARDS")
            .name("Rewards")
            .text("Create and manage your rewards here!").breakLine()
            .actionsHeader().action("LMB", "Open")
            .build();

    public static final EditorLocale REWARD_OBJECT = builder(PREFIX + "REWARD_OBJECT")
            .name(Placeholders.REWARD_NAME + " &7(ID: &f" + Placeholders.REWARD_ID + "&7)")
            .text("Chance: &f" + Placeholders.REWARD_CHANCE + "%")
            .actionsHeader().action("LMB", "Configure")
            .action("LMB+SHIFT", "Move Forward").action("RMB+SHIFT", "Move Backward")
            .action("[Q/Drop] key", "Delete " + RED + "(No Undo)")
            .build();

    public static final EditorLocale REWARD_CREATE = builder(PREFIX + "REWARD_CREATE")
            .name("Create Reward")
            .text("Create a new reward for the dungeon.").breakLine()
            .actionsHeader().action("LMB", "Manual Creation")
            .action("Insert Item", "Quick Creation")
            .build();


    public static final EditorLocale REWARD_SORT = builder(PREFIX + "REWARD_SORT")
            .name("Reward Sorting")
            .text("Automatically sorts rewards in the specified order.").breakLine()
            .actionsHeader()
            .action("[Slot 1]", "by chance").action("[Slot 2]", "by type")
            .action("[Slot 3]", "by name")
            .build();
    public static final EditorLocale REWARD_NAME = builder(PREFIX + "REWARD_CHANGE_NAME")
            .name("Displayed Name")
            .text("Sets the displayed name of the reward.", "Used in menus and messages.").breakLine()
            .currentHeader().current("Displayed Name", Placeholders.REWARD_NAME).breakLine()
            .warningHeader().warning("This is " + RED + "NOT" + GRAY + " the actual name of the reward!").breakLine()
            .actionsHeader().action("LMB", "Change").action("RMB", "Take from Item")
            .action("LMB+SHIFT", "Set on Item")
            .build();

    public static final EditorLocale REWARD_ITEM = builder(PREFIX + "REWARD_CHANGE_ITEM")
            .name("Item")
            .text("The item that will be added to the chest").breakLine()
            .actionsHeader().action("Insert Item", "Replace Item").action("RMB", "Get a Copy")
            .build();

    public static final EditorLocale REWARD_CHANCE = builder(PREFIX + "REWARD_CHANGE_CHANCE")
            .name("Chance")
            .text("Sets the probability of the reward appearing in the chest.")
            .currentHeader().current("Chance", Placeholders.REWARD_CHANCE + "%").breakLine()
            .actionsHeader().action("LMB", "Change")
            .build();

    public static final EditorLocale REWARD_BROADCAST = builder(PREFIX + "REWARD_CHANGE_BROADCAST")
            .name("Notification")
            .text("Sets whether there will be a notification", "when a player finds this item", "in the dungeon.").breakLine()
            .currentHeader().current("Enabled", Placeholders.REWARD_BROADCAST).breakLine()
            .actionsHeader().action("LMB", "Toggle")
            .build();

    public static final EditorLocale REWARD_LIMITS = builder(PREFIX + "REWARD_CHANGE_LIMITS")
            .name("Item Limits")
            .text("Determines the quantity of the item that will be in the Dungeon").breakLine()
            .currentHeader()
            .current("Maximum", Placeholders.REWARD_MAX_AMOUNT)
            .current("Minimum", Placeholders.REWARD_MIN_AMOUNT).breakLine()
            .actionsHeader()
            .action("LMB", "Set Maximum Quantity")
            .action("RMB", "Set Minimum Quantity")
            .build();

    public static final EditorLocale KEY_OBJECT = builder(PREFIX + "KEY_OBJECT")
            .name(Placeholders.KEY_NAME + GRAY + " (ID: " + BLUE + Placeholders.KEY_ID + GRAY + ")")
            .actionsHeader().action("LMB", "Change")
            .action("RMB+SHIFT", "Delete " + RED + "(No Undo)")
            .build();

    public static final EditorLocale KEY_CREATE = builder(PREFIX + "KEY_CREATE")
            .name("Create Key")
            .text("Create a new key for dungeons.").breakLine()
            .actionsHeader().action("LMB", "Create")
            .build();

    public static final EditorLocale KEY_NAME = builder(PREFIX + "KEY_CHANGE_NAME")
            .name("Displayed Name")
            .text("Sets the displayed name of the key.", "Used in menus and messages.").breakLine()
            .currentHeader().current("Displayed Name", Placeholders.KEY_NAME).breakLine()
            .warningHeader().warning("This is " + RED + "NOT" + GRAY + " the actual name of the key!").breakLine()
            .actionsHeader().action("LMB", "Change")
            .build();

    public static final EditorLocale KEY_ITEM = builder(PREFIX + "KEY_CHANGE_ITEM")
            .name("Item")
            .text("Sets the physical item of the key.").breakLine()
            .noteHeader().notes("Use an item with a predefined name, description, etc.").breakLine()
            .actionsHeader().action("Insert Item", "Replace").action("RMB", "Get")
            .build();
    /**
     * Russian
     */

//    public static final EditorLocale DUNGEON_EDITOR = builder(PREFIX + "EDITOR_DUNGEON")
//            .name("�����")
//            .text("���������� ���� ����� � ���������� ��� �����!").breakLine()
//            .actionsHeader().action("���", "�������").build();
//
//    public static final EditorLocale KEYS_EDITOR = builder(PREFIX + "EDITOR_KEYS")
//            .name("�����")
//            .text("���������� ���� ����� � ���������� ��� �����!").breakLine()
//            .actionsHeader().action("���", "�������").build();
//
//    public static final EditorLocale DUNGEON_OBJECT = builder(PREFIX + "DUNGEON_OBJECT")
//            .name(Placeholders.DUNGEON_NAME + " &7(ID: &f" + Placeholders.DUNGEON_ID + "&7)")
//            .actionsHeader()
//            .action("���", "���������")
//            .action("���+����", "������� " + RED + "(��� ������)").build();
//
//    public static final EditorLocale DUNGEON_CREATE = builder(PREFIX + "DUNGEON_CREATE")
//            .name("������� ����")
//            .text("������� ����� ����.")
//            .actionsHeader().action("���", "�������").build();
//
//    public static final EditorLocale DUNGEON_NAME = builder(PREFIX + "DUNGEON_CHANGE_NAME")
//            .name("��������")
//            .text("������ ������������ ��� �����.", "��� ������������ � ���������� � ����.").breakLine()
//            .currentHeader().current("������������ ���", Placeholders.DUNGEON_NAME).breakLine()
//            .actionsHeader().action("���", "��������").build();
//
//    public static final EditorLocale DUNGEON_KEYS = builder(PREFIX + "DUNGEON_CHANGE_KEYS")
//            .name("������������� �����")
//            .text("�������������, ����� �����", "����� ������������ ��� �������� ����� �����.").breakLine()
//            .currentHeader().current("��", Placeholders.DUNGEON_KEY_IDS).breakLine()
//            .warningHeader().warning("���� ����� �� �����������, ���� ����� ������� � ��� ���!")
//            .warning("���� ������������� �������� �����, �� �� ������� ������� ����!").breakLine()
//            .actionsHeader().action("���", "���������� ����").action("���", "�������� ������")
//            .build();
//
//    public static final EditorLocale DUNGEON_BLOCK_HOLOGRAM_OPEN = builder(PREFIX + "DUNGEON_CHANGE_BLOCK_HOLOGRAM_OPEN")
//            .name("����������. ������=��������")
//            .text("������������� ����� ����������", "����� ���� ������ �������� ��������").breakLine()
//            .noteHeader()
//            .action("�� ������ ������� ����� �� �������� �����", "")
//            .action("��������� �����������", "dungeon_open_in")
//            .breakLine()
//            .current("����� ��������", "").text(Placeholders.DUNGEON_HOLOGRAM_TEXT_OPEN.toString()).breakLine()
//            .actionsHeader().action("���", "�������� �����").action("���+����", "��������")
//            .build();
//    public static final EditorLocale DUNGEON_BLOCK_HOLOGRAM_CLOSE = builder(PREFIX + "DUNGEON_CHANGE_BLOCK_HOLOGRAM_CLOSE")
//            .name("����������. ������=��������")
//            .text("������������� ����� ����������", "����� ���� ������").breakLine()
//            .noteHeader()
//            .action("�� ������ ������� ����� �� �������� �����", "")
//            .action("��������� �����������", "dungeon_hologram_text_close")
//            .breakLine()
//            .current("����� ��������", "").text(Placeholders.DUNGEON_HOLOGRAM_TEXT_CLOSE).breakLine()
//            .actionsHeader().action("���", "�������� �����").action("���+����", "��������")
//            .build();
//
//    public static final EditorLocale DUNGEON_BLOCK_HOLOGRAM_WAIT = builder(PREFIX + "DUNGEON_CHANGE_BLOCK_HOLOGRAM_WAIT")
//            .name("����������. ������=��������")
//            .text("������������� ����� ����������", "����� ���� ������� ��������", "� ������ �������� CLICK").breakLine()
//            .current("����� ��������", "").text(Placeholders.DUNGEON_HOLOGRAM_TEXT_CLOSE).breakLine()
//            .actionsHeader().action("���", "�������� �����").action("���+����", "��������")
//            .build();
//
//    public static final EditorLocale DUNGEON_SCHEMATIC = builder(PREFIX + "DUNGEON_CHANGE_SCHEMATIC")
//            .name("���������")
//            .text("������ ��������,", "������� ����� ����������").breakLine()
//            .warningHeader().warning("��������� ������ ��������� ��������� ����, ������� ������ � �������").breakLine()
//            .warningHeader().warning("���� ������ " + RED + "����" + GRAY + ", �� ���� �� ����� ��������!").breakLine()
//            .currentHeader().current("������", "").text(Placeholders.DUNGEON_SCHEMATICS)
//            .breakLine()
//            .actionsHeader().action("���", "��������").action("���+����", "��������").build();
//
//    public static final EditorLocale DUNGEON_OPEN_TYPE = builder(PREFIX + "DUNGEON_CHANGE_OPEN_TYPE")
//            .name("��� ��������")
//            .text("������������� ��� �������� �����", Dungeon.OpenType.CLICK.name() + " - ������� ���� �� �����", Dungeon.OpenType.TIMER.name() + " - ������� ���� �� �������").breakLine()
//            .currentHeader().current("", Placeholders.DUNGEON_OPEN_TYPE).breakLine()
//            .actionsHeader().action("���", "��������").build();
//
//    public static final EditorLocale DUNGEON_REWARDS = builder(PREFIX + "DUNGEON_CHANGE_REWARDS")
//            .name("�������")
//            .text("���������� ������� � ���������� ��� �����!").breakLine()
//            .actionsHeader().action("���", "�������")
//            .build();
//
//    public static final EditorLocale REWARD_OBJECT = builder(PREFIX + "REWARD_OBJECT")
//            .name(Placeholders.REWARD_NAME + " &7(ID: &f" + Placeholders.REWARD_ID + "&7)")
//            .text("����: &f" + Placeholders.REWARD_CHANCE + "%")
//            .actionsHeader().action("���", "���������")
//            .action("���+����", "����������� ������").action("���+����", "����������� �����")
//            .action("[Q/����] �������", "������� " + RED + "(��� ������)")
//            .build();
//
//    public static final EditorLocale REWARD_CREATE = builder(PREFIX + "REWARD_CREATE")
//            .name("������� �������")
//            .text("������� ����� ������� ��� �����.").breakLine()
//            .actionsHeader().action("���", "������ ��������")
//            .action("������� �������", "������� ��������")
//            .build();
//
//    public static final EditorLocale REWARD_SORT = builder(PREFIX + "REWARD_SORT")
//            .name("���������� ������")
//            .text("������������� ��������� ������� �", "��������� �������.").breakLine()
//            .actionsHeader()
//            .action("[���� 1]", "�� �����").action("[���� 2]", "�� ����")
//            .action("[���� 3]", "�� �����")
//            .build();
//
//    public static final EditorLocale REWARD_NAME = builder(PREFIX + "REWARD_CHANGE_NAME")
//            .name("������������ ��������")
//            .text("������������� ������������ ��� �������.", "��� ������������ � ���� � ����������.").breakLine()
//            .currentHeader().current("������������ ���", Placeholders.REWARD_NAME).breakLine()
//            .warningHeader().warning("��� " + RED + "��" + GRAY + " ����������� �������� �������!").breakLine()
//            .actionsHeader().action("���", "��������").action("���", "����� � ��������")
//            .action("���+����", "���������� �� �������")
//            .build();
//
//    public static final EditorLocale REWARD_ITEM = builder(PREFIX + "REWARD_CHANGE_ITEM")
//            .name("�������")
//            .text("������� ������� ����� �������� � ������").breakLine()
//            .actionsHeader().action("������� �������", "�������� �������").action("���", "�������� �����")
//            .build();
//
//    public static final EditorLocale REWARD_CHANCE = builder(PREFIX + "REWARD_CHANGE_CHANCE")
//            .name("����")
//            .text("������������� ����������� ��������� ������� � ������.")
//            .currentHeader().current("����", Placeholders.REWARD_CHANCE + "%").breakLine()
//            .actionsHeader().action("���", "��������")
//            .build();
//
//    public static final EditorLocale REWARD_BROADCAST = builder(PREFIX + "REWARD_CHANGE_BROADCAST")
//            .name("�����������")
//            .text("�������������, ����� �� ���������� � ���,", "��� ����� ����� ���� �������", "� �����.").breakLine()
//            .currentHeader().current("��������", Placeholders.REWARD_BROADCAST).breakLine()
//            .actionsHeader().action("���", "�����������")
//            .build();
//
//    public static final EditorLocale REWARD_LIMITS = builder(PREFIX + "REWARD_CHANGE_LIMITS")
//            .name("���������� ��������")
//            .text("���������� ���������� �������� ������� ����� � �����").breakLine()
//            .currentHeader()
//            .current("������������", Placeholders.REWARD_MAX_AMOUNT)
//            .current("�����������", Placeholders.REWARD_MIN_AMOUNT).breakLine()
//            .actionsHeader()
//            .action("���", "���������� ����. ����������")
//            .action("���", "���������� ���. ����������")
//            .build();
//
//    public static final EditorLocale KEY_OBJECT = builder(PREFIX + "KEY_OBJECT")
//            .name(Placeholders.KEY_NAME + GRAY + " (ID: " + BLUE + Placeholders.KEY_ID + GRAY + ")")
//            .actionsHeader().action("���", "��������")
//            .action("���+����", "������� " + RED + "(��� ������)")
//            .build();
//
//    public static final EditorLocale KEY_CREATE = builder(PREFIX + "KEY_CREATE")
//            .name("������� ����")
//            .text("������� ����� ���� ��� ������.").breakLine()
//            .actionsHeader().action("���", "�������")
//            .build();
//
//    public static final EditorLocale KEY_NAME = builder(PREFIX + "KEY_CHANGE_NAME")
//            .name("������������ ��������")
//            .text("������ ������������ ��� �����.", "��� ������������ � ���� � ����������.").breakLine()
//            .currentHeader().current("������������ ���", Placeholders.KEY_NAME).breakLine()
//            .warningHeader().warning("��� " + RED + "��" + GRAY + " ����������� �������� �����!").breakLine()
//            .actionsHeader().action("���", "��������")
//            .build();
//
//    public static final EditorLocale KEY_ITEM = builder(PREFIX + "KEY_CHANGE_ITEM")
//            .name("�������")
//            .text("������������� ���������� ������� �����.").breakLine()
//            .noteHeader().notes("����������� ������� � ������� �������� ������, ��������� � �.�.").breakLine()
//            .actionsHeader().action("������� �������", "��������").action("���", "��������")
//            .build();
}