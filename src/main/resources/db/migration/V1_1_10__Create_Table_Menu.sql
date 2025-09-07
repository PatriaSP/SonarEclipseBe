-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS "menu" CASCADE;
CREATE TABLE "menu" (
  "id" BIGSERIAL NOT NULL,
  "title" varchar(255) COLLATE "pg_catalog"."default",
  "icon" varchar(255) COLLATE "pg_catalog"."default",
  "to" varchar(255) COLLATE "pg_catalog"."default",
  "parent" int4
)
;

-- ----------------------------
-- Primary Key structure for table menu
-- ----------------------------
ALTER TABLE "menu" ADD CONSTRAINT "menu_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Foreign Keys structure for table menu
-- ----------------------------
ALTER TABLE "menu" ADD CONSTRAINT "fk_parent" FOREIGN KEY ("parent") REFERENCES "menu" ("id") ON DELETE SET NULL ON UPDATE CASCADE;
