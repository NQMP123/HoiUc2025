package com.ngocrong.server;

public class SQLStatement {

    public static final String INIT_CLAN_IMAGE = "SELECT * FROM `nr_clan_image`";
    public static final String INIT_SKILL_DISCIPLE = "SELECT * FROM `nr_skill_disciple`";
    public static final String INIT_GAME_INFO = "SELECT * FROM `nr_game_info`";
    public static final String INIT_SPECIAL_SKILL = "SELECT * FROM `nr_special_skill`";
    public static final String INIT_CAPTION = "SELECT * FROM `nr_caption`";
    public static final String INIT_POWER = "SELECT * FROM `nr_power`";
    public static final String INIT_DART = "SELECT * FROM `nr_dart`";
    public static final String INIT_ARROW = "SELECT * FROM `nr_arrow`";
    public static final String INIT_EFFECT = "SELECT * FROM `nr_effect`";
    public static final String INIT_IMAGE_BY_NAME = "SELECT * FROM `nr_image_by_name`";
    public static final String INIT_IMAGE = "SELECT * FROM `nr_image`";
    public static final String INIT_PART = "SELECT * FROM `nr_part`";
    public static final String INIT_SKILL_PAINT = "SELECT * FROM `nr_skill_paint`";
    public static final String INIT_EFFECT_DATA = "SELECT * FROM `nr_effect_data`";
    public static final String INIT_BACKGROUND = "SELECT * FROM `nr_background_item`";
    public static final String INIT_MAP = "SELECT * FROM `nr_map`";
    public static final String INIT_MOB_TEMPLATE = "SELECT * FROM `nr_mob_template`";
    public static final String INIT_NPC_TEMPLATE = "SELECT * FROM `nr_npc_template`";
    public static final String INIT_CONFIG = "SELECT * FROM `nr_others`";
    public static final String INIT_ITEM_OPTION = "SELECT * FROM `nr_item_option_template`";
    public static final String INIT_ITEM_TEMPLATE = "SELECT * FROM `nr_item`";
    public static final String INIT_TASK_TEMPLATE = "SELECT * FROM `nr_task`";
    public static final String INIT_SKILL_OPTION = "SELECT * FROM `nr_skill_option_template`";
    public static final String INIT_SKILL_TEMPLATE = "SELECT * FROM `nr_skill` WHERE `class` = ";
    public static final String INIT_ACHIEVEMENT = "SELECT * FROM `nr_achievement`";
    public static final String INIT_LUCKY_WHEEL = "SELECT * FROM `nr_lucky_wheel`";
    public static final String LOAD_COLLECTION = "SELECT * FROM `nr_collection_book`";
    public static final String REGISTER = "INSERT INTO `nr_user` (`username`, `password`, `status`, `gold_bar`, `coin`, `lock_gold`, `lock_time`, `activated`, `role`, `create_time`) VALUES (?, ?,0, 0, 0, NULL, NULL, 1, 1, NOW())";
    public static final String POWER_RANK = "SELECT `name`, CAST(JSON_UNQUOTE(JSON_EXTRACT(info,\"$.power\")) AS UNSIGNED) AS power from nr_player where server_id = ? ORDER BY power DESC LIMIT 100";
    public static final String TASK_RANK = "SELECT `name`, CAST(JSON_UNQUOTE(JSON_EXTRACT(task,\"$.id\")) AS UNSIGNED) AS taskId,"
            + "CAST(JSON_UNQUOTE(JSON_EXTRACT(task,\"$.index\")) AS UNSIGNED) AS taskIndex,"
            + "CAST(JSON_UNQUOTE(JSON_EXTRACT(task,\"$.count\")) AS UNSIGNED) AS taskCount "
            + "from nr_player ORDER BY taskId DESC, taskIndex DESC, taskCount DESC LIMIT 100";
}
