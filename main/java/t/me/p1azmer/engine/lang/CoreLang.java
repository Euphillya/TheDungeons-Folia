package t.me.p1azmer.engine.lang;

import t.me.p1azmer.engine.api.lang.LangKey;

public class CoreLang {

    public static final LangKey CORE_COMMAND_USAGE     = new LangKey("Core.Command.Usage", "&7�����������: &c/%command_label% &6%command_usage%");
    public static final LangKey CORE_COMMAND_HELP_LIST = new LangKey("Core.Command.Help.List",
            "&6&m              &6&l[ &e&l%plugin_name_localized% &7- &e&l������� &6&l]&6&m              &7\n" +
                    "&7\n" +
                    "&7\n" +
                    "&6\u25aa &e/%command_label% &6%command_usage% &7- %command_description%\n" +
                    "&7\n");
    public static final LangKey CORE_COMMAND_HELP_DESC   = new LangKey("Core.Command.Help.Desc", "�������� ������.");
    public static final LangKey CORE_COMMAND_EDITOR_DESC = new LangKey("Core.Command.Editor.Desc", "���� ���������.");
    public static final LangKey CORE_COMMAND_ABOUT_DESC  = new LangKey("Core.Command.About.Desc", "��-���� ���������� � �������.");
    public static final LangKey CORE_COMMAND_RELOAD_DESC = new LangKey("Core.Command.Reload.Desc", "������������� ������.");
    public static final LangKey CORE_COMMAND_RELOAD_DONE = new LangKey("Core.Command.Reload.Done", "������� �������������!");

    public static final LangKey TIME_DAY  = new LangKey("Time.Day", "%s%�.");
    public static final LangKey TIME_HOUR = new LangKey("Time.Hour", "%s%�.");
    public static final LangKey TIME_MIN = new LangKey("Time.Min", "%s%���.");
    public static final LangKey TIME_SEC = new LangKey("Time.Sec", "%s%���.");
    public static final LangKey OTHER_FREE  = new LangKey("Other.Free", "&6���������");
    public static final LangKey OTHER_YES = new LangKey("Other.Yes", "&a��");
    public static final LangKey OTHER_NO   = new LangKey("Other.No", "&c���");
    public static final LangKey OTHER_ANY   = new LangKey("Other.Any", "�����");
    public static final LangKey OTHER_NONE     = new LangKey("Other.None", "�����");
    public static final LangKey OTHER_NEVER     = new LangKey("Other.Never", "�������");
    public static final LangKey OTHER_ONE_TIMED = new LangKey("Other.OneTimed", "�����������");
    public static final LangKey OTHER_UNLIMITED = new LangKey("Other.Unlimited", "�����������");
    public static final LangKey OTHER_INFINITY  = new LangKey("Other.Infinity", "\u221e");

    public static final LangKey ERROR_PLAYER_INVALID = new LangKey("Error.Player.Invalid", "&c����� �� ������.");
    public static final LangKey ERROR_WORLD_INVALID   = new LangKey("Error.World.Invalid", "&c��� �� ������.");
    public static final LangKey ERROR_NUMBER_INVALID  = new LangKey("Error.Number.Invalid", "&7%num% &c���������������� �����.");
    public static final LangKey ERROR_PERMISSION_DENY = new LangKey("Error.Permission.Deny", "&c���� ����!");
    public static final LangKey ERROR_ITEM_INVALID = new LangKey("Error.Item.Invalid", "&c�� ������ ������� �������!");
    public static final LangKey ERROR_TYPE_INVALID   = new LangKey("Error.Type.Invalid", "������������ ���. ���������: %types%");
    public static final LangKey ERROR_COMMAND_SELF   = new LangKey("Error.Command.Self", "������ ������������ �� ����.");
    public static final LangKey ERROR_COMMAND_SENDER = new LangKey("Error.Command.Sender", "�������, ����, ��� ��� �������.");
    @Deprecated
    public static final LangKey ERROR_INTERNAL       = new LangKey("Error.Internal", "&c���������� ������!");

    public static final LangKey NOT_DONATER       = new LangKey("Player.Not.Donater", "&c������ ����� ������������, ����� ������������ ���!");

    public static final LangKey CANT_ADD_ITEM_AND_DROP = LangKey.of("Player.Cant.Add.Item.DungeonReward", "&c��������! ��� ��������� ����� � ��������� �������� ����� ����� ��� �� �����!");

    public static final LangKey EDITOR_TIP_EXIT = LangKey.of("Editor.Tip.Exit", "<! prefix:\"false\" !> <? showText:\"&7����� ��� ����� &f#exit\" run_command:\"/#exit\" ?>&b�����, ����� ����� �� &d������ ���������</>");
    public static final LangKey EDITOR_TITLE_DONE             = LangKey.of("Editor.Title.Done", "&a&l�������!");
    public static final LangKey EDITOR_TITLE_EDIT           = LangKey.of("Editor.Title.Edit", "&a&l< ����� ��������� >");
    public static final LangKey EDITOR_TITLE_ERROR          = LangKey.of("Editor.Title.Error", "&c&l������!");
    public static final LangKey EDITOR_ERROR_NUMBER_GENERIC = LangKey.of("Editor.Error.Number.Generic", "&7�������� �����!");
    public static final LangKey EDITOR_ERROR_NUMBER_NOT_INT = LangKey.of("Editor.Error.Number.NotInt", "&7����� ������ ���� &c������&7!");
    public static final LangKey EDITOR_ERROR_ENUM           = LangKey.of("Editor.Error.Enum", "&7�������� ���! ������ � ���.");

    public static LangKey EDITOR_WRITE_NAME = LangKey.of("Editor.Tip.Write.Name", "������� ������������ ��������");
    public static LangKey EDITOR_WRITE_INTEGER = LangKey.of("Editor.Tip.Write.Integer", "������� �����");
    public static LangKey EDITOR_CREATE_EXIST = LangKey.of("Editor.Exist", "����� ID ��� ����������!");
    public static LangKey EDITOR_CREATE_TIP = LangKey.of("Editor.Tip.Create", "������� ���������� ID");
}