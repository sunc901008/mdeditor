DROP TABLE IF EXISTS md_editor;

CREATE TABLE md_editor (
  `id`          bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `ip`          varchar(30)         NOT NULL,
  `content`     TEXT                NULL     DEFAULT '',
  `create_time` datetime            NOT NULL,
  `update_time` datetime            NOT NULL,
  PRIMARY KEY (id)
);
